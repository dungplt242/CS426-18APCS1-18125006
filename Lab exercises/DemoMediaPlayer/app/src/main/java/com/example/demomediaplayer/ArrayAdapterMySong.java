package com.example.demomediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArrayAdapterMySong extends ArrayAdapter<MySong> {
    private Context _context;
    private int _layoutID;
    private List<MySong> _items;
    public ArrayAdapterMySong (@NonNull Context context, int resource, @NonNull List<MySong> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _items = objects;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewThumbnail);
        TextView textView = convertView.findViewById(R.id.textName);
        final Button button = convertView.findViewById(R.id.buttonPlay);

        final MySong song = _items.get(position);
        imageView.setImageBitmap(song.get_thumbnail());
        textView.setText(song.get_name());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!song.get_mediaPlayer().isPlaying()){
                    song.get_mediaPlayer().start();
                    button.setBackgroundResource(android.R.drawable.ic_media_pause);
                }
                else {
                    song.get_mediaPlayer().pause();
                    button.setBackgroundResource(android.R.drawable.ic_media_play);
                }
            }
        });

        return convertView;
    }
}
