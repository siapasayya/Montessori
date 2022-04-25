package com.example.montessori.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.montessori.R;
import com.example.montessori.util.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private MaterialButton buttonLogout;
    private TextView username,email, Role, profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.tv_name);
        email = view.findViewById(R.id.tv_email);
        Role = view.findViewById(R.id.tv_role);
//        profile = view.findViewById(R.id.tv_profile);
        buttonLogout = view.findViewById(R.id.btn_logout);

        buttonLogout.setOnClickListener(view1 -> {
            Helper.doLogout(requireActivity());
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("User").document(currentid);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    String UserName = task.getResult().getString("UserName");
                    String UserEmail = task.getResult().getString("UserEmail");
                    String role = task.getResult().getString("role");
                    username.setText(UserName);
                    email.setText(UserEmail);
                    Role.setText(role);
//                    profile.setText(role);

                } else {

                }
            }
        });
    }
}