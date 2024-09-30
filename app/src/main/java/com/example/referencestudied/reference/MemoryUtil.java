package com.example.referencestudied.reference;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * 다운로드 전 저장공간 확보
 * 여유공간 체크 로직 : (app Size * 2) + 50MB > 여유공간
 * 여유공간 사이즈 한도 : 2G
 */
public class MemoryUtil {
    private static final String TAG = MemoryUtil.class.getName();

    /* 내부 저장소의 전체 용량을 반환
     * 안드로이드 시스템의 데이터 디렉토리를 반환
     * 시스템 데이터와 앱 데이터를 저장하는 데 사용
     * */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory(); // 내부 저장소의 루트 디렉토리를 반환
        StatFs stat = new StatFs(path.getPath()); // StatFs 객체를 사용하여 파일 시스템의 상태
        long blockSize = stat.getBlockSizeLong(); // 파일 시스템의 블록 크기
        long totalBlocks = stat.getBlockCountLong(); // 전체 블록 수
        return totalBlocks * blockSize; // 전체 블록 수와 블록 크기를 곱하여 내부 저장소의 전체 용량을 바이트 단위로 반환
    }

    // 내부 저장소의 사용 가능한 용량
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory(); // 내부 저장소의 루트 디렉토리를 반환
        StatFs stat = new StatFs(path.getPath()); // StatFs 객체를 사용하여 파일 시스템의 상태
        long blockSize = stat.getBlockSizeLong(); // 파일 시스템의 블록 크기
        long availableBlocks = stat.getAvailableBlocksLong(); // 사용 가능한 블록 수
        return availableBlocks * blockSize; // 사용 가능한 블록 수와 블록 크기를 곱하여 내부 저장소의 사용 가능한 용량을 바이트 단위로 반환
    }

    /**
     * 내부 공유 저장소의 디렉토리
     * 외부 저장소(미디어 공유 저장소)의 루트 디렉토리를 반환
     * 사용자가 접근하여 파일을 저장하고 공유하는 용도 - 컴퓨터에 마운트(장치 연결)되어야 확인 가능
     */
    // 외부 저장소의 전체 용량
    public static long getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) { // 외부 저장소가 사용 가능한지 확인
            File path = Environment.getExternalStorageDirectory(); // 외부 저장소의 루트 디렉토리를 반환
            StatFs stat = new StatFs(path.getPath()); // 객체를 사용하여 파일 시스템의 상태
            long blockSize = stat.getBlockSizeLong(); // 파일 시스템의 블록 크기
            long totalBlocks = stat.getBlockCountLong(); // 전체 블록 수
            return totalBlocks * blockSize; // 전체 블록 수와 블록 크기를 곱하여 외부 저장소의 전체 용량을 bytes 단위로 반환
        } else {
            return -1;
        }
    }

    // 외부 저장소의 사용 가능한 용량
    public static long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) { // 외부 저장소가 사용 가능한지 확인
            File path = Environment.getExternalStorageDirectory(); // 외부 저장소의 루트 디렉토리를 반환
            StatFs stat = new StatFs(path.getPath()); // 객체를 사용하여 파일 시스템의 상태
            long blockSize = stat.getBlockSizeLong();// 파일 시스템의 블록 크기
            long availableBlocks = stat.getAvailableBlocksLong(); // 사용 가능한 블록 수
            return availableBlocks * blockSize; // 사용 가능한 블록 수와 블록 크기를 곱하여 외부 저장소의 사용 가능한 용량을 bytes 단위로 반환
        } else {
            return -1;
        }
    }

    //  외부 저장소가 사용 가능한지 여부
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 외부 저장소가 마운트된 상태인지 확인
    }

    /**
     * bytes 단위 변환 >> KB
     */
    public static double bytesToKB(long bytes) {
        double unit = (double) bytes / 1024;
//        return Math.round(unit * 100.0) / 100.0; // %.2f
        return unit;
    }

    /**
     * bytes 단위 변환 >> MB
     */
    public static double bytesToMB(long bytes) {
        double unit = (double) bytes / (1024 * 1024);
//        return Math.round(unit * 100.0) / 100.0; // %.2f
        return unit;
    }

    /**
     * bytes 단위 변환 >> GB
     */
    public static double bytesToGB(long bytes) {
        double unit = (double) bytes / (1024 * 1024 * 1024);
//        return Math.round(unit * 100.0) / 100.0; // %.2f
        return unit;
    }

    /**
     * 원하는 단위를 붙여서 한글로 변환
     */
    public static String unitString(double size, String unit) {
        return String.format("%.2f ", size) + unit;
    }

    /**
     * File List 용량 조회
     */
    public static long getTotalApkListSize(ArrayList<String> apkList) {
        long totalSize = 0;
        for (String apkFilePath : apkList) { // apkFilePath = 저장 경로
            File apkFile = new File(apkFilePath);
            // file 한개 용량
            Log.d(TAG, "apkFile : " + unitString(bytesToMB(apkFile.length()), "MB"));

            if (apkFile.isFile() && apkFile.getName().toLowerCase().endsWith(".apk")) {
                totalSize += apkFile.length();
            }
        }
        return totalSize; // bytes
    }

    /**
     * apkFile 단일 용량 조회
     */
    public static long getTotalApkListSize(String apkFilePath) {
        long apkFileSize = 0;
        File apkFile = new File(apkFilePath);
        // file 한개 용량
        Log.d(TAG, "apkFile : " + unitString(bytesToMB(apkFile.length()), "MB"));

        if (apkFile.isFile() && apkFile.getName().toLowerCase().endsWith(".apk")) {
            apkFileSize = apkFile.length();
        }
        return apkFileSize; // bytes
    }

    /**
     * 여유공간 체크 로직
     */
    public static Boolean getTotalApkListSize(long fileSizes) {
        Boolean result = false;
//        Log.d(TAG, "standbyList fileSizes : " + fileSizes + " bytes");

        // 여유공간
        long availableMemory = MemoryUtil.getAvailableExternalMemorySize(); // byte

        // 여유공간 사이즈 한도 : 2G
        Boolean is2GBLimit = bytesToGB(availableMemory) > 2;
        Log.d(TAG, "is2GBLimit : " + is2GBLimit);
//        Log.d(TAG, "availableMemory : " + unitString(bytesToGB(availableMemory), "GB"));

        // 여유공간 체크 로직 : (fileSizes * 2) + 50MB < 여유공간
        Boolean isLimit = (bytesToMB((fileSizes * 2)) + 50) < bytesToMB(availableMemory);
        Log.d(TAG, "isLimit : " + isLimit);
//        Log.d(TAG, "(fileSizes * 2) + 50MB : " + unitString((bytesToMB((fileSizes * 2)) + 50), "MB"));
//        Log.d(TAG, "availableMemory : " + unitString(bytesToMB(availableMemory), "MB"));

        if (is2GBLimit && isLimit) {
            result = true;
        }

        return result;
    }

}

