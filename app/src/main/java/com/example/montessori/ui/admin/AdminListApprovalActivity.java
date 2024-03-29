package com.example.montessori.ui.admin;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.PostApprovalAdapter;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.Constants;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminListApprovalActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);
    private PostApprovalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_approval);

        RecyclerView rvPost = findViewById(R.id.rvPost);
        ImageButton imageButton = findViewById(R.id.btnBack);

        adapter = new PostApprovalAdapter(this);

        rvPost.setLayoutManager(new LinearLayoutManager(this));
        rvPost.setAdapter(adapter);

        imageButton.setOnClickListener(view -> finish());

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (currentUser != null) {
            postReference.whereEqualTo(Constants.CHECKED_FIELD, false).addSnapshotListener((value, error) -> {
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
                }
            });
        }
    }
}