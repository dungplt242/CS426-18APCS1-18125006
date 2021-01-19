package com.example.graphic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onDrawShapeClicked(View view) {
        MyPaintView paintView = (MyPaintView)findViewById(R.id.paintView);
        switch (view.getId())
        {
            case R.id.buttonLine:
                paintView.selectShape(0);
                break;
            case R.id.buttonRectangle:
                paintView.selectShape(1);
                break;
            case R.id.buttonEllipse:
                paintView.selectShape(2);
                break;
        }
    }

}