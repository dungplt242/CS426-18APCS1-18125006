package com.example.listappplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListView();
        setRecycleView();
    }

    private void setRecycleView() {
        RecyclerView myRV=(RecyclerView)findViewById(R.id.recyclerViewMain);
        ArrayList<UniversityInfo> universityInfoArrayList = new ArrayList<>();
        UniversityAdapter universityAdapter = new UniversityAdapter(this, universityInfoArrayList);
        myRV.setAdapter(myAdapter);
    }

    private void setListView() {
        ListView listView = (ListView)findViewById(R.id.listViewMain);

        ArrayList<UniversityInfo> universityInfoArrayList = new ArrayList<>();

        universityInfoArrayList.add(new UniversityInfo("ABC", "0909", "http."));
        universityInfoArrayList.add(new UniversityInfo("ABC", "0909", "http."));
        universityInfoArrayList.add(new UniversityInfo("ABC", "0909", "http."));


        UniversityAdapter universityAdapter = new UniversityAdapter(this, universityInfoArrayList);
        listView.setAdapter(universityAdapter);


    }
}
