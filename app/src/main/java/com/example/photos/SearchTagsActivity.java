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

public class SearchTagsActivity extends AppCompatActivity {


    public final Context context = this;

    EditText location;
    EditText person;
    Button AndSearch;
    Button OrSearch;
    public static List<Photo> finalList = new ArrayList<>();
    private String type = "and";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_tags);

        location = findViewById(R.id.editTextLocation);
        person = findViewById(R.id.editTextPerson);
        AndSearch = findViewById(R.id.andSearch);
        OrSearch = findViewById(R.id.orSearch);

        AndSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                type = "and";
                search();
            }
        });
        OrSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                type = "or";
                search();
            }
        });
    }
    public void search() {

        finalList.clear();
        if (type.equals("and") || type.equals("or")) {
            String locationValue = location.getText().toString().toLowerCase();
            String personValue = person.getText().toString().toLowerCase();

            HashMap<String, HashSet<Photo>> Albums = MainActivity.albums;
            Iterator albumIterator = Albums.entrySet().iterator();

            while (albumIterator.hasNext()) {
                Map.Entry albumElement = (Map.Entry) albumIterator.next();

                String key = (String) albumElement.getKey();
                String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, key);
                List<Photo> photos = Utilities.readSerializedObjectFromFile(context, albumPath);

                for (Photo photo : photos) {
                    Set<String> tagNames = photo.getTags().keySet();

                    if (type.equals("and")) {
                        if (photo.getTags().get("location").contains(locationValue) && photo.getTags().get("person").contains(personValue)) {
                            finalList.add(photo);
                        }
                    } else {
                        if (photo.getTags().get("location").contains(locationValue) || photo.getTags().get("person").contains(personValue)) {
                            finalList.add(photo);
                        }
                    }
                }
            }
        }

        Intent intent = new Intent(SearchTagsActivity.this, SearchResultsActivity.class);
        startActivity(intent);
    }

}
