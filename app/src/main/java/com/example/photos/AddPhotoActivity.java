package com.example.photos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.photos.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Controller that adds photo to album
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class AddPhotoActivity extends AppCompatActivity {
    TextView addPhotoLabel;
    EditText photoPathEntry;
    Button addPhotoButton;

    private static final String ADD_PHOTO_LABEL_FORMAT = "Add photo to your %s album";

    /**
     * Runs whenever controller is triggered
     * @param savedInstanceState default param
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.addPhotoLabel.setText(String.format(ADD_PHOTO_LABEL_FORMAT, UserController.selectedAlbum));
    }

    /**
     * Adds photo to album
     * @param view response to clicking button
     */
    public void addPhotoToAlbum(View view) {
        System.out.println(addPhotoButton.getText());
//        String photoPath = this.photoPath.getText();
//        if (photoPath.isEmpty()) {
//            Utilities.displayAlert(Alert.AlertType.ERROR, "No filepath entered");
//        } else {
//            File file = new File(photoPath);
//            if (file.isFile()) {
//                try {
//                    String albumPath = String.format(Constants.ALBUM_PATH_FORMAT, Photos.currentUser, UserController.selectedAlbum);
//                    Calendar cal = Calendar.getInstance();
//                    cal.set(Calendar.MILLISECOND, 0);
//                    Photo photo = new Photo(photoPath, (captionText.getText() == null) ? "" : captionText.getText(), new Date(file.lastModified()));
//                    List<Photo> photosInAlbum = Utilities.readSerializedObjectFromFile(albumPath);
//                    photosInAlbum.add(photo);
//                    Utilities.writeSerializedObjectToFile(photosInAlbum, albumPath);
//                    Utilities.displayAlert(Alert.AlertType.CONFIRMATION, "User will be added after closing this box");
//                } catch (Exception e) {
//                    String msg = "Error writing to file";
//                    throw new RuntimeException(msg, e);
//                }
//            } else {
//                Utilities.displayAlert(Alert.AlertType.ERROR, "Photo not found");
//            }
//        }
    }


}
