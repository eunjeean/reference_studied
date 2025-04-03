package com.example.referencestudied.reference.autologin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.referencestudied.R;
import com.example.referencestudied.reference.autologin.KeyStore.KeyStorePreferences;
import com.example.referencestudied.reference.autologin.accountManager.AccountManagerHelper;
import com.example.referencestudied.reference.autologin.securePref.SecurePreferences;
import com.example.referencestudied.util.LogUtil;

public class LoginFragment extends Fragment {
    String buttonKey = "";

    // Fragment 생성 시 안전하게 데이터를 전달하는 newInstance() 메서드
    public static LoginFragment newInstance(String buttonKey) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("BUTTON_KEY", buttonKey);
        fragment.setArguments(args); // 여기서만 setArguments 사용!
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // 선언
        EditText userId = view.findViewById(R.id.user_id);
        EditText userPw = view.findViewById(R.id.user_pw);
        Button login_btn = view.findViewById(R.id.login_btn);
        // requireArguments()로 안전하게 값 가져오기
        buttonKey = requireArguments().getString("BUTTON_KEY", "");

        login_btn.setOnClickListener(v -> {
            // 버튼을 누르는 순간 작성된 문자를 가져오기
            String userIdText = userId.getText().toString();
            String userPwText = userPw.getText().toString();

            if (!userIdText.isEmpty() && !userPwText.isEmpty()) {
                switch (buttonKey) {
                    case "AccountManager":
                        AccountManagerHelper.addAccount(getContext(), userIdText, userPwText);
                        break;

                    case "SharedPreferences":
                        SecurePreferences securePreferences = new SecurePreferences(getContext());
                        securePreferences.saveLoginInfo(userIdText, userPwText);
                        break;

                    case "KeyStore":
                        KeyStorePreferences keyStorePreferences = new KeyStorePreferences(getContext());
                        keyStorePreferences.saveLoginInfo(userIdText, userPwText);
                        break;

                    default:
                        LogUtil.e("buttonKey case notFind! " + buttonKey);
                }
                Toast.makeText(getContext(), "계정 저장 완료!", Toast.LENGTH_SHORT).show();

                // 로그아웃 페이지로 이동
                LogoutFragmentChange();

            } else {
                Toast.makeText(getContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void LogoutFragmentChange() {
        LogUtil.i("LogoutFragmentChange >>");
        // Fragment 변경
//        requireActivity().getSupportFragmentManager().beginTransaction()
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, LogoutFragment.newInstance(buttonKey)) // fragmentContainer는 Activity의 FrameLayout ID
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