package com.example.montessori.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.util.IntentNameExtra;
import com.example.montessori.util.ReferenceConstant;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class AdminApprovalActivity extends AppCompatActivity {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);

    private ScrollView container;
    private LinearLayout llApproval;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approval);

        PostMember postData = getIntent().getParcelableExtra(IntentNameExtra.POST_DATA);

        if (postData == null) {
            Toast.makeText(this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        container = findViewById(R.id.container);
        llApproval = findViewById(R.id.llApproval);
        progressBar = findViewById(R.id.progressBar);

        ImageView ivPost = findViewById(R.id.ivPost);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvUsername = findViewById(R.id.tv_username);
        TextView tvPem = findViewById(R.id.tvPem);
        TextView tvUmur = findViewById(R.id.tvUmur);
        TextView tvDesc = findViewById(R.id.tvDesc);

        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialButton btnReject = findViewById(R.id.btn_tolak);
        MaterialButton btnApprove = findViewById(R.id.btn_setuju);

        btnBack.setOnClickListener(view -> finish());
        btnReject.setOnClickListener(view -> updateApprovalData(postData, false));
        btnApprove.setOnClickListener(view -> updateApprovalData(postData, true));

        Glide.with(this).load(postData.getPostUri()).into(ivPost);
        tvUsername.setText(postData.getFullUsername());
        tvName.setText(postData.getFullName());
        tvDesc.setText(postData.getDesc());
        tvUmur.setText(postData.getUmur());
        tvPem.setText(postData.getPem());
    }

    private void updateApprovalData(PostMember postData, boolean isApproved) {
        toggleLoadingVisibility(true);

        postData.setChecked(true);
        postData.setApproved(isApproved);

        postReference.document(postData.getId()).set(postData).addOnCompleteListener(task -> {
            toggleLoadingVisibility(false);
            if (task.isSuccessful()) {
                showApprovalToast(postData);
            } else {
                Toast.makeText(this, "Error occurred when submitting data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showApprovalToast(PostMember data) {
        String message = "";
        if (data.isApproved()) {
            message = String.format(new Locale("id", "ID"), getString(R.string.post_approved_message), data.getFullName());
        } else {
            message = String.format(new Locale("id", "ID"), getString(R.string.post_rejected_message), data.getFullName());
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void toggleLoadingVisibility(boolean isLoadingVisible) {
        container.setVisibility(isLoadingVisible ? View.GONE : View.VISIBLE);
        llApproval.setVisibility(isLoadingVisible ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isLoadingVisible ? View.VISIBLE : View.GONE);
    }
}