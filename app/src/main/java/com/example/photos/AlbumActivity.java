package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.photos.models.Photo;
import com.example.photos.utilities.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AlbumActivity extends Activity {
    TextView mAlbumName;

    public static String albumName;
    public final Context context = this;
    Uri currImageURI;

    static List<Photo> photos;
    View selected;

    private void updateList() {
        photos = Utilities.readSerializedObjectFromFile(this, String.format(Constants.ALBUM_PATH_FORMAT, albumName));
        TableLayout entryList = findViewById(R.id.entryList);
        entryList.invalidate();
        entryList.removeAllViews();

        int i = 0;
        for (Photo p : photos) {
            TableRow row = new TableRow(this);
            row.setClickable(true);
            row.setId(i++);
            row.setOnClickListener(new View.OnClickListener() {
                private void resetBackgroundColors(int idToSkip, TableLayout table) {
                    int tableSize = table.getChildCount();
                    for (int i = 0; i < tableSize; i++) {
                        if (i != idToSkip) table.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                @Override
                public void onClick(View view) {
                    view.setBackgroundColor(Color.LTGRAY);
                    resetBackgroundColors(view.getId(), (TableLayout) view.getParent());
                    selected = view;
                }
            });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mAlbumName = (TextView) findViewById(R.id.AlbumName);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAlbumName.setText(bundle.getString("AlbumName"));
            albumName = mAlbumName.getText().toString();
        }
        updateList();

    }

    public void openAddPhotoDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

        // Set up the input
//        final EditText input = new EditText(this);
//
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String textInput = input.getText().toString();
//                String albumFileName;
//                if (textInput.isEmpty()) {
//                    System.out.println("Empty");
//                    Utilities.displayAlert(context, "Error", "Invalid filepath");
//                } else {
//                    File file = new File(textInput);
//                    if (file.isFile()) {
//                        try {
//                            albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, albumName);
//                            Calendar cal = Calendar.getInstance();
//                            cal.set(Calendar.MILLISECOND, 0);
//
//                            Photo photo = new Photo(textInput, new Date(file.lastModified()));
//                            List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(context, albumFileName);
//                            photosInAlbum.add(photo);
//                            Utilities.writeSerializedObjectToFile(context, photosInAlbum, albumFileName);
//                            Utilities.displayAlert(context, "Confirm", "Please reopen album to see image");
//                        } catch (Exception e) {
//                            String msg = "Error writing to file";
//                            throw new RuntimeException(msg, e);
//                        }
//                    } else {
//                        Utilities.displayAlert(context, "Error", "Photo not found");
//                    }
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
        TableLayout entryList = findViewById(R.id.entryList);
        updateList();
    }

    public void removePhoto(View view) {
        System.out.println("Attempting to remove");
        if (selected != null) {
            String albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, albumName);
            TableLayout table = (TableLayout) selected.getParent();

            photos.remove(selected.getId());
            table.refreshDrawableState();
            Utilities.writeSerializedObjectToFile(this, photos, albumFileName);
            updateList();
        }
    }

    public void viewSlideshow(View view) {
        Intent intent = new Intent(this, SlideshowActivity.class);
        intent.putExtra("album",albumName);
        if(selected != null){
            intent.putExtra("i",selected.getId());
        } else {
            intent.putExtra("i",0);

        }

        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {


                currImageURI = data.getData();
                String textInput = getRealPathFromURI(currImageURI);
                File file = new File(textInput);
                String albumFileName;
                if (file.isFile()) {
                    try {
                        albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, albumName);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MILLISECOND, 0);

                        Photo photo = new Photo(textInput, new Date(file.lastModified()));
                        List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(context, albumFileName);
                        photosInAlbum.add(photo);
                        Utilities.writeSerializedObjectToFile(context, photosInAlbum, albumFileName);
                    } catch (Exception e) {
                        String msg = "Error writing to file";
                        throw new RuntimeException(msg, e);
                    }
                } else {
                    Utilities.displayAlert(context, "Error", "Photo not found");
                }


        }
        TableLayout entryList = findViewById(R.id.entryList);
        updateList();
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = this.context.getContentResolver().query( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }





}