package com.example.montessori.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.montessori.R;
import com.example.montessori.SearchActivity;
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
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);
    private PostAdapter adapter;

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PostAdapter(requireContext());

        swipeLayout = view.findViewById(R.id.swipeLayout);
        rvPost = view.findViewById(R.id.rvPost);

        ImageView btnPractical = view.findViewById(R.id.practical);
        ImageView btnMath = view.findViewById(R.id.math);
        ImageView btnLanguage = view.findViewById(R.id.language);
        ImageView btnSensorial = view.findViewById(R.id.sensorial);
        ImageView btnMini = view.findViewById(R.id.mini);
        ImageView btnSmall = view.findViewById(R.id.kecil);
        ImageView btnMedium = view.findViewById(R.id.sedang);
        ImageView btnLarge = view.findViewById(R.id.besar);
        ImageButton btnSearch = view.findViewById(R.id.ib_search);

        swipeLayout.setOnRefreshListener(() -> loadData());
        rvPost.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnPractical.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_PRACTICAL, true));
        btnMath.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_MATH, true));
        btnLanguage.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_LANGUAGE, true));
        btnSensorial.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_SENSORIAL, true));

        btnMini.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_PRESCHOOL, false));
        btnSmall.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_JUNIOR, false));
        btnMedium.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_HIGH_SCHOOL, false));
        btnLarge.setOnClickListener(v -> Helper.openCategoryPage(requireActivity(), Constants.CATEGORY_ADULT, false));

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (currentUser != null) {
            postReference.whereNotEqualTo(Constants.UID_FIELD, currentUser.getUid()).addSnapshotListener((value, error) -> {
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
                swipeLayout.setRefreshing(false);
            });
        }
    }
}