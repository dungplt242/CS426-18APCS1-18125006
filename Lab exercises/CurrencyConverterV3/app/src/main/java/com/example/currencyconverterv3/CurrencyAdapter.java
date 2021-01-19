package com.example.currencyconverterv3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.currencyconverterv3.CountryCurrency;
import com.example.currencyconverterv3.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter <CountryCurrency> {
    public CurrencyAdapter(Context context, ArrayList<CountryCurrency> countryCurrencyArrayList) {
        super(context, 0, countryCurrencyArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("xin chao", " position");
        if (convertView == null) {
            convertView = createRow(position);
        }
        DisplayInfo(convertView, getItem(position));
        return convertView;
    }

    private View createRow(int position) {
        View itemView = createANewRow(position);
        return itemView;
    }

    private void DisplayInfo(View itemView, CountryCurrency countryCurrency) {
        TextView currencyName = (TextView)itemView.findViewById(R.id.currencyName);
        currencyName.setText(countryCurrency.Name);
        TextView longerCurrencyName = (TextView)itemView.findViewById(R.id.longerCurrencyName);
        longerCurrencyName.setText(countryCurrency.LongerName);
        TextView currency = (TextView)itemView.findViewById(R.id.targetCurrency);
        currency.setText(countryCurrency.curRes);
    }

    private View createANewRow(int position) {
        return LayoutInflater.from(this.getContext()).inflate(R.layout.item_layout, null);
    }
}
