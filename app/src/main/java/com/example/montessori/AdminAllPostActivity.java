package com.example.montessori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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
    private RecyclerView rvPost;
    private PostAdapter adapter;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference reference = database.collection(ReferenceConstant.ALL_IMAGES);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_post);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onPostCreate(view, savedInstanceState);
////        life = requireActivity().findViewById(R.id.life);
////        life.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                startActivity(new Intent(HomeFragment.this, DetailCategoryActivity.class));
////            }
////        });
//        rvPost = requireActivity().findViewById(R.id.rvPost);
//
//        adapter = new PostAdapter(requireContext());
//
//        rvPost.setLayoutManager(new LinearLayoutManager(requireContext()));
//        rvPost.setAdapter(adapter);
//    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (currentUser != null) {
            reference.addSnapshotListener((value, error) -> {
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