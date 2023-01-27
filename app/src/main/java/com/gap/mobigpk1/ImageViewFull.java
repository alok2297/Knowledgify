package com.gap.mobigpk1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageViewFull extends AppCompatActivity {

    private ImageView exit;
    private ImageView fullPic;
    private String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_full);

        Intent intent = getIntent();
        pic = intent.getStringExtra("fullPic");

        exit=findViewById(R.id.exit);
        fullPic=findViewById(R.id.zoom);

        Glide.with(this).load(pic).into(fullPic);

        getSupportActionBar().hide();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}