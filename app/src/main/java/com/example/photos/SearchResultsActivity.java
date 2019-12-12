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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.example.photos.models.Photo;
import com.example.photos.utilities.Utilities;

public class SearchResultsActivity extends AppCompatActivity {


    public final Context context = this;
    static List<Photo> photos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        updateList();
    }

    private void updateList() {
        photos = SearchTagsActivity.finalList;
        TableLayout entryList = findViewById(R.id.searchList);
        entryList.invalidate();
        entryList.removeAllViews();

        int i = 0;
        for (Photo p : photos) {
            TableRow row = new TableRow(this);
            row.setClickable(true);
            row.setId(i++);

            row.setPadding(0, 5, 0, 5);
            TextView photoLocation = new TextView(this);
            photoLocation.setText(p.getLocation());

            ImageView image = new ImageView(this);
            image.setPadding(5, 0, 5, 0);
            image.setImageBitmap(BitmapFactory.decodeFile(p.getLocation()));
            row.addView(image);
            row.addView(photoLocation);
            entryList.addView(row);
        }
        entryList.refreshDrawableState();

    }

}
