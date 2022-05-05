package com.example.montessori.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.montessori.R;
import com.example.montessori.util.IntentNameExtra;
import com.github.chrisbanes.photoview.PhotoView;

public class ImagePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        String imageUrl = getIntent().getStringExtra(IntentNameExtra.IMAGE_URL_DATA);

        ImageView btnBack = findViewById(R.id.btnBack);
        PhotoView photoView = findViewById(R.id.imagePreviewCanvas);

        btnBack.setOnClickListener(view -> finish());
        Glide.with(this).load(imageUrl).into(photoView);
    }
}