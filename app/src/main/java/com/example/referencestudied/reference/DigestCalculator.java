package com.example.referencestudied.reference;


import android.os.Build;

import com.example.referencestudied.util.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

public class DigestCalculator {
    private static final String TAG = DigestCalculator.class.getName();

    /**
     * SHA256 해싱 후 Base64 인코딩하기 <br>
     * salt : 암호화 문자는 지정할 수 있음(코드상에는 날짜로 해둠) <br>
     * 암호화만 가능하면 복호화 불가능(키가 없기 때문에)
     */
    public static void sha256Hashing() {
        String password = "password";

        // Salt 생성 : 요청 시간으로 암호화 만들기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // YYYYMMDDHHmmss 형식 지정
        String salt = dateFormat.format(calendar.getTime()); // Calendar date를 지정된 형식으로 포맷
//        String salt = "20240930133119";
        LogUtil.d("salt : " + salt);

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
//            sha256.update((salt).getBytes(StandardCharsets.UTF_8)); // StandardCharsets.UTF_8 기본값이라 지정 안해도됨
//            byte[] hash = sha256.digest();
            byte[] hash = sha256.digest((password + salt).getBytes()); // 위에 2줄과 같은 내용
            LogUtil.d("digest : " + base64Encode(hash));

            // Base64 인코딩 반환
            base64Encode(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * N회차 SHA256 해싱 후 Base64 인코딩하기 <br>
     * Password = “(임의 문자값)” <br>
     * Salt = YYYYMMDDHHmmss <br>
     * 반복회차 : N = 100 - ss (100 -Date의 초) <br>
     * digest : BASE64(SHA256(SHA256(Password + salt)+salt)...N)
     */
    public static void generateDigest() {
        String password = "password";

        // Salt 생성 : 요청 시간으로 암호화 만들기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // YYYYMMDDHHmmss 형식 지정
        String salt = dateFormat.format(calendar.getTime()); // Calendar date를 지정된 형식으로 포맷
//        String salt = "20240930133119";
        LogUtil.d("salt : " + salt);

        // 뒤에 2자리 추출(seconds)
        String lastTwoSalt = salt.substring(salt.length() - 2);
        int seconds = Integer.parseInt(lastTwoSalt);

        int N = 100 - seconds; // N 계산
        LogUtil.d("generateDigest N : " + N);

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update((password + salt).getBytes());
            byte[] hash = sha256.digest();
            byte[] saltBytes = salt.getBytes();
//            LogUtil.d( "hash : " + base64Encode(hash));

            // N번 반복 해싱
            for (int i = 1; i <= N; i++) {
                // 해시와 솔트를 결합
                byte[] hashAndSalt = new byte[hash.length + saltBytes.length];
                System.arraycopy(hash, 0, hashAndSalt, 0, hash.length);
                System.arraycopy(saltBytes, 0, hashAndSalt, hash.length, saltBytes.length);

                hash = sha256.digest(hashAndSalt);
//                LogUtil.d( "[" + i + "] hash : " + base64Encode(hash));
            }
            LogUtil.d("digest : " + base64Encode(hash));

            // Base64 인코딩 반환
            base64Encode(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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

    /**
     * 번외 : Calendar date를 지정된 형식으로 포맷
     * 포맷 형식에 따라 출력되는 내용이 달라짐
     */
    public static String dateFormat(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(calendar.getTime());
    }
}
