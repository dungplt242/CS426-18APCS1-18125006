package com.example.currencyconverterv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SelectionAdapter extends ArrayAdapter<CountryCurrency> {
    public SelectionAdapter(Context context, ArrayList<CountryCurrency> countryCurrencyArrayList) {
        super(context, 0, countryCurrencyArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createRow(position);
        }
        DisplayInfo(convertView, getItem(position));
        return convertView;
    }

    private void DisplayInfo(View itemView, CountryCurrency countryCurrency) {
        TextView currencyName = (TextView)itemView.findViewById(R.id.currencyName);
        currencyName.setText(countryCurrency.Name);
        TextView longerCurrencyName = (TextView)itemView.findViewById(R.id.longerCurrencyName);
        longerCurrencyName.setText(countryCurrency.LongerName);
    }

    private View createRow(int position) {
        View itemView = createANewRow(position);
        return itemView;
    }

    private View createANewRow(int position) {
        return LayoutInflater.from(this.getContext()).inflate(R.layout.select_item_layout, null);
    }
}
