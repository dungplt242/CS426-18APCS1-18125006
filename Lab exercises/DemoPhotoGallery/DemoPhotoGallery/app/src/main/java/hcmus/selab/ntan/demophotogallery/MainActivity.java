package hcmus.selab.ntan.demophotogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private GridView _gridView;
    private GridViewArrayAdapter _adapter;
    private ArrayList<ImageItem> _images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        updateGridView();
    }

    private void updateGridView() {
        (new AsyncTaskLoadImage(this)).execute();
    }

    private void initComponents() {
        _gridView = findViewById(R.id.gridViewGallery);
    }

    private class AsyncTaskLoadImage extends AsyncTask<Void, Void, List<ImageItem>> {
        private ProgressDialog _dialog;

        public AsyncTaskLoadImage(Activity activity) {
            _dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            _dialog.setTitle("Loading");
            _dialog.show();
        }

        @Override
        protected List<ImageItem> doInBackground(Void... voids) {
            // https://developer.android.com/training/data-storage/shared/media
            ArrayList<ImageItem> results = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME
            };
            String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";

            try (Cursor cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    //MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
            )) {
                // Cache column indices.
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

                while (cursor.moveToNext()) {
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
        protected void onPostExecute(List<ImageItem> results) {
            _images = (ArrayList<ImageItem>) results;
            _adapter = new GridViewArrayAdapter(MainActivity.this,
                    R.layout.gridview_item, _images);
            _gridView.setAdapter(_adapter);
            if (_dialog.isShowing()) {
                _dialog.dismiss();
            }
        }
    }
}
