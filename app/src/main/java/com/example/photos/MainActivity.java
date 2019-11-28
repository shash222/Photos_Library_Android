package com.example.photos;

import java.util.*;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.photos.models.Photo;

public class MainActivity extends AppCompatActivity {

    public static HashMap<String, HashSet<Photo>> albums = new HashMap<String, HashSet<Photo>>();

    ListView albumsList;
    Button addAlbumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_view);

        addAlbumButton = findViewById(R.id.add_album);
        albumsList=(ListView)findViewById(R.id.albums);

        addAlbumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mAlbumName = (EditText) mView.findViewById(R.id.etAlbumName);
                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        if(!mAlbumName.getText().toString().isEmpty() && !albums.containsKey(mAlbumName.getText().toString())){
                            Toast.makeText(MainActivity.this, "New Album Created", Toast.LENGTH_SHORT).show();
                            albums.put(mAlbumName.getText().toString(), new HashSet<Photo>());
                            refreshAlbums();
                            mView.setVisibility(View.GONE);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Error Creating Album", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        refreshAlbums();
    }

    public void refreshAlbums(){
        List<String> listOfAlbums = new ArrayList<String>(albums.keySet());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfAlbums);
        albumsList.setAdapter(arrayAdapter);
    }
}
