package com.example.montessori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class DetailCategoryActivity extends AppCompatActivity {
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);

        imageButton = findViewById(R.id.back);
        imageButton.setOnClickListener(view -> finish());

    }
}