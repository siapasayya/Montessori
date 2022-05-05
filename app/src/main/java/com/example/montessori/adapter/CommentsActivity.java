package com.example.montessori.adapter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentsActivity extends AppCompatActivity {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        EditText etComment = findViewById(R.id.et_comment);
        Button btnComment = findViewById(R.id.btn_comment);
        RecyclerView rvComment = findViewById(R.id.rvComment);

        rvComment.setHasFixedSize(true);
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        btnComment.setOnClickListener(view -> {
            String comment = etComment.getText().toString();
            if (comment.isEmpty()) {
                Toast.makeText(CommentsActivity.this, "Silahkan tulis komentar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}