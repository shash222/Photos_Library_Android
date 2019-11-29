package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class AlbumActivity extends AppCompatActivity {

    TextView mAlbumName;
    LinearLayout linearLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mAlbumName = (TextView)findViewById(R.id.AlbumName);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mAlbumName.setText(bundle.getString("AlbumName"));
        }

        // Will be modified. This is for testing purposes to see how these objects work
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        for(int x=0;x<3;x++) {
            TextView text = new TextView(AlbumActivity.this);
            text.setText(Integer.toString(x));
//            ImageView image = new ImageView(AlbumActivity.this);
//            image.setBackgroundResource(R.drawable.ic_launcher_background);
            linearLayout1.addView(text);
        }

    }
}
