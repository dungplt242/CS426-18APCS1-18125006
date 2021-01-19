package com.example.currencyconverterv3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UserHistory extends AppCompatActivity {
    RecordList recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recordList = new RecordList(this);
        recordList.setView();
    }
}
