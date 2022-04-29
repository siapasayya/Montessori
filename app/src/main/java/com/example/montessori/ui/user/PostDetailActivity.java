package com.example.montessori.ui.user;

import android.content.Intent;
import android.os.Bundle;
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

public class PostDetailActivity extends AppCompatActivity {
    private PostMember postData;
    private ImageView ivPost;
    private TextView tvDesc;
    private TextView tvPem;
    private TextView tvUmur;
    private TextView tvName;
    private TextView tvUsername;
    private ImageButton imageButton;
    private Button btn_share;

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
        tvName = findViewById(R.id.tv_name);
        tvUsername = findViewById(R.id.tv_username);
        tvPem = findViewById(R.id.tvPem);
        tvUmur = findViewById(R.id.tvUmur);
        tvDesc = findViewById(R.id.tvDesc);

        imageButton = findViewById(R.id.btnBack);
        btn_share = findViewById(R.id.share);

        imageButton.setOnClickListener(view -> finish());
        btn_share.setOnClickListener(view -> startActivity(new Intent(PostDetailActivity.this, ShareActivity.class)));

        Glide.with(this).load(postData.getPostUri()).into(ivPost);
        tvUsername.setText(postData.getFullUsername());
        tvName.setText(postData.getFullName());
        tvDesc.setText(postData.getDesc());
        tvUmur.setText(postData.getUmur());
        tvPem.setText(postData.getPem());
    }
}