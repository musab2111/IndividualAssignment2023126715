package com.example.individualassignment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        TextView websiteLink = findViewById(R.id.websiteLink);
        websiteLink.setMovementMethod(LinkMovementMethod.getInstance());

    }
}