package com.example.photos.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.photos.MainActivity;
import com.example.photos.models.Photo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.photos.MainActivity.albums;

public class Utilities {

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
