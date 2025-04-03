package com.example.referencestudied.reference.autologin.securePref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePreferences {
    private static final String PREF_NAME = "secure_prefs";
    private static SharedPreferences prefs;

    /* 경로 : /data/data/<패키지명>/shared_prefs/<PREF_NAME>.xml */
    public SecurePreferences(Context context) {
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

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    // 로그인 정보 저장
    public void saveLoginInfo(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password); // 실제 사용 시 패스워드는 암호화 권장
        editor.apply();
    }

    // 로그인 정보 가져오기
    public String getUsername() {
        try {
            return prefs.getString("username", "");
        } catch (Exception e) {
            return "";
        }
    }

    public String getPassword() {
        try {
            return prefs.getString("password", "");
        } catch (Exception e) {
            return "";
        }
    }

    // 자동 로그인 여부 체크
    public boolean isLoggedIn() {
        return !getUsername().isEmpty() && !getPassword().isEmpty() && getUsername() != null && getPassword() != null;
    }

    // 로그아웃 (데이터 삭제)
    public void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

}

