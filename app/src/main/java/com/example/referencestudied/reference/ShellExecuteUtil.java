package com.example.referencestudied.reference;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * adb shell 명령어를 통해, 시스템 자원을 로그로 내보내는 간단한 코드 <br>
 * 명령 수행시 App의 권한은 항상 User이기 때문에 su권한을 필요로 하는 명령은 수행할 수 없다는 점 유의 > manifest에 권한 추가하여 사용 가능한 부분도 있음
 * <p>
 * AndroidManifest 권한 필요! > 명령어마다 다름 <br>
 * "android.permission.ACCESS_SUPERUSER" <br>
 * "android.permission.DEVICE_POWER" <br>
 * "android.permission.DUMP"
 * <p>
 * <p>
 * 대부분 SecurityException: Permission Denial 생기는데 환경에 따라 적용할 수 있을듯
 */
public class ShellExecuteUtil {
    private static final String TAG = ShellExecuteUtil.class.getName();

    /**
     * adb shell sync 호출 매서드 > 퍼미션 없이 적용 확인 <br>
     * sync(adb shell) 이외의 명령어는 불필요하나, sync와 관련된 테스트 사항도 아래코드처럼 로그로 확인
     *
     * @param cmd adb shell 이후 문장으로 요청하는 명령어를 입력
     */
    public static void shellExecuteSync(String cmd) {
        Log.i(TAG, "shellExecuteSync()");

        // adb shell 이후에 요청하는 명령어를 입력!
//        String cmd = "sync"; // 명령어
//        String cmd = "devices"; // 연결된 Device 목록 확인
//
//        String cmd = "pm clear --user 0 com.android.sample"; // 데이터 지우기 Clear App data : adb shell pm clear --user [user id] [package name]
//        String cmd = "pm clear --user 0 --cache-only com.android.sample"; // 캐시 지우기 Clear cache data : adb shell pm clear --user [user id] --cache-only [package name]
//
//        앱 사용/사용중지 기능 동작시 권한문제가 발생할 수 있음 > 그러면 동작하지 않음
//        String cmd = "pm disable-user --user 0 --cache-only com.android.sample"; // 앱 비활성화 시키기 : adb shell pm disable-user --user 0 [package name]
//        String cmd = "pm enable --user 0 com.android.sample"; // 앱 다시 활성화 시키기 : adb shell pm enable --user 0 [package name]
//
//        설치:
//        adb install –r –d - t APK명(경로)
//        삭제:
//        adb shell pm uninstall 패키지명
//        String cmd = "pm uninstall --user 0 com.android.sample"; // 앱 삭제 시키기 : adb shell pm uninstall --user 0 [package name]

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            Log.i(TAG, "process : " + process);
            int exitCode = process.waitFor(); // 프로세스가 종료될 때까지 기다림

            if (exitCode == 0) {
                Log.i(TAG, "sync command executed successfully");
            } else {
                Log.i(TAG, "sync command failed with exit code : " + exitCode);
            }

            // try-with-resources 구문으로 감싸서 BufferedReader 리소스를 자동으로 닫도록 함
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, "Sync output: " + line);
                }

                while ((line = errorReader.readLine()) != null) {
                    Log.i(TAG, "Sync error: " + line);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e(TAG, "error!!! : " + e);
        }
    }

}
