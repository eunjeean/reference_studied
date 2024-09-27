package com.example.referencestudied;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.referencestudied.reference.ShellExecuteUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        List<ListButtonData> buttonDataList = new ArrayList<>();
        ListAdapter adapter = new ListAdapter(this, buttonDataList);
        listView.setAdapter(adapter);

        // 기능 소개 -------------------------------------------------------
        /* 터미널에서 작성하는 adb 기능을 코드로 구현 */
        buttonDataList.add(new ListButtonData("Shell Execute", () -> {
            // adb shell 이후 문장으로 요청하는 명령어를 입력
            String cmd = "sync"; // adb shell sync > 동기화
//            String cmd = "devices"; // 연결된 Device 목록 확인

//            String cmd = "pm clear --user 0 com.android.sample"; // 데이터 지우기 Clear App data : adb shell pm clear --user [user id] [package name]
//            String cmd = "pm clear --user 0 --cache-only com.android.sample"; // 캐시 지우기 Clear cache data : adb shell pm clear --user [user id] --cache-only [package name]

            // 앱 사용/사용중지 기능 동작시 권한문제가 발생할 수 있음 > 그러면 동작하지 않음
//            String cmd = "pm disable-user --user 0 --cache-only com.android.sample"; // 앱 비활성화 시키기 : adb shell pm disable-user --user 0 [package name]
//            String cmd = "pm enable --user 0 com.android.sample"; // 앱 다시 활성화 시키기 : adb shell pm enable --user 0 [package name]

            // 설치 : adb install –r –d -t APK명(경로)
            // 삭제 : adb shell pm uninstall 패키지명
//            String cmd = "pm uninstall --user 0 com.android.sample"; // 앱 삭제 시키기 : adb shell pm uninstall --user 0 [package name]

            ShellExecuteUtil.shellExecuteSync(cmd);
            return null; // Callable<Void>를 위한 null 반환
        }));

        /* 내외부 저장소의 메모리 용량 체크 방법 */
        buttonDataList.add(new ListButtonData("MemoryCheck", () -> {

            return null; // Callable<Void>를 위한 null 반환
        }));

        /* N회차 암호화하기 */
        buttonDataList.add(new ListButtonData("Digest Calculartor", () -> {

            return null; // Callable<Void>를 위한 null 반환
        }));

        /* 설명 작성 */
//        buttonDataList.add(new ListButtonData("버튼문구", () -> {
//            // 기능 추가
//            return null; // Callable<Void>를 위한 null 반환
//        }));
    }
}
