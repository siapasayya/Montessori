package com.example.montessori.ui.admin;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.PostAdapter;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminAllPostActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_post);

        RecyclerView rvPost = findViewById(R.id.rvPost);
        ImageButton imageButton = findViewById(R.id.btnBack);

        adapter = new PostAdapter(this);

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
            postReference.addSnapshotListener((value, error) -> {
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

    private void deleteData(String documentId) {
        postReference.document().delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminAllPostActivity.this, "Data gagal di hapus", Toast.LENGTH_SHORT).show();
            }
            loadData();
        });
    }
}