package com.example.referencestudied;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.referencestudied.reference.ShellExecuteUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        List<ListButtonData> buttonDataList = new ArrayList<>();

        // 기능 소개 -------------------------------------------------------
        buttonDataList.add(new ListButtonData("ShellExecute", () -> {
            ShellExecuteUtil.shellExecuteSync();
            return null; // Callable<Void>를 위한 null 반환
        }));

        // -------------------------------------------------------

        // 어댑터 설정
        ListAdapter adapter = new ListAdapter(this, buttonDataList);
        listView.setAdapter(adapter);
    }
}
