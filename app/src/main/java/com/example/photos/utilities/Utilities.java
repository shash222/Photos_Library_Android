package com.example.photos.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.provider.SyncStateContract;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.AlbumActivity;
import com.example.photos.Constants;
import com.example.photos.MainActivity;
import com.example.photos.R;
import com.example.photos.models.Photo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.photos.MainActivity.albums;

public class Utilities {

    public static void deleteFile(Context context, String fileName) {
        context.deleteFile(fileName);
    }

    public static boolean writeToFile(String fileName, List<String> content, Context context) throws IOException {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            for( String album : content){
                fos.write(album.getBytes());
                fos.write("\n".getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public static void readFile(String fileName, Context context){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                MainActivity.albums.put(line, new HashSet<Photo>());
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSerializedObjectToFile(Context context, List<Photo> photos, String filePath) {
        try {
            System.out.println("Photos: " + photos);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(filePath, MODE_PRIVATE));
            objectOutputStream.writeObject(photos);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            String msg = "Cannot find file";
            throw new RuntimeException(msg, e);
        } catch (IOException e) {
            String msg = "IOException";
            throw new RuntimeException(msg, e);
        }
    }


    public static void displayAlert(Context context, String alertType, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static void createFileIfNotExists(Context context, String filename) {
        try {
            File file = context.getFileStreamPath(filename);
            if(file == null || !file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating file", e);
        }
    }

    public static List<Photo> readSerializedObjectFromFile(Context context, String fileName) {
        List<Photo> photosInAlbum = new ArrayList<>();
        try {
            createFileIfNotExists(context, fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(fileName));
            List<Photo> photoList = (ArrayList<Photo>) objectInputStream.readObject();
            objectInputStream.close();
            return photoList;
        } catch (EOFException e) {
            System.out.println("End of file reached, prevented from throwing");
        } catch (IOException e) {
            String msg = "IOException";
            throw new RuntimeException(msg, e);
        } catch (ClassNotFoundException e) {
            String msg = "Could not find class";
            throw new RuntimeException(msg, e);
        }
        return photosInAlbum;
    }
}
