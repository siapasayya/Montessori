package com.example.montessori.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.PostAdapter;
import com.example.montessori.model.PostMember;
import com.example.montessori.model.User;
import com.example.montessori.util.Constants;
import com.example.montessori.util.Helper;
import com.example.montessori.util.ReferenceConstant;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_IMAGES);
    private final CollectionReference userReference = database.collection(ReferenceConstant.USERS);
    private PostAdapter adapter;

    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvRole;
    private RecyclerView rvPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvRole = view.findViewById(R.id.tv_role);
        rvPost = view.findViewById(R.id.rvPost);

        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> Helper.doLogout(requireActivity()));

        adapter = new PostAdapter(requireContext());

        rvPost.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (currentUser != null) {
            userReference.document(currentUser.getUid()).addSnapshotListener((value, error) -> {
                if (value != null && value.exists()) {
                    User data = value.toObject(User.class);
                    if (data != null) {
                        tvUsername.setText(data.getUserName());
                        tvEmail.setText(data.getUserEmail());
                        tvRole.setText(data.getRole());
                    }
                } else {
                    Toast.makeText(requireContext(), "Error occurred when load user profile", Toast.LENGTH_SHORT).show();
                }
            });

            postReference.whereEqualTo(Constants.UID_FIELD, currentUser.getUid()).addSnapshotListener((value, error) -> {
                if (value != null) {
                    ArrayList<PostMember> posts = new ArrayList<>();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        if (document.exists()) {
                            PostMember data = document.toObject(PostMember.class);
                            if (data != null) {
                                posts.add(data);
                            }
                        }
                    }
                    adapter.setList(posts);
                    rvPost.setAdapter(adapter);
                }
            });
        }
    }
}