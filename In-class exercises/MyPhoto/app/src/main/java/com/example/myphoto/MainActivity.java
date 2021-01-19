package com.example.myphoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView _gridView;
    private ArrayList<ImageItem> _items;
    private GridViewArrayAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        loadImages();
    }

    private void loadImages() {
        (new AsyncTaskLoadImage()).execute();
    }

    private void initComponents() {
        _gridView = findViewById(R.id.gridViewImage);
    }

    private class AsyncTaskLoadImage extends AsyncTask<Void, Void, List<ImageItem>>{

        @Override
        protected List<ImageItem> doInBackground(Void... voids) {
            ArrayList<ImageItem> results = new ArrayList<>();
            String[] projection = new String[] {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
            };
            String selection = MediaStore.Images.Media.DISPLAY_NAME + " ASC";
            String[] selectionArgs = new String[] {
                    String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
};
            String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

            try (Cursor cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
            )) {
                // Cache column indices.
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int durationColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    ImageItem item = new ImageItem(contentUri, name);
                    results.add(item);
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<ImageItem> imageItems) {
            _items = (ArrayList<Image>) imageItems;
            _adapter = new GridViewArrayAdapter(MainActivity.this, R.layout.gridview_item);
            _gridView.setAdapter(_adapter);
        }

    }
}