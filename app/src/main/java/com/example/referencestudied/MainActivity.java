package com.example.referencestudied;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.referencestudied.reference.DigestCalculator;
import com.example.referencestudied.reference.LogFilter;
import com.example.referencestudied.reference.MemoryUtil;
import com.example.referencestudied.reference.ShellExecuteUtil;
import com.example.referencestudied.util.LogUtil;

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
        buttonDataList.add(new ListButtonData("Memory Size", () -> {
            // 내부 저장소 -------------------------------------------------------------
            long test1 = MemoryUtil.getTotalInternalMemorySize(); // 내부 저장소의 전체 용량을 반환
            long test2 = MemoryUtil.getAvailableInternalMemorySize(); // 내부 저장소의 사용 가능한 용량
            LogUtil.d("내부 저장소의 전체 용량 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test1), "MB"));
            LogUtil.d("내부 저장소의 사용 가능한 용량 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test2), "MB"));

            // 외부 저장소 -------------------------------------------------------------
            long test3 = MemoryUtil.getTotalExternalMemorySize(); // 외부 저장소의 전체 용량
            long test4 = MemoryUtil.getAvailableExternalMemorySize(); // 외부 저장소의 사용 가능한 용량
            LogUtil.d("외부 저장소의 전체 용량 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test3), "MB"));
            LogUtil.d("외부 저장소의 사용 가능한 용량 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test4), "MB"));

            // apk File List 용량 조회 -------------------------------------------------------------
//            ArrayList<String> apkList = new ArrayList<>();
//            long test5 = MemoryUtil.getTotalApkListSize(apkList);
//            LogUtil.d("apk File List 용량 조회 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test5), "MB"));
//
//            // apkFile 단일 용량 조회 -------------------------------------------------------------
//            String apkFilePath = "";
//            long test6 = MemoryUtil.getTotalApkListSize(apkFilePath);
//            LogUtil.d("apkFile 단일 용량 조회 : " + MemoryUtil.unitString(MemoryUtil.bytesToMB(test6), "MB"));
//
//            // 여유공간 체크 로직 -------------------------------------------------------------
//            long fileSizes = 0L;
//            Boolean test7 = MemoryUtil.getTotalApkListSize(fileSizes);
//            LogUtil.d("여유공간 체크 로직 : " + test7);

            return null; // Callable<Void>를 위한 null 반환
        }));

        /* 해시 암호화하기 */
        buttonDataList.add(new ListButtonData("Digest Calculator", () -> {
            DigestCalculator.sha256Hashing(); // 1회 해시
            return null; // Callable<Void>를 위한 null 반환
        }));

        /* N회차 해시 암호화하기 */
        buttonDataList.add(new ListButtonData("Digest Calculator N", () -> {
            DigestCalculator.generateDigest(); // N회 반복 해시
            return null; // Callable<Void>를 위한 null 반환
        }));

        /* Calendar date를 지정된 형식으로 포맷 */
        buttonDataList.add(new ListButtonData("Date Format", () -> {
            String date1 = "yyyy/MM/dd HH:mm:ss z"; // 2024/09/24 16:14:15 GMT+09:00
            String date2 = "yyyy/MM/dd HH:mm:ss Z"; // 2024/09/24 16:14:15 +0900
            String date3 = "yyyy/MM/dd HH:mm:ss 'Asia/Seoul'"; // 2024/09/24 16:14:15 Asia/Seoul
            String date4 = "yyyy/MM/dd HH:mm:ss 'KST'"; // 2024/09/24 16:14:15 KST
            LogUtil.d("digest : " + DigestCalculator.dateFormat(date1));
            LogUtil.d("digest : " + DigestCalculator.dateFormat(date2));
            LogUtil.d("digest : " + DigestCalculator.dateFormat(date3));
            LogUtil.d("digest : " + DigestCalculator.dateFormat(date4));
            return null; // Callable<Void>를 위한 null 반환
        }));

        /* 현재 출력된 로그 조회 > 특정키워드 필터링 로그 저장 */
        buttonDataList.add(new ListButtonData("Log Filter", () -> {
            String keyword = "FluteResponseService";
            LogFilter logFilter = new LogFilter(this);
//            logFilter.saveRealTimeLogs(keyword); // 각각 파일 저장
            logFilter.saveFindKeywordLogs(keyword); // 필터링된 파일만 저장
            return null; // Callable<Void>를 위한 null 반환
        }));

        /* 설명 작성 */
//        buttonDataList.add(new ListButtonData("버튼문구", () -> {
//            // 기능 추가
//            return null; // Callable<Void>를 위한 null 반환
//        }));
    }
}
