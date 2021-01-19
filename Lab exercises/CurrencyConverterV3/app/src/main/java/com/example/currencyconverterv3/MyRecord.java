package com.example.currencyconverterv3;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MyRecord {
    public String baseCurrency;
    public ArrayList<String> targetCountries;
    public ArrayList<String> targetCurrencies;
    public String time;

    public MyRecord(double baseCurrency, CurrentList targetList, LocalDateTime nowTime) {
        targetCurrencies = new ArrayList<>();
        targetCountries = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        this.baseCurrency = String.valueOf(Double.valueOf(df.format(baseCurrency)));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        time = nowTime.format(format);
        for (CountryCurrency target : targetList.list) {
            targetCountries.add(target.Name);
            targetCurrencies.add(target.curRes);
        }
    }

    public MyRecord(Scanner scan) {
        targetCurrencies = new ArrayList<>();
        targetCountries = new ArrayList<>();
        time = scan.nextLine();
        baseCurrency = scan.nextLine();
        int size = Integer.parseInt(scan.nextLine());
        for (int i=0; i<size; ++i) {
            targetCountries.add(scan.nextLine());
            targetCurrencies.add(scan.nextLine());
        }
    }



    public void export(PrintStream output) {
        output.println(time);
        output.println(baseCurrency);
        output.println(targetCountries.size());
        for (int i=0; i< targetCountries.size(); ++i) {
            output.println(targetCountries.get(i));
            output.println(targetCurrencies.get(i));
        }
    }

    public String allRecordsText() {
        String res = "EUR: ";
        res += baseCurrency + '\n';
        for (int i=0; i<targetCurrencies.size(); ++i) {
            res += targetCountries.get(i) + ": " + targetCurrencies.get(i);
            res += '\n';
        }
        return res;
    }
}
