package com.example.myphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class GridViewArrayAdapter extends ArrayAdapter<ImageItem> {
    private Context _context;
    private int _layoutID;
    private List<ImageItem> _items;

    public GridViewArrayAdapter(@NonNull Context context, int resource, @NonNull List<ImageItem> objects, Context _context, int _layoutID, List<ImageItem> _items) {
        super(context, resource, objects);
        this._context = _context;
        this._layoutID = _layoutID;
        this._items = _items;
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

        ImageItem item = _items.get(position);
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(_context.getContentResolver(), item.get_uri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);

        return convertView;
    }
}
