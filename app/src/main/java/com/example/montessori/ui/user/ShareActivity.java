package com.example.montessori.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.montessori.R;

public class ShareActivity extends AppCompatActivity {
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        imageButton = findViewById(R.id.btnBack);
        imageButton.setOnClickListener(view -> finish());
    }
}