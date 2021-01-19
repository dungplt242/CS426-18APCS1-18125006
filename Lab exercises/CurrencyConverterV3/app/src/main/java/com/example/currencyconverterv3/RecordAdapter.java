package com.example.currencyconverterv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecordAdapter extends ArrayAdapter<MyRecord> {
    public RecordAdapter(Context context, ArrayList<MyRecord> records) {
        super(context, 0, records);
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

    private void DisplayInfo(View itemView, MyRecord myRecord) {
        TextView timeView = itemView.findViewById(R.id.dateTime);
        timeView.setText(myRecord.time);
        TextView recordView = itemView.findViewById(R.id.convertRecord);
        recordView.setText(myRecord.allRecordsText());
    }

    private View createRow(int position) {
        View itemView = createANewRow(position);
        return itemView;
    }

    private View createANewRow(int position) {
        return LayoutInflater.from(this.getContext()).inflate(R.layout.record_layout, null);
    }
}
