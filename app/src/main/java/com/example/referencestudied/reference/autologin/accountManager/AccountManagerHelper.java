package com.example.referencestudied.reference.autologin.accountManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * 계정 추가 기능 구현 - 계정 추가 및 불러오기
 *
 * xml에 사용한 Account 계정파일 필요
 * authenticator.xml
 * authenticator_prefs.xml
 * manifest에도 Service등록 필요
 */
public class AccountManagerHelper {
    private static final String TAG = "AccountManagerHelper";
    private static final String ACCOUNT_TYPE = "com.example.referencestudied.account";

    public static void addAccount(Context context, String username, String password) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account(username, ACCOUNT_TYPE);

        if (accountManager.addAccountExplicitly(account, password, null)) {
            Log.d(TAG, "계정이 추가되었습니다 : " + username);
        } else {
            Log.e(TAG, "계정 추가 실패");
        }
    }

    public static Account getSavedAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

        return accounts.length > 0 ? accounts[0] : null;
    }

    public static String getAccountPassword(Context context, Account account) {
        AccountManager accountManager = AccountManager.get(context);
        return accountManager.getPassword(account);
    }

    public static void deleteAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE); // 계정 타입 지정

        if (accounts.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isSuccess = accountManager.removeAccountExplicitly(accounts[0]);
                if (isSuccess) {
                    Log.d("AccountManager", "계정 삭제 성공");
                } else {
                    Log.e("AccountManager", "계정 삭제 실패");
                }
            } else {
                accountManager.removeAccount(accounts[0], null, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle result = future.getResult();
                            boolean isSuccess = result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                            if (isSuccess) {
                                Log.d("AccountManager", "계정 삭제 성공");
                            } else {
                                Log.e("AccountManager", "계정 삭제 실패");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        }
    }
}
