package com.example.referencestudied;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<ListButtonData> buttonDataList;

    public ListAdapter(Context context, List<ListButtonData> buttonDataList) {
        this.context = context;
        this.buttonDataList = buttonDataList;
    }

    @Override
    public int getCount() {
        return buttonDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return buttonDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.button_item, parent, false);
        }

        Button button = convertView.findViewById(R.id.btn);
        ListButtonData data = buttonDataList.get(position);
        button.setText(data.getText());

        // 버튼 클릭 리스너 설정
        button.setOnClickListener(v -> {
            try {
                data.getAction().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return convertView;
    }
}