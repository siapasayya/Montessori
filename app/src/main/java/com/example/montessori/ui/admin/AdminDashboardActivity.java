package com.example.montessori.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;
import com.example.montessori.util.Helper;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnApprove = findViewById(R.id.btn_approve);
        Button btnAllPost = findViewById(R.id.btn_all_post);
        ImageButton btnLogout = findViewById(R.id.ib_logout);

        btnApprove.setOnClickListener(view -> startActivity(new Intent(this, AdminListApprovalActivity.class)));
        btnAllPost.setOnClickListener(view -> startActivity(new Intent(this, AdminAllPostActivity.class)));
        btnLogout.setOnClickListener(v -> Helper.doLogout(this));
    }
}