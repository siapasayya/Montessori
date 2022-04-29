package com.example.montessori.ui.user;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.PostAdapter;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.IntentNameExtra;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DetailCategoryActivity extends AppCompatActivity {
    private RecyclerView rvPost;

    private PostAdapter adapter;
    private ImageButton imageButton;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);
        rvPost = findViewById(R.id.rvPost);

        imageButton = findViewById(R.id.back);
        imageButton.setOnClickListener(view -> finish());

        adapter = new PostAdapter(this);

        rvPost.setLayoutManager(new LinearLayoutManager(this));
        rvPost.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        String category = getIntent().getStringExtra(IntentNameExtra.CATEGORY_DATA);
        loadData(category);
    }

    private void loadData(String category) {
        if (currentUser != null) {
            postReference.whereEqualTo("pem", category).addSnapshotListener((value, error) -> {
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