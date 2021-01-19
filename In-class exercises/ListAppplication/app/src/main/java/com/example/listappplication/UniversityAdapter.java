package com.example.listappplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UniversityAdapter extends ArrayAdapter <UniversityInfo> {
    public UniversityAdapter(Context context, ArrayList<UniversityInfo> universityInfoArrayList) {
        super(context, 0, universityInfoArrayList);
        //int n = this.getCount();
        //UniversityInfo t = getItem(0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createRow(position, (ListView)parent);
        }
        return convertView;
        //return super.getView(position, convertView, parent);
    }

    private View createRow(int position, ViewGroup parent) {
        View itemView = createANewRow(position);
        UniversityInfo universityInfo = getItem(position);
        DisplayInfo(itemView, universityInfo);
        return itemView;
    }

    private void DisplayInfo(View itemView, final UniversityInfo universityInfo) {
        TextView textViewName = (TextView)itemView.findViewById(R.id.textViewName);
        textViewName.setText(universityInfo.Name);

        Button buttonTelephones = (Button)itemView.findViewById(R.id.buttonTelephone);
        buttonTelephones.setText(universityInfo.Tel);

        Button buttonWebsites = (Button)itemView.findViewById(R.id.buttonWebsite);
        buttonWebsites.setText(universityInfo.Website);
        buttonWebsites.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Uri url = Uri.parse(universityInfo.Website);
               Intent intent=new Intent(Intent.ACTION_VIEW, url);
               getContext().startActivity(intent);
           }
        });
    }

    private View createANewRow(int position) {
        return LayoutInflater.from(this.getContext()).inflate(R.layout.item_layout_1, null);
    }
}
