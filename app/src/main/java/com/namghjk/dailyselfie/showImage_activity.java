package com.namghjk.dailyselfie;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class showImage_activity extends AppCompatActivity {

    ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        addControlls();
        addEvents();

    }

    private void addEvents() {
    }

    private void addControlls() {
        imgV = findViewById(R.id.imageview1);
        Bundle bundle = getIntent().getExtras();
        String filepath = (String) bundle.get("filepath");
        imgV.setImageBitmap(BitmapFactory.decodeFile(filepath));

    }
}