package com.example.photos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private static final String ALBUM_FILE_NAME = "albums.txt";
    public static HashSet<Photo> currentAlbum = null;

    public static HashMap<String, HashSet<Photo>> albums = new HashMap<String, HashSet<Photo>>();

    ListView albumsList;
    Button addAlbumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_view);

        Utilities.readFile(ALBUM_FILE_NAME, MainActivity.this);
        setUpAddAlbumButton();


        albumsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int i, long l){
                currentAlbum = albums.get(albumsList.getItemAtPosition(i).toString());
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                intent.putExtra("AlbumName", albumsList.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
    }
    private void refreshAlbums(){
        List<String> listOfAlbums = new ArrayList<String>(albums.keySet());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfAlbums);
        albumsList.setAdapter(arrayAdapter);
    }

    private void setUpAddAlbumButton(){
        addAlbumButton = findViewById(R.id.add_album);
        albumsList=(ListView)findViewById(R.id.albums);
        addAlbumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mAlbumName = (EditText) mView.findViewById(R.id.etAlbumName);
                Button mAddAlbum = (Button) mView.findViewById(R.id.btnAddAlbum);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAddAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!mAlbumName.getText().toString().isEmpty() && !albums.containsKey(mAlbumName.getText().toString())){
                            addAlbum(mAlbumName, mView, dialog);
                        } else {
                            Toast.makeText(MainActivity.this, "Error Creating Album", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        refreshAlbums();
    }

    private void addAlbum(EditText mAlbumName, View mView, AlertDialog dialog){
        albums.put(mAlbumName.getText().toString(), new HashSet<Photo>());
        List<String> albumNames = new ArrayList<>(albums.keySet());
        refreshAlbums();
        FileOutputStream fos = null;
        try {
            Utilities.writeToFile(ALBUM_FILE_NAME, albumNames, MainActivity.this);
            Toast.makeText(MainActivity.this, "New Album Created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mView.setVisibility(View.GONE);
        dialog.dismiss();
    }
}
