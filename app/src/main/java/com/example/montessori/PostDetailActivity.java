package com.example.montessori;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.IntentNameExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PostDetailActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private PostMember postData;
    // private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    // private final CollectionReference reference = database.collection(ReferenceConstant.ALL_IMAGES);
    private ImageView ivPost;
    private TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postData = getIntent().getParcelableExtra(IntentNameExtra.POST_DATA);

        if (postData == null) {
            Toast.makeText(this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }

        ivPost = findViewById(R.id.ivPost);
        tvDesc = findViewById(R.id.tvDesc);

        Glide.with(this).load(postData.getPostUri()).into(ivPost);
        tvDesc.setText(postData.getDesc());
    }
}