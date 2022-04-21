package com.example.montessori.archive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.montessori.AdminAllPostActivity;
import com.example.montessori.R;
import com.example.montessori.ui.auth.LoginActivity;
import com.example.montessori.ui.auth.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class tes extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private Button btn = findViewById(R.id.btn_AllPost);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAllPostActivity.class));
            }
        });
    }

    public void LogoutAdmin(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
    

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (fAuth.getCurrentUser() != null) {
//            new Intent(this, tes.class).
//        }
//    }
}