package com.example.myphoto;

import android.net.Uri;

public class ImageItem {
    private Uri _uri;
    private String _title;

    public ImageItem(Uri _uri, String _title) {
        this._uri = _uri;
        this._title = _title;
    }

    public Uri get_uri() {
        return _uri;
    }

    public String get_title() {
        return _title;
    }
}
