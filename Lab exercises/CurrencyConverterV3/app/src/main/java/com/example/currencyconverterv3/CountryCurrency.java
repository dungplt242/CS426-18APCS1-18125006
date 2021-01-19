package com.example.currencyconverterv3;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class CountryCurrency extends AppCompatActivity {
    public String Name;
    public String LongerName;
    public String curRes = "0.0";
    public double exch;

    public CountryCurrency(String name, String longerName, double exchange) {
        Name = name;
        LongerName = longerName;
        exch = exchange;
    }

    public void updateCurrent(double curResult) {
        double res;
        if (exch != 0)
            res = curResult / exch;
        else
            res = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        curRes = String.valueOf(Double.valueOf(df.format(res)));
    }
}
