package com.example.currencyconverterv3;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

public class CurrentList extends AppCompatActivity {
    static public ArrayList<CountryCurrency> list;
    static public Context context;
    ListView listView;
    final CurrencyAdapter currencyAdapter;

    public CurrentList(MainActivity context) {
        if (list == null) list = new ArrayList<>();
        listView = (ListView)context.findViewById(R.id.currenciesList);
        currencyAdapter = new CurrencyAdapter(context, list);
        this.context = context;
    }

    public static void addNewCurrency(CountryCurrency chosenCurrency) {
        list.add(chosenCurrency);
    }


    public void setView() {
        listView.setAdapter(currencyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CountryCurrency chosenCurrency = (CountryCurrency)adapterView.getItemAtPosition(i);
                ToChooseList.addNewCurrency(chosenCurrency);
                list.remove(i);
                currencyAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateCurrency(double curResult, final MainActivity mainActivity){
        for (CountryCurrency countryCurrency : list) {
            countryCurrency.updateCurrent(curResult);
            currencyAdapter.notifyDataSetChanged();
        }
        mainActivity.storeANewRecord();
    }

    public int size() {
        return list.size();
    }

    public void loadList() {
        list.add(new CountryCurrency("CAD", "", 0.0));
        list.add(new CountryCurrency("HKD","",0.0));
        list.add(new CountryCurrency("ISK","",0.0));

    }

    public void setListView(MainActivity context) {
        listView = context.findViewById(R.id.currenciesList);
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
