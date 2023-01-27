package com.gap.mobigpk1;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvAbout = findViewById(R.id.tvAbout);

        tvAbout.setText(Html.fromHtml(String.format(getString(R.string.about))));
    }
}