package com.example.referencestudied.reference.autologin;

import android.accounts.Account;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.referencestudied.R;
import com.example.referencestudied.reference.autologin.KeyStore.KeyStorePreferences;
import com.example.referencestudied.reference.autologin.accountManager.AccountManagerHelper;
import com.example.referencestudied.reference.autologin.securePref.SecurePreferences;

public class AutoLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        // Intent에서 데이터 가져오기
        String buttonKey = getIntent().getStringExtra("BUTTON_KEY");
        if (buttonKey != null) {
            if (buttonKey.equalsIgnoreCase("AccountManager")) {
                Account account = AccountManagerHelper.getSavedAccount(this); // Fragment에서는 requireContext() 사용
                showFragment((account != null), buttonKey);

            } else if (buttonKey.equalsIgnoreCase("SharedPreferences")) {
                SecurePreferences securePreferences = new SecurePreferences(this);
                showFragment(securePreferences.isLoggedIn(), buttonKey);

            } else if (buttonKey.equalsIgnoreCase("KeyStore")) {
                KeyStorePreferences keyStorePreferences = new KeyStorePreferences(this);
                showFragment(keyStorePreferences.isLoggedIn(), buttonKey);
            }
        }
    }

    public void showFragment(boolean isLogin, String buttonKey) {
        Fragment fragment; // 공통 Fragment 타입 사용

        if (isLogin) { // 로그인 상태라면 로그아웃 화면 띄우기
            fragment = LogoutFragment.newInstance(buttonKey);
        } else { // 아니라면 로그인 화면 띄우기
            fragment = LoginFragment.newInstance(buttonKey);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null) // 뒤로 가기 버튼 누르면 돌아오기 가능
                .commit();
    }

}
