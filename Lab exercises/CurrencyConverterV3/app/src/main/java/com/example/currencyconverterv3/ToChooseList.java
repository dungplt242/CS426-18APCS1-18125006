package com.example.currencyconverterv3;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

public class ToChooseList extends AppCompatActivity {
    static private ArrayList<CountryCurrency> list;
    private SelectionAdapter selectionAdapter; //final
    public Selection context;
    ListView listView;

    public ToChooseList(Selection context) {
        if (list == null) list = new ArrayList<>();
        listView = context.findViewById(R.id.selectionList);
        selectionAdapter = new SelectionAdapter(context, list);
        this.context = context;
    }

    public ToChooseList() {
        if (list == null) list = new ArrayList<>();
    }

    public void setView() {
        listView.setAdapter(selectionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CountryCurrency chosenCurrency = (CountryCurrency)adapterView.getItemAtPosition(i);
                CurrentList.addNewCurrency(chosenCurrency);
                list.remove(i);
                setResult(24);
                context.finish();
            }
        });
    }

    public static void addNewCurrency(CountryCurrency chosenCurrency) {
        list.add(chosenCurrency);
    }

    public static void loadList() {
        list.add(new CountryCurrency("PHP","",0.0));
        list.add(new CountryCurrency("DKK","",0.0));
        list.add(new CountryCurrency("HUF","",0.0));
        list.add(new CountryCurrency("CZK","",0.0));
        list.add(new CountryCurrency("AUD","",0.0));
        list.add(new CountryCurrency("SEK","",0.0));
        list.add(new CountryCurrency("IDR","",0.0));
        list.add(new CountryCurrency("INR","",0.0));
        list.add(new CountryCurrency("BRL","",0.0));
        list.add(new CountryCurrency("RUB","",0.0));
        list.add(new CountryCurrency("HRK","",0.0));
        list.add(new CountryCurrency("JPY","",0.0));
        list.add(new CountryCurrency("CNY","",0.0));
        list.add(new CountryCurrency("USD","",0.0));

    }

    public void updateRealTimeRate(JSONObject rates) throws JSONException {
        for (int i=0; i<list.size(); i++) {
            list.get(i).exch = rates.getDouble(list.get(i).Name);
        }
    }
    public void getBackupRate(Map<String, Double> rates) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).exch = rates.get(list.get(i).Name);
        }
    }

    public void exportRate(PrintStream output) {
        for (int i = 0; i < list.size(); i++) {
            output.println(list.get(i).Name);
            output.println(list.get(i).exch);
        }
    }

}
