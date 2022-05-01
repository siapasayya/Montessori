package com.example.montessori.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.IntentNameExtra;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostDetailActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        PostMember postData = getIntent().getParcelableExtra(IntentNameExtra.POST_DATA);

        if (postData == null) {
            Toast.makeText(this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView ivPost = findViewById(R.id.ivPost);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvUsername = findViewById(R.id.tv_username);
        TextView tvPem = findViewById(R.id.tvPem);
        TextView tvUmur = findViewById(R.id.tvUmur);
        TextView tvDesc = findViewById(R.id.tvDesc);

        ImageButton imgBtnBack = findViewById(R.id.btnBack);
        ImageButton imgBtnDelete = findViewById(R.id.btnDelete);
        Button btn_share = findViewById(R.id.share);

        imgBtnBack.setOnClickListener(view -> finish());
        imgBtnDelete.setOnClickListener(view -> {
            if (currentUser.getUid().equals(postData.getUid())) {
                deleteData(postData.getId());
                finish();
            }
        });
        imgBtnDelete.setVisibility(currentUser.getUid().equals(postData.getUid()) ? View.VISIBLE : View.GONE);
        btn_share.setOnClickListener(view -> startActivity(new Intent(PostDetailActivity.this, ShareActivity.class)));

        Glide.with(this).load(postData.getPostUri()).into(ivPost);
        tvUsername.setText(postData.getFullUsername());
        tvName.setText(postData.getFullName());
        tvDesc.setText(postData.getDesc());
        tvUmur.setText(postData.getUmur());
        tvPem.setText(postData.getPem());
    }

    private void deleteData(String documentId) {
        postReference.document(documentId).delete().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(PostDetailActivity.this, "Data gagal di hapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}