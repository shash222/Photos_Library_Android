package com.example.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.models.Photo;
import com.example.photos.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SlideshowActivity extends AppCompatActivity {

    List<Photo> photos;
    int i;
    ImageView image;
    String album;
    Button move;
    public final Context context = this;


    private void updatePhoto() {
        image.setImageBitmap(BitmapFactory.decodeFile(photos.get(i).getLocation()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_slideshow);
        image = findViewById(R.id.imageView);
        move = findViewById(R.id.moveToAlbum);
        photos = AlbumActivity.photos;
        i = 0;
        updatePhoto();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album = (bundle.getString("album"));
        }

        move.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SlideshowActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.move_to_album, null);
                final EditText mAlbumName = (EditText) mView.findViewById(R.id.etAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.btnAddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!mAlbumName.getText().toString().isEmpty() && MainActivity.albums.containsKey(mAlbumName.getText().toString())){
                            String albumFileNameNew = String.format(Constants.ALBUM_PATH_FORMAT, mAlbumName.getText().toString());
                            List<Photo> photosInNewAlbum = Utilities.readSerializedObjectFromFile(context, albumFileNameNew);
                            photosInNewAlbum.add(AlbumActivity.photos.get(i));
                            // MainActivity.albums.get(mAlbumName.getText().toString()).add(AlbumActivity.photos.get(i));
                            /* NEED TO MAKE IT SUCH THAT IT DELETES THE IMAGE IN THE CURRENT ALBUM
                            HashSet<Photo> photos =  MainActivity.albums.get(AlbumActivity.albumName);
                            for(Photo p : photos){
                                if()
                            }
                            MainActivity.albums.get(AlbumActivity.albumName).remove(AlbumActivity.photos.get(i));

                            List<Photo> list = new ArrayList<Photo>(MainActivity.albums.get(AlbumActivity.albumName));
                            String albumFileNameThis = String.format(Constants.ALBUM_PATH_FORMAT, album);
                            Utilities.writeSerializedObjectToFile(context, list, albumFileNameThis);
                            */
                            // List<Photo> list2 = new ArrayList<Photo>( MainActivity.albums.get(mAlbumName.getText().toString()));
                            Utilities.writeSerializedObjectToFile(context,photosInNewAlbum, albumFileNameNew);


                            Intent intent = new Intent(SlideshowActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SlideshowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

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

    public void viewEditTags(View view) {
        Intent intent = new Intent(SlideshowActivity.this, EditTagsActivity.class);
        intent.putExtra("index",i);
        intent.putExtra("album",album);

        startActivity(intent);
    }



}
