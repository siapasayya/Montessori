package com.example.montessori.ui.user;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.IntentNameExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PostDetailActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private PostMember postData;
    private ImageView ivPost;
    private TextView tvDesc;
    private TextView tvPem;
    private TextView tvUmur;
    private ImageButton imageButton;

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
        tvPem = findViewById(R.id.tvPem);
        tvUmur = findViewById(R.id.tvUmur);
        tvDesc = findViewById(R.id.tvDesc);
        imageButton = findViewById(R.id.back);

        imageButton.setOnClickListener(view -> finish());

        Glide.with(this).load(postData.getPostUri()).into(ivPost);
        tvDesc.setText(postData.getDesc());
        tvUmur.setText(postData.getUmur());
        tvPem.setText(postData.getPem());
    }
}