package com.example.referencestudied.reference.autologin.KeyStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class KeyStorePreferences {
    private static final String PREF_NAME = "keystore_prefs";
    private static SharedPreferences prefs;
    private static final String KEY_ALIAS = "keystorePrefKey";  // KeyStore에서 사용할 키 별칭
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

    /* 경로 : /data/data/<패키지명>/shared_prefs/<PREF_NAME>.xml */
    public KeyStorePreferences(Context context) {
        try {
            // 암호화 키 생성
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // EncryptedSharedPreferences 초기화
            prefs = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // KeyStore 키 생성 (최초 실행 시)
            generateSecretKey();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Log.e("KeyStorePreferences", "error : " + e);
        }
    }

    // 로그인 정보 저장
    public void saveLoginInfo(String username, String password) {
        try {
            Log.w("KeyStorePreferences", "ID : " + username + " | password : " + password);
            String encryptedPassword = encrypt(password);
            String encryptedUsername = encrypt(username);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", encryptedUsername); // username 암호화 후 저장
            editor.putString("password", encryptedPassword); // 암호화된 패스워드 저장
            editor.putBoolean("isLogin", true);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        try {
            return decrypt(prefs.getString("username", ""));
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }

    public String getPassword() {
        try {
            return decrypt(prefs.getString("password", ""));
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }

    public boolean getIsLogin() {
        return prefs.getBoolean("isLogin", false);
    }

    // 자동 로그인 여부 체크
    public boolean isLoggedIn() {
        return getUsername() != null && !getUsername().isEmpty() && getPassword() != null && !getPassword().isEmpty() && getIsLogin();
    }

    // 로그아웃 (데이터 삭제)
    public void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 🔹 AES 키를 KeyStore에 저장
     **/
    private void generateSecretKey() throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build()
            );
            keyGenerator.generateKey();
        }
    }

    /**
     * 🔹 AES 암호화 메소드
     **/
    private String encrypt(String data) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // IV + 암호문을 Base64로 인코딩하여 저장
        return Base64.encodeToString(iv, Base64.DEFAULT) + ":" + Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 🔹 AES 복호화 메소드
     **/
    private static String decrypt(String encryptedData) throws GeneralSecurityException, IOException {
        if (encryptedData == null) return null;

        String[] parts = encryptedData.split(":");
        if (parts.length != 2) return null;  // 유효하지 않은 데이터

        byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT);

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        SecretKey secretKey = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] decryptedData = cipher.doFinal(encryptedBytes);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}

