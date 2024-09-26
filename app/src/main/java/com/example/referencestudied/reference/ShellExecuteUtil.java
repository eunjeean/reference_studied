package com.example.referencestudied.reference;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellExecuteUtil {

    private static final String TAG = ShellExecuteUtil.class.getName();

//    /**
//     * adb shell 명령어를 통해, 시스템 자원을 로그로 내보내는 간단한 코드
//     * 명령 수행시 App의 권한은 항상 User이기 때문에 su권한을 필요로 하는 명령은 수행할 수 없다는 점 유의 > manifest에 권한 추가하여 사용 가능한 부분도 있음
//     * <p>
//     * AndroidManifest 권한 필요! > 명령어마다 다름
//     * "android.permission.ACCESS_SUPERUSER"
//     * "android.permission.DEVICE_POWER"
//     * "android.permission.DUMP"
//     */
//    public static void shellExecute() {
//         Log.i(TAG, "shellExecute()");
//
//        try {
//            String cmd = "ps -ef"; // 명령어
//            try {
//                Process process = Runtime.getRuntime().exec(cmd);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                     Log.i(TAG, "Line : " + line);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * adb shell sync 호출 매서드 > 퍼미션 없이 적용 확인
     * 펌웨어에도 요청된 상태로, 회신결과에 따라 크로스체크 예정
     * sync(adb shell) 이외의 명령어는 불필요하나, sync와 관련된 테스트 사항도 아래코드처럼 로그로 확인이 되니, 기능정의가 FIX되고 개발완료시점에 TC작업시 활용하기
     */
    public static void shellExecuteSync() {
         Log.i(TAG, "shellExecuteSync()");

        String cmd = "sync"; // 명령어
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

            // TODO 삭제
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                 Log.i(TAG, "sync output : " + line);
//            }
//
//            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            while ((line = errorReader.readLine()) != null) {
//                 Log.i(TAG, "sync error : " + line);
//            }

            /* 'Process.waitFor();' 후 무한 대기 문제시 스크림 초기화 > 단, 리턴값 받을 수 없음 */
//            process.getErrorStream().close();
//            process.getInputStream().close();
//            process.getOutputStream().close();
//            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
