package com.example.montessori.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;
import com.example.montessori.model.User;
import com.example.montessori.util.Constants;
import com.example.montessori.util.Helper;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, RegisterActivity.class)));

        if (auth.getCurrentUser() != null) {
            checkUserAccessLevel(auth.getCurrentUser().getUid());
        }
    }

    private void checkUserAccessLevel(String uid) {
        database.collection(ReferenceConstant.USERS).document(uid).get().addOnCompleteListener(task -> {
            if (task.getResult() != null && task.getResult().exists()) {
                User userData = task.getResult().toObject(User.class);
                if (userData != null && userData.getRole() != null) {
                    switch (userData.getRole()) {
                        case Constants.ROLE_ADMIN:
                            Helper.openAdminDashboard(this);
                            break;
                        case Constants.ROLE_PROFESSIONAL:
                        case Constants.ROLE_PARENT:
                            Helper.openUserDashboard(this);
                            break;
                        default:
                            Toast.makeText(StartActivity.this, "Role is not available.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(StartActivity.this, "User data is not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}