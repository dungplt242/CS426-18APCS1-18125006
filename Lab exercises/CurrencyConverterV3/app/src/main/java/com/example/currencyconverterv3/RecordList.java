package com.example.currencyconverterv3;

import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class RecordList extends AppCompatActivity {
    static private ArrayList<MyRecord> list;
    private RecordAdapter recordAdapter; //final
    public UserHistory context;
    ListView listView;

    public RecordList(UserHistory context) {
        if (list == null)
            list = new ArrayList<>();
        listView = context.findViewById(R.id.history_list_view);
        recordAdapter = new RecordAdapter(context, list);
        this.context = context;
    }

    public RecordList() {
        if (list == null)
            list = new ArrayList<>();
    }


    public void loadList(MainActivity context)  {
        try {
            Scanner scan = new Scanner(context.openFileInput("history.txt"));
            while (scan.hasNextLine()) {
                MyRecord record = new MyRecord(scan);
                list.add(0, record);
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNewRecord(MyRecord newRecord, MainActivity context) {
        list.add(0, newRecord);
        try {
            PrintStream output = new PrintStream(context.openFileOutput("history.txt", MODE_APPEND));
            newRecord.export(output);
            output.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setView() {
        listView.setAdapter(recordAdapter);
    }
}
