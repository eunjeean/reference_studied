package com.example.referencestudied.reference;


import android.os.Build;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

/**
 * API 보안인증강화를 위한 Digest 생성
 * <p>
 * date: YYYY/MM/DD HH:mm:ssTZ
 * Password = “{said}:{stb_mac}”
 * Salt = YYYYMMDDHHmmss
 * N = 100 -ss (100 -Date의 초)
 * Digest: BASE64(SHA256(SHA256(Password + salt)+salt)...N)
 * 해당결과값은 API 호출시 header에 said/mac/digest실행된 date/digest를 추가하여 전송한다.
 */

public class DigestCalculator {
    private static final String TAG = DigestCalculator.class.getName();
    
    /**
     * Digest 생성
     * BASE64(SHA256(SHA256(Password + salt)+salt)...N)
     */
    public static String generateDigest(Date time) {
        // Password 생성
        String saId = "SettingManager.getInstance().getSaId()";
        String stbMac = "SettingManager.getInstance().getMacAdd()";
        String password = saId + ":" + stbMac;

        // Salt 생성
        String salt = dateFormat(time, "yyyyMMddHHmmss"); // YYYYMMDDHHmmss 형식으로 포맷
        Log.d(TAG, "salt : " + salt);

        // 뒤에 2자리 추출(seconds)
        String lastTwoSalt = salt.substring(salt.length() - 2);
        int seconds = Integer.parseInt(lastTwoSalt);

        int N = 100 - seconds; // N 계산
        Log.d(TAG, "generateDigest N : " + N);

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update((password + salt).getBytes());
            byte[] hash = sha256.digest();
            byte[] saltBytes = salt.getBytes();

            // N번 반복 해싱
            for (int i = 1; i <= N; i++) {
                // 해시와 솔트를 결합
                byte[] hashAndSalt = new byte[hash.length + saltBytes.length];
                System.arraycopy(hash, 0, hashAndSalt, 0, hash.length);
                System.arraycopy(saltBytes, 0, hashAndSalt, hash.length, saltBytes.length);

                hash = sha256.digest(hashAndSalt);
//                Log.d(TAG, "[" + i + "] hash : " + base64Encode(hash));
            }
            Log.d(TAG, "digest : " + base64Encode(hash));

            // Base64 인코딩 반환
            return base64Encode(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String base64Encode(byte[] hash) {
        // Base64 인코딩 반환
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(hash);
        } else {
            return android.util.Base64.encodeToString(hash, android.util.Base64.NO_WRAP);
        }
    }

    /** Calendar date를 지정된 형식으로 포맷 */
    public static String dateFormat(Date time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(time);
    }
}
