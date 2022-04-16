package com.example.montessori.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText fullName, email, password, username;

    CheckBox isParentBox, isProfBox;
    Button Btnregister;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.et_fullname);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        username = findViewById(R.id.et_username);

        Btnregister = findViewById(R.id.btn_register);


        isParentBox = findViewById(R.id.isParent);
        isProfBox = findViewById(R.id.isProf);

//        progressDialog = new ProgressDialog(Register.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Menyimpan...");


//                check box logic
        isParentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isProfBox.setChecked(false);
                }
            }
        });

        isProfBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isParentBox.setChecked(false);
                }
            }
        });
//
//        Btnregister.setOnClickListener(v -> {
//            if (fullName.getText().length()>0 && email.getText().length()>0 && username.getText().length()>0 && password.getText().length()>0){
//                saveData(fullName.getText().toString(), email.getText().toString(), username.getText().toString(), password.getText().toString());
//            }else{
//                Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Intent intent = getIntent();
//        if (intent!=null){
//            fullName.setText(intent.getStringExtra("FullName"));
//            email.setText(intent.getStringExtra("UserEmail"));
//            username.setText(intent.getStringExtra("UserName"));
//            password.setText(intent.getStringExtra("Password"));
//        }
//    }
//    private void saveData(String FullName, String UserEmail, String UserName, String Password) {
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("FullName", FullName);
//        userInfo.put("UserEmail", UserEmail);
//        userInfo.put("UserName", UserName);
//        userInfo.put("Password", Password);
//
//        progressDialog.show();
//        FirebaseUser user = fAuth.getCurrentUser();
//
//        if (user != null) {
//            fStore.collection("User").document(user.getUid())
//                    .set(userInfo)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
//                                finish();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            fStore.collection("User")
//                    .add(userInfo)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    });
//        }
//    }


        Btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(username);


//                checkbox validation
                if (!(isParentBox.isChecked() || isProfBox.isChecked())) {
                    Toast.makeText(RegisterActivity.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valid) {
//            start the user registration
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("User").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullName.getText().toString());
                            userInfo.put("UserEmail", email.getText().toString());
                            userInfo.put("UserName", username.getText().toString());
                            userInfo.put("Password", password.getText().toString());


//                    spesify if user is admin
                            if (isParentBox.isChecked()) {
                                userInfo.put("isParent", "1");
                            }
                            if (isProfBox.isChecked()) {
                                userInfo.put("isProfessional", "1");
                            }

                            df.set(userInfo);

//
//                            if (isParentBox.isChecked()) {
//                                startActivity(new Intent(getApplicationContext(),Login.class));
//                                finish();
//                            }
//                            if (isProfBox.isChecked()) {
//                                startActivity(new Intent(getApplicationContext(),Login.class));
//                                finish();
//                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }

    protected void onStop() {
        super.onStop();
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

}