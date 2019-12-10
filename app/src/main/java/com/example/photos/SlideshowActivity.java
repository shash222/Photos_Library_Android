package com.example.photos;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.models.Photo;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SlideshowActivity extends AppCompatActivity {

    List<Photo> photos;
    int i;
    ImageView image;

    private void updatePhoto() {
        image.setImageBitmap(BitmapFactory.decodeFile(photos.get(i).getLocation()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_slideshow);
        image = findViewById(R.id.imageView);
        photos = AlbumActivity.photos;
        i = 0;
        updatePhoto();

//        mAlbumName = (TextView) findViewById(R.id.AlbumName);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            mAlbumName.setText(bundle.getString("AlbumName"));
//            albumName = mAlbumName.getText().toString();
//        }
//        updateList();

    }

    public void prevPhoto(View view) {
        if (--i < 0) {
            i = photos.size() - 1;
        }
        updatePhoto();
    }

    public void nextPhoto(View view) {
        if (++i >= photos.size()) {
            i = 0;
        }
        updatePhoto();
    }



}
