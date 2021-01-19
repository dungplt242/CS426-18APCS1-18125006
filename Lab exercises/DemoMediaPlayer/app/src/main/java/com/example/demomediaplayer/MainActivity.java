package com.example.demomediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView _listViewSongs;
    private ArrayList<MySong> _songs;
    private ArrayAdapterMySong _arrayAdapterMySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSongs();
        initComponents();
    }

    private void loadSongs() {
        String[] names = {"Có phương tự thưởng",
        "Con đường bình phàm",
        "Kiêu ngạo",
        "Lối nhỏ",
        "Tướng quân"
        };
        int[] thumbnail_IDs = {
                R.drawable.co_phuong,
                R.drawable.con_duong,
                R.drawable.kieu_ngao,
                R.drawable.loi_nho,
                R.drawable.tuong_quan
        };
        int[] media_IDs = {
                R.raw.co_phuong,
                R.raw.con_duong,
                R.raw.kieu_ngao,
                R.raw.loi_nho,
                R.raw.tuong_quan
        };
        _songs = new ArrayList<>();
        for (int i=0; i<names.length; i++){
            MySong s = new MySong(names[i], BitmapFactory.decodeResource(getResources(), thumbnail_IDs[i]), MediaPlayer.create(this, media_IDs[i]));
            _songs.add(s);
        }
    }

    private void initComponents() {
        _listViewSongs = findViewById(R.id.listViewSong);
        _arrayAdapterMySong = new ArrayAdapterMySong(this, R.layout.listview_song_item, _songs);
        _listViewSongs.setAdapter(_arrayAdapterMySong);
    }
}