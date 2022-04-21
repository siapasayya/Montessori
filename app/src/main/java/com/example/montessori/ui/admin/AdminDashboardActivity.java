package com.example.montessori.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.AdminAllPostActivity;
import com.example.montessori.R;
import com.example.montessori.util.Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private Button btnAllPost, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAllPost = findViewById(R.id.btn_all_post);
        btnLogout = findViewById(R.id.btn_logout);

        btnAllPost.setOnClickListener(view -> startActivity(new Intent(this, AdminAllPostActivity.class)));
        btnLogout.setOnClickListener(view1 -> Helper.doLogout(this));
    }
}