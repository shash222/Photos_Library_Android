package com.example.photos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

import com.example.photos.models.Photo;
import com.example.photos.utilities.Utilities;

public class EditTagsActivity extends AppCompatActivity {
    List<Photo> photos;
    int i;
    String album;
    public final Context context = this;

    ListView persons;
    ListView locations;
    Button addPersonButton;
    Button addLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags_view);
        persons =(ListView)findViewById(R.id.persons);
        locations =(ListView)findViewById(R.id.locations);
        addPersonButton = (Button)findViewById(R.id.addPersonButton);
        addLocationButton = (Button)findViewById(R.id.addLocationButton);


        photos = AlbumActivity.photos;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            i = bundle.getInt("index");
            album = bundle.getString("album");
            refreshAlbums();
        }

        addPersonButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mAlbumName = (EditText) mView.findViewById(R.id.etAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.btnAddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!mAlbumName.getText().toString().isEmpty() && !AlbumActivity.photos.get(i).getTags().get("person").contains(mAlbumName.getText().toString())){
                            AlbumActivity.photos.get(i).addTag("person", mAlbumName.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                        }
                        refreshAlbums();
                    }
                });
                refreshAlbums();
            }
        });

        addLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTagsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mAlbumName = (EditText) mView.findViewById(R.id.etAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.btnAddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!mAlbumName.getText().toString().isEmpty() && !AlbumActivity.photos.get(i).getTags().get("location").contains(mAlbumName.getText().toString())){
                            AlbumActivity.photos.get(i).addTag("location", mAlbumName.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(EditTagsActivity.this, "Error Creating", Toast.LENGTH_SHORT).show();
                        }
                        refreshAlbums();
                    }
                });
                refreshAlbums();

            }
        });



    }

    private void refreshAlbums(){
        List<String> listOfPersons = new ArrayList<String>(AlbumActivity.photos.get(i).getTags().get("person"));
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfPersons);
        persons.setAdapter(arrayAdapter);

        List<String> listOfLocations = new ArrayList<String>(AlbumActivity.photos.get(i).getTags().get("location"));
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfLocations);
        locations.setAdapter(arrayAdapter2);

        String albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, album);
        Utilities.writeSerializedObjectToFile(context, AlbumActivity.photos, albumFileName);

    }

    private void AddPersonView(View view){

    }

}