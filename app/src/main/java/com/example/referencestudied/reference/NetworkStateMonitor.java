package com.example.referencestudied.reference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.example.referencestudied.util.LogUtil;

    /* 반영 방법
    OS7 이상 ================================================
    ✅ manifest 권한 선언
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    ✅ 전역변수 선언
    * 추가 : private NetworkStateMonitor networkMonitor;

    ✅ 호출 부분
    * 추가 : networkMonitor = new NetworkStateMonitor(getApplicationContext());

    ✅ onDestroy()
    * 추가 : networkMonitor.unregister();

    OS7 미만 ================================================
    ✅ FluteResponseService.onStartCommand()
    * 추가 : registerIntentFilter();

    ✅ onDestroy()
    * 추가 : unRegisterIntentFilter();
    */

/**
 * ConnectivityManager.CONNECTIVITY_ACTION은 API 28(Android 9.0)부터 deprecated 되었으므로 NetworkCallback 사용을 고려하는 것이 좋음
 * <p>
 * 이전에는 네트워크 상태 변경을 감지하기 위해 브로드캐스트 리시버를 사용하여 이 액션을 수신했지만, API 26(Android 8.0)부터는 암시적 브로드캐스트에 대한 정적 리시버 등록이 제한
 * 최신 버전에서는 registerNetworkCallback 또는 registerDefaultNetworkCallback과 같은 메서드를 사용하여 네트워크 상태 변경을 비동기적으로 처리하는 것이 권장
 * registerNetworkCallback() : 특정 네트워크 요청에 대한 변경 사항을 감지할 때 사용, Wi-Fi나 특정 VPN 연결 같은 특정 네트워크 유형에 대한 변경을 감지 가능
 * 현재사용코드 >> registerDefaultNetworkCallback() : 기기의 기본 네트워크(현재 활성화된 네트워크) 상태가 바뀔 때 호출됨, 특정 네트워크 유형을 지정하지 않고, 모든 네트워크 변경을 감지
 */
public class NetworkStateMonitor {
    private final Context context;
    private ConnectivityManager connectivityManager = null;
    private static boolean isConnectedNetwork = true;
    private static ConnectivityManager.NetworkCallback networkCallback;

    public NetworkStateMonitor(Context context) {
        this.context = context.getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) {
                LogUtil.e("[NetworkCallback] ConnectivityManager is NULL!");
                return;
            }
        }

        register();
    }

    private void register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // API 28 이상: NetworkCallback 사용
            if (networkCallback == null) { // 중복 등록 방지
                registerNetworkCallback();
            }

        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 해당 리시버가 다른 앱에 내보내질지 여부를 나타내는 플래그를 명시적 지정(단, 시스템 브로드 캐스트만 수신하는 리시버의 경우 플래그 지정이 필요하지 않음)
                    // Context.RECEIVER_EXPORTED > 외부 앱도 네트워크 상태 변경 감지를 허용
                    context.registerReceiver(networkReceiver, filter, Context.RECEIVER_NOT_EXPORTED); // 이 앱 내부에서만 네트워크 상태 변경 감지를 허용
                } else {
                    context.registerReceiver(networkReceiver, filter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (networkCallback != null) {
                try {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
//                LogUtil.d("[NetworkCallback] 네트워크 콜백 해제 완료");
                } catch (Exception e) {
                    LogUtil.e("[NetworkCallback] 콜백 해제 중 예외 발생 : " + e.getMessage());
                } finally {
                    networkCallback = null; // 해제 후 null 처리
                }
            }

        } else {
            if (networkReceiver != null) {
                try {
                    context.unregisterReceiver(networkReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // API 28 이상: NetworkCallback 사용
    private void registerNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                handleNetworkChange(true);
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                handleNetworkChange(false);
            }
        };

        // 네트워크 변경 감지를 요청
        try {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
//                LogUtil.d("[NetworkCallback] 네트워크 콜백 등록 완료");
        } catch (Exception e) {
            LogUtil.e("[NetworkCallback] 콜백 등록 중 예외 발생 : " + e.getMessage());
        }
    }

    // 네트워크 상태 변경 시 처리
    private void handleNetworkChange(boolean isConnected) {
        LogUtil.d("[NetworkCallback] isNetwork : " + isConnectedNetwork);

        // 네트워크가 끊겼다가 다시 붙는 경우 처음부터 다시 시도
        if (!isConnectedNetwork && isConnected) {
            LogUtil.d("[AppUpdateAgent]==> [NetworkCallback] Disconnect -> Connected Service Restart");
            // 최소 호출한 로직을 호출해도 된다
//            Intent intent = new Intent(액션명);
//            intent.putExtra("TYPE", "CHANGE_NETWORK_STATE");
//            intent.setComponent(new ComponentName(패키지명, 클래스명));
//            ContextCompat.startForegroundService(context, intent);

            // 재시도
            if (connectivityManager == null) {
                LogUtil.e("[NetworkCallback] ConnectivityManager is NULL!");
                return;
            }

            register();
        }

        LogUtil.i("[NetworkCallback] " + (isConnected ? "[CONNECTED]" : "[DISCONNECTED]"));
        isConnectedNetwork = isConnected;
    }

    BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                LogUtil.d("[NetworkReceiver]  action : " + intent.getAction());

                // 네트워크 체크
                if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    LogUtil.d("[NetworkReceiver] isNetwork : " + isConnectedNetwork);

                    // 네트워크가 끊겼다가 다시 붙는 경우 처음부터 다시 시도한다
                    if (!isConnectedNetwork) {
                        LogUtil.d("[AppUpdateAgent]==> [NetworkReceiver] Disconnect -> Connected Service Restart");
                        // 최소 호출한 로직을 호출해도 된다
//                        Intent intent = new Intent(액션명);
//                        intent.putExtra("TYPE", "CHANGE_NETWORK_STATE");
//                        intent.setComponent(new ComponentName(패키지명, 클래스명));
//                        ContextCompat.startForegroundService(context, intent);

                        // 재시도
                        if (connectivityManager == null) {
                            LogUtil.e("[NetworkCallback] ConnectivityManager is NULL!");
                            return;
                        }

                        register();
                    }

                    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

                    if (info != null) {
                        NetworkInfo.DetailedState detail = info.getDetailedState();
                        if (detail == NetworkInfo.DetailedState.CONNECTED) {
                            LogUtil.i("[NetworkReceiver] [CONNECTED]");
                            isConnectedNetwork = true;
                        } else if (detail == NetworkInfo.DetailedState.DISCONNECTED) {
                            LogUtil.i("[NetworkReceiver] [DISCONNECTED]");
                            isConnectedNetwork = false;
                        }
                    }
                }
            }
        }
    };
}
