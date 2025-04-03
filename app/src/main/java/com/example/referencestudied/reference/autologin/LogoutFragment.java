package com.example.referencestudied.reference.autologin;

import android.accounts.Account;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.referencestudied.R;
import com.example.referencestudied.reference.autologin.KeyStore.KeyStorePreferences;
import com.example.referencestudied.reference.autologin.accountManager.AccountManagerHelper;
import com.example.referencestudied.reference.autologin.securePref.SecurePreferences;
import com.example.referencestudied.util.LogUtil;

public class LogoutFragment extends Fragment {
    String buttonKey = "";

    // Fragment 생성 시 안전하게 데이터를 전달하는 newInstance() 메서드
    public static LogoutFragment newInstance(String buttonKey) {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        args.putString("BUTTON_KEY", buttonKey);
        fragment.setArguments(args); // 여기서만 setArguments 사용!
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        // 선언
        Button logoutBtn = view.findViewById(R.id.logout_btn);
        TextView checkUser = view.findViewById(R.id.check_user);

        // requireArguments()로 안전하게 값 가져오기
        buttonKey = requireArguments().getString("BUTTON_KEY", "");

        switch (buttonKey) {
            case "AccountManager":
                Account account = AccountManagerHelper.getSavedAccount(requireContext());
                if (account != null) {
                    checkUser.setText("자동 로그인 계정 : " + account.name);
                } else {
                    checkUser.setText("저장된 계정 없음. 로그인 필요.");
                }
                logoutBtn.setOnClickListener(v -> {
                    AccountManagerHelper.deleteAccount(getContext()); // 로그아웃
                    LoginFragmentChange(); // 로그인페이지로 이동
                });

                break;

            case "SharedPreferences":
                SecurePreferences securePreferences = new SecurePreferences(requireContext());
                if (securePreferences.isLoggedIn()) {
                    checkUser.setText("자동 로그인 계정 : " + securePreferences.getUsername());
                } else {
                    checkUser.setText("저장된 계정 없음. 로그인 필요.");
                }

                logoutBtn.setOnClickListener(v -> {
                    securePreferences.logout(); // 로그아웃
                    LoginFragmentChange(); // 로그인페이지로 이동
                });

                break;

            case "KeyStore":
                KeyStorePreferences keyStorePreferences = new KeyStorePreferences(requireContext());
                if (keyStorePreferences.isLoggedIn()) {
                    checkUser.setText("자동 로그인 계정 : " + keyStorePreferences.getUsername());
                } else {
                    checkUser.setText("저장된 계정 없음. 로그인 필요.");
                }

                logoutBtn.setOnClickListener(v -> {
                    keyStorePreferences.logout(); // 로그아웃
                    LoginFragmentChange(); // 로그인페이지로 이동
                });

                break;

            default:
                LogUtil.e("buttonKey case notFind! " + buttonKey);
        }
        return view;
    }

    private void LoginFragmentChange() {
        LogUtil.i("LoginFragmentChange >>");
        // Fragment 변경
//        requireActivity().getSupportFragmentManager().beginTransaction()
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment.newInstance(buttonKey)) // fragmentContainer는 Activity의 FrameLayout ID
//                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기 위해 추가
                .commit();

        // Activity에서 처리
//        Intent intent = new Intent(getContext(), AutoLoginActivity.class);
//        intent.putExtra("BUTTON_KEY", buttonKey); // 버튼 정보 전달
//        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 뒤로가는 경우,fragment가 Resume되는데 Activity도 종료되도록 처리
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish(); // 현재 Activity 종료
            }
        });
    }

}
