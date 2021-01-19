package com.example.demomediaplayer;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class MySong {
    private String _name;
    private Bitmap _thumbnail;
    private MediaPlayer _mediaPlayer;

    public MySong(String _name, Bitmap _thumbnail, MediaPlayer _mediaPlayer) {
        this._name = _name;
        this._thumbnail = _thumbnail;
        this._mediaPlayer = _mediaPlayer;
    }

    public String get_name() {
        return _name;
    }

    public Bitmap get_thumbnail() {
        return _thumbnail;
    }

    public MediaPlayer get_mediaPlayer() {
        return _mediaPlayer;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_thumbnail(Bitmap _thumbnail) {
        this._thumbnail = _thumbnail;
    }

    public void set_mediaPlayer(MediaPlayer _mediaPlayer) {
        this._mediaPlayer = _mediaPlayer;
    }

}
