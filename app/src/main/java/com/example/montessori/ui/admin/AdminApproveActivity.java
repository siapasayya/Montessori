package com.example.montessori.ui.admin;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;

public class AdminApproveActivity extends AppCompatActivity {
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);
        imageButton = findViewById(R.id.back);

        imageButton.setOnClickListener(view -> finish());
    }
}