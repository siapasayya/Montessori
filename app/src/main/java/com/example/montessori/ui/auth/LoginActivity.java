package com.example.montessori.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.montessori.R;
import com.example.montessori.UserDashboardActivity;
import com.example.montessori.archive.tes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;

    Button Btnlogin ;
    boolean valid = true;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        Btnlogin = findViewById(R.id.btn_login);

        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(email);
                checkField(password);
                Log.d("TAG", "onClick" + email.getText().toString());

                if (valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                            checkUserAccesLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void checkUserAccesLevel(String uid) {
        DocumentReference df = fStore.collection("User").document(uid);
//        extrack data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());
//                identify the user access level

                if (documentSnapshot.getString("isParent") != null) {
//                    user is admin
                    startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                    finish();
                }

                if (documentSnapshot.getString("isProfessional") != null) {
                    startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                    finish();
                }

                if (documentSnapshot.getString("isAdmin") != null) {
                    startActivity(new Intent(getApplicationContext(), tes.class));
                    finish();
                }
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            DocumentReference df = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
////                    if (documentSnapshot.getString("isParent") != null) {
////                        startActivity(new Intent(getApplicationContext(),tes.class));
////                        finish();
////                    }
////
////                    if (documentSnapshot.getString("isisProfessional") != null) {
////                        startActivity(new Intent(getApplicationContext(),tes2.class));
////                        finish();
////                    }
////
////                    if (documentSnapshot.getString("isAdmin") != null) {
////                        startActivity(new Intent(getApplicationContext(),tes.class));
////                        finish();
////                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    FirebaseAuth.getInstance().signOut();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                    finish();
//                }
//            });
//        }
//    }
}