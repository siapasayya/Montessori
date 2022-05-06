package com.example.montessori.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.adapter.CommentAdapter;
import com.example.montessori.model.Comment;
import com.example.montessori.model.User;
import com.example.montessori.util.DateTimeFormat;
import com.example.montessori.util.Helper;
import com.example.montessori.util.IntentNameExtra;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference userReference = database.collection(ReferenceConstant.USERS);
    private final CollectionReference commentsReference = database.collection(ReferenceConstant.COMMENTS);
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseUser currentUser;
    private String postId;
    private User userData;
    private final ArrayList<Comment> commentsList = new ArrayList<>();

    private RecyclerView rvComment;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        adapter = new CommentAdapter(this);

        postId = getIntent().getStringExtra(IntentNameExtra.POST_ID_DATA);

        EditText etComment = findViewById(R.id.et_comment);
        Button btnComment = findViewById(R.id.btn_comment);
        ImageButton btnBack = findViewById(R.id.btnBack);
        rvComment = findViewById(R.id.rvComment);

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User isn't logged in!", Toast.LENGTH_SHORT).show();
            Helper.doLogout(this);
            return;
        }

        if (postId == null) {
            Toast.makeText(this, "There's no ID post given.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser = auth.getCurrentUser();

        rvComment.setHasFixedSize(true);
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(view -> finish());
        btnComment.setOnClickListener(view -> {
            String comment = etComment.getText().toString();
            if (comment.isEmpty()) {
                Toast.makeText(CommentsActivity.this, "Please write a comment first.", Toast.LENGTH_SHORT).show();
            } else {
                submitComment(comment);
            }
        });

        loadUserData();
        loadComments();
    }

    private void submitComment(String comment) {
        if (userData == null) {
            Toast.makeText(this, "User profile is not loaded.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> commentData = new HashMap<>();
        commentData.put(Comment.COMMENT_FIELD, comment);
        commentData.put(Comment.NAME_FIELD, userData.getFullName());
        // Nanti sesuaikan jadi UTC
        commentData.put(Comment.TIMESTAMP_FIELD, System.currentTimeMillis());
        commentData.put(Comment.DATE_FIELD, Helper.convertToFormattedDate(
                DateTimeFormat.DEFAULT_DATETIME_FORMAT, Calendar.getInstance().getTime()
        ));

        data.put(Helper.generateId(userData.getUserName()), commentData);

        commentsReference.document(postId).update(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add comment. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments() {
        commentsReference.document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists() && task.getResult().getData() != null) {
                commentsList.clear();
                Map<String, Object> comments = task.getResult().getData();
                for (Map.Entry<String, Object> comment : comments.entrySet()) {
                    Comment data = new Comment();
                    HashMap<String, Object> commentData = (HashMap<String, Object>) comment.getValue();
                    data.setComment((String) commentData.get(Comment.COMMENT_FIELD));
                    data.setName((String) commentData.get(Comment.NAME_FIELD));
                    data.setDate((String) commentData.get(Comment.DATE_FIELD));
                    data.setTimestamp((Long) commentData.get(Comment.TIMESTAMP_FIELD));
                    commentsList.add(data);
                }
                adapter.setList(commentsList);
                rvComment.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Error occurred when load comment data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            userReference.document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.getResult() != null && task.getResult().exists()) {
                    User data = task.getResult().toObject(User.class);
                    if (data != null) {
                        userData = data;
                    }
                } else {
                    Toast.makeText(this, "Error occurred when load user profile", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}