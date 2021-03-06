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
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
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

import static com.example.photos.AlbumActivity.albumName;

public class MainActivity extends AppCompatActivity {
    private static final String ALBUM_FILE_NAME = "albums.txt";
    public static HashSet<Photo> currentAlbum = null;

    public static HashMap<String, HashSet<Photo>> albums = new HashMap<String, HashSet<Photo>>();

    private final Context context = this;
    private static final int PICK_FROM_GALLERY = 1;

    ListView albumsList;
    Button addAlbumButton;
    Button searchTags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_view);
        searchTags = findViewById(R.id.searchTags);
        Utilities.readFile(ALBUM_FILE_NAME, MainActivity.this);
        setUpAddAlbumButton();

        albumsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int i, long l){
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                currentAlbum = albums.get(albumsList.getItemAtPosition(i).toString());
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                intent.putExtra("AlbumName", albumsList.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });

        searchTags.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SearchTagsActivity.class);
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

    public void removeAlbum(View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Remove Album");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textInput = input.getText().toString();
                if (textInput.isEmpty() ||  !albums.containsKey(textInput)) {
                    System.out.println("Empty");
                    Utilities.displayAlert(context, "Error", "Invalid album name");
                } else {
                    try {
                        String albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, textInput);
                        Utilities.deleteFile(context, albumFileName);
                        albums.remove(textInput);
                        List<String> albumNames = new ArrayList<>(albums.keySet());
                        refreshAlbums();
                        Utilities.writeToFile(ALBUM_FILE_NAME, albumNames, MainActivity.this);
//                        Utilities.displayAlert(context, "Confirm", "Please reopen album to see image");
                    } catch (Exception e) {
                        String msg = "Error writing to file";
                        throw new RuntimeException(msg, e);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
