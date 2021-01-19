package com.example.currencyconverterv3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Selection extends AppCompatActivity {
    ToChooseList toChooseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        toChooseList = new ToChooseList(this);
        toChooseList.setView();
    }
}