package com.example.montessori.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.PostAdapter;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.Constants;
import com.example.montessori.util.Helper;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private PostAdapter adapter;
    private RecyclerView rvPost;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference reference = database.collection(ReferenceConstant.ALL_IMAGES);
    private ImageView practical, math, language, sensorial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPost = requireActivity().findViewById(R.id.rvPost);
        practical = requireActivity().findViewById(R.id.practical);
        math = requireActivity().findViewById(R.id.math);
        language = requireActivity().findViewById(R.id.language);
        sensorial = requireActivity().findViewById(R.id.sensorial);
        practical.setOnClickListener(view1 -> {
            Helper.Category(Constants.CATEGORY_PRACTICAL, requireActivity());
        });
        math.setOnClickListener(view1 -> {
            Helper.Category(Constants.CATEGORY_MATH, requireActivity());
        });
        language.setOnClickListener(view1 -> {
            Helper.Category(Constants.CATEGORY_LANGUAGE, requireActivity());
        });
        sensorial.setOnClickListener(view1 -> {
            Helper.Category(Constants.CATEGORY_SENSORIAL, requireActivity());
        });

        adapter = new PostAdapter(requireContext());

        rvPost.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPost.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (currentUser != null) {
            reference.whereNotEqualTo(Constants.UID_FIELD, currentUser.getUid()).addSnapshotListener((value, error) -> {
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