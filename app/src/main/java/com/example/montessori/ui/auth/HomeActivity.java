package com.example.montessori.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.montessori.R;
import com.example.montessori.UserDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button Btnlogin, Btnregister;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Btnlogin = findViewById(R.id.btn_login);
        Btnregister = findViewById(R.id.btn_register);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, UserDashboardActivity.class));
        }

        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        Btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
            }
        });
    }
}