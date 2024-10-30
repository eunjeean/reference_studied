package com.example.referencestudied.reference;

import android.content.Context;

import com.example.referencestudied.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 현재 출력된 전체로그를 조회 > 특정키워드 필터링 후 저장
 * <p>
 * [로그 데이터를 읽기 위한 Manifest 권한] > 권한 주지 않으면 현재 package에서 출력된 내용만 저장됨
 * <uses-permission android:name="android.permission.READ_LOGS"/>
 * [외부 저장소 접근 권한]
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <p>
 * adb 호출방법(경로에 따라 권한이 필요함) -----------------------------------------------------
 * [실시간 로그 저장 > 계속 덮어쓰면서 백업됨(과부하의 원인)]
 * adb logcat > /(파일경로)/testLog.txt
 * // >없이 쓰려면 adb logcat -f /(파일경로)/testLog.txt
 * <p>
 * [현재 출력된 로그만 저장]
 * adb logcat -d > \(파일경로)\testLog.txt
 * // >없이 쓰려면 adb logcat -d  -f \(파일경로)\testLog.txt
 * <p>
 * [현재 출력된 로그의 특정 키워드 필터링하여 저장]
 * adb logcat -d | grep "keyword" > \(파일경로)\testLog.txt
 * adb logcat -d | find "keyword" > \(파일경로)\testLog.txt
 * adb logcat -d | findstr "keyword" > \(파일경로)\testLog.txt
 * // >없이 쓰려면
 * adb logcat -d | grep "keyword" -f \(파일경로)\testLog.txt
 * adb logcat -d | find "keyword" -f \(파일경로)\testLog.txt
 * adb logcat -d | findstr "keyword" -f \(파일경로)\testLog.txt
 * <p>
 * 기타 >>
 * [버퍼에 저장된 모든 로그를 지우는 명령어(이전 로그를 삭제해 새 로그만 기록되도록 설정)]
 * adb logcat -c > /(파일경로)/testLog.txt
 */
public class LogFilter {
    static int logsCnt = 0;
    static int keywordLogsCnt = 0;
    static String path = "";

    public LogFilter(Context context) {
        path = context.getFilesDir().getPath(); // "/data/user/0/(packageName)/files"
//        LogUtil.d("path : " + path);
    }

    /**
     * 각각 파일 저장하는 코드
     * 1) 현재 출력된 로그 저장 saveRealTimeLogs()
     * 2) 특정키워드를 필터링 저장 findKeyword()
     */
    public static void saveRealTimeLogs(String keyword) {
        LogUtil.i("saveRealTimeLogs >>>>>");

        try {
            File logDirectory = new File(path);

            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
                LogUtil.d("logDirectory 생성!");
            }
            setFilePermissions(logDirectory);

            // 덮어쓰지 않도록 이름 다르게 처리
            logsCnt++;
            String logFileName = "log_" + logsCnt + ".txt";
            File logFile = new File(logDirectory, logFileName);

            // logcat을 파일에 저장
            Process process = Runtime.getRuntime().exec("logcat -d -f " + logFile); // 지금까지 출력된 로그만을 저장
            LogUtil.d("logcat을 파일에 저장");

            int exitCode = process.waitFor(); // 프로세스가 종료될 때까지 기다림

            // 에러 메시지 출력
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorMsg = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorMsg.append(line).append("\n");
                }
                errorReader.close();
                LogUtil.e("sync command failed with exit code: " + exitCode);
                LogUtil.e("Error message: " + errorMsg.toString());
            } else {
                LogUtil.i("sync command executed successfully");

                // 성공하면 특정키워드 찾아서 저장
                findKeyword(keyword, logFile);
            }

        } catch (IOException e) {
//            e.printStackTrace();
            LogUtil.e("로그를 읽는 중 오류 발생 : " + e);
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.e("Exception : " + e);
        }
    }

    private static void findKeyword(String keyword, File logFile) {
        LogUtil.i("findKeyword >>>>> ");

        // 덮어쓰지 않도록 이름 다르게 처리
        String logFileName = "logFilter_" + logsCnt + ".txt";
        File outputFile = new File(path, logFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile.getAbsolutePath()));
             FileWriter fileWriter = new FileWriter(outputFile, true)) {
            int cnt = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyword)) {
                    cnt++;
//                    LogUtil.w("line.contains(keyword) : " + line);
                    fileWriter.write(line + System.lineSeparator()); // 검색 문자열이 포함된 라인 저장
                }
            }
            LogUtil.d(cnt + "개 확인 >> findKeyword DONE!");

            // 전체로그 저장한 파일 삭제
//            LogUtil.d("전체 내용 저장한 로그 파일 삭제");
//            deleteFile(logFile);

        } catch (IOException e) {
            //            e.printStackTrace();
            LogUtil.e("Error reading file : " + e);

            // 전체로그 저장한 파일 삭제
            LogUtil.e("실패하여 전체 내용 저장한 로그 파일 삭제합니다.");
            deleteFile(logFile);
        }
    }

    /**
     * 필터링된 파일만 저장!
     * 현재 출력된 로그 조회 > 특정키워드를 필터링 저장
     */
    public static void saveFindKeywordLogs(String keyword) {
        LogUtil.i("saveFindKeywordLogs >>>>>");
        try {
            File logDirectory = new File(path);

            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
                LogUtil.e("logDirectory 생성!");
            }
            setFilePermissions(logDirectory);

            // 덮어쓰지 않도록 이름 다르게 처리
            keywordLogsCnt++;
            String logFileName = "onlyFilterLog_" + keywordLogsCnt + ".txt";
            File logFile = new File(logDirectory, logFileName);

            // logcat 조회
            Process process = Runtime.getRuntime().exec("logcat -d");

            // 파일에 기록할 FileWriter 생성 (append 모드 사용)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 FileWriter writer = new FileWriter(logFile, true)) { // true로 append 모드 설정
                String line;
                int cnt = 0;

                // 로그에서 특정 키워드가 포함된 줄만 필터링하여 파일에 기록
                while ((line = reader.readLine()) != null) {
                    if (line.contains(keyword)) {
                        writer.write(line + System.lineSeparator()); // 각 줄 끝에 개행 문자 추가 = "\n"
                        cnt++;
                    }
                }
                LogUtil.d(cnt + "개 확인 >> findKeyword DONE!");
            }

            // 프로세스가 종료될 때까지 기다림
            int exitCode = process.waitFor();

            // 에러 메시지 출력
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorMsg = new StringBuilder();
                String errorMsgLine;
                while ((errorMsgLine = errorReader.readLine()) != null) {
                    errorMsg.append(errorMsgLine).append("\n");
                }
                errorReader.close();
                LogUtil.e("sync command failed with exit code: " + exitCode);
                LogUtil.e("Error message: " + errorMsg.toString());
            } else {
                LogUtil.i("sync command executed successfully");
            }

        } catch (IOException e) {
            //            e.printStackTrace();
            LogUtil.e("로그를 읽는 중 오류 발생 : " + e);
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.e("Exception : " + e);
        }
    }

    private static void setFilePermissions(File file) {
        // 파일에 읽기, 쓰기, 실행 권한 부여
        file.setWritable(true, false);
        file.setReadable(true, false);
        file.setExecutable(true, false);
    }

    private static void deleteFile(File file) {
        // 파일 삭제
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                LogUtil.i("File deleted successfully");
            } else {
                LogUtil.e("Failed to delete the file!!!");
            }
        } else {
            LogUtil.d("File does not exist!!!");
        }
    }
}

