package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.photos.models.Photo;
import com.example.photos.utilities.Utilities;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    TextView mAlbumName;

    public static String albumName;
    public final Context context = this;

    private void updateList() {
        List<Photo> photos = Utilities.readSerializedObjectFromFile(this, String.format(Constants.ALBUM_PATH_FORMAT, albumName));
        TableLayout entryList = findViewById(R.id.entryList);
        entryList.removeAllViews();

        for (Photo p : photos) {
            TableRow row = new TableRow(this);
            row.setPadding(0, 5, 0, 5);
            TextView photoLocation = new TextView(this);
//            photoLocation.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            photoLocation.setText(p.getLocation());

            ImageView image = new ImageView(this);
            image.setPadding(5,0 , 5, 0);
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

        mAlbumName = (TextView)findViewById(R.id.AlbumName);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mAlbumName.setText(bundle.getString("AlbumName"));
            albumName = mAlbumName.getText().toString();
        }
        updateList();

    }

    public void openAddPhotoDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");

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
                String albumFileName;
                if (textInput.isEmpty()) {
                    System.out.println("Empty");
                    Utilities.displayAlert(context, "Error", "Invalid filepath");
                } else {
                    File file = new File(textInput);
                    if (file.isFile()) {
                        try {
                            albumFileName = String.format(Constants.ALBUM_PATH_FORMAT, albumName);
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.MILLISECOND, 0);
                            Photo photo = new Photo(textInput, new Date(file.lastModified()));
                            List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(context, albumFileName);
                            photosInAlbum.add(photo);
                            Utilities.writeSerializedObjectToFile(context, photosInAlbum, albumFileName);
                            Utilities.displayAlert(context, "Confirm", "User will be added after closing this box");
                        } catch (Exception e) {
                            String msg = "Error writing to file";
                            throw new RuntimeException(msg, e);
                        }
                    } else {
                        Utilities.displayAlert(context, "Error", "Photo not found");
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
        updateList();

    }
}
