package com.example.montessori.ui.auth;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;
import com.example.montessori.model.User;
import com.example.montessori.util.Constants;
import com.example.montessori.util.Helper;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText tvEmail, tvPassword;

    private Button btnLogin;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvEmail = findViewById(R.id.et_email);
        tvPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);

        tvPassword.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnLogin.performClick();
                return true;
            }

            return false;
        });
        btnLogin.setOnClickListener(view -> {
            if (isFieldNotValid(tvEmail) || isFieldNotValid(tvPassword)) {
                return;
            }

            auth.signInWithEmailAndPassword(tvEmail.getText().toString().trim(), tvPassword.getText().toString().trim()).addOnSuccessListener(authResult -> {
                if (authResult.getUser() != null) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());
                } else {
                    Toast.makeText(LoginActivity.this, "No User Found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });
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
                            Toast.makeText(LoginActivity.this, "Role is not available.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User data is not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isFieldNotValid(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("This field is empty.");
            return true;
        } else {
            return false;
        }
    }
}