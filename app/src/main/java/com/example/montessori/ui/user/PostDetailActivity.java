package com.example.montessori.ui.user;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.ui.ImagePreviewActivity;
import com.example.montessori.util.Helper;
import com.example.montessori.util.IntentNameExtra;
import com.example.montessori.util.ReferenceConstant;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postReference = database.collection(ReferenceConstant.ALL_POSTS);
    private final CollectionReference likeReference = database.collection(ReferenceConstant.LIKE);

    private TaskCompletionSource<PostMember> postSource = new TaskCompletionSource<>();
    private TaskCompletionSource<List<String>> likeSource = new TaskCompletionSource<>();

    // private TaskCompletionSource<PostMember> commentSource = new TaskCompletionSource<>();

    private PostMember postData;
    // NOTE: Untuk simpan state mengenai apakah postingan sudah dilike atau belum oleh pengguna.
    private boolean isPostLiked = false;

    private SwipeRefreshLayout swipeLayout;
    private RelativeLayout container;
    private LinearLayout statusContainer, likeContainer, experienceContainer, commentContainer;
    private ProgressBar progressBar;
    private ImageView ivPost, ivLike;
    private TextView tvTitle, tvName, tvCategory, tvLikeCount, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postData = getIntent().getParcelableExtra(IntentNameExtra.POST_DATA);

        if (postData == null) {
            Toast.makeText(this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        swipeLayout = findViewById(R.id.swipeLayout);
        container = findViewById(R.id.container);
        statusContainer = findViewById(R.id.notApprovedContainer);
        likeContainer = findViewById(R.id.likeContainer);
        experienceContainer = findViewById(R.id.experienceContainer);
        commentContainer = findViewById(R.id.commentContainer);
        progressBar = findViewById(R.id.progressBar);

        ivPost = findViewById(R.id.ivPost);
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvDesc = findViewById(R.id.tvDesc);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnDelete = findViewById(R.id.btnDelete);
        ivLike = findViewById(R.id.btnLike);
        Button btnShare = findViewById(R.id.btnShare);

        swipeLayout.setOnRefreshListener(this::loadData);
        statusContainer.setOnClickListener(view -> Toast.makeText(this, "The post hasn't approved by admin yet so the like and comment feature is still disabled.", Toast.LENGTH_LONG).show());

        btnBack.setOnClickListener(view -> finish());
        btnDelete.setVisibility(currentUser.getUid().equals(postData.getUid()) ? View.VISIBLE : View.GONE);
        // Harusnya ada pop-up konfirmasi
        // AlertDialog confirmation
        btnDelete.setOnClickListener(view -> {
            if (currentUser.getUid().equals(postData.getUid())) {
                deleteData(postData.getId());
                finish();
            }
        });

        likeContainer.setOnClickListener(view -> doLikePost());
        btnShare.setOnClickListener(view -> startActivity(new Intent(PostDetailActivity.this, ShareActivity.class)));

        loadData();
    }

    private void doLikePost() {
        Map<String, Object> updates = new HashMap<>();
        if (isPostLiked) {
            updates.put(currentUser.getUid(), FieldValue.delete());

            likeReference.document(postData.getId()).update(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isPostLiked = false;
                    toggleLikeButton();
                    loadLikes(true);
                } else {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updates.put(currentUser.getUid(), true);

            likeReference.document(postData.getId()).update(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isPostLiked = true;
                    toggleLikeButton();
                    loadLikes(true);
                } else {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setPostData(PostMember postData) {
        if (postData == null) {
            Toast.makeText(this, "Error occurred when load post data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        this.postData = postData;

        tvTitle.setText(postData.getPostTitle());
        tvName.setText(postData.getTitleName(this));
        tvCategory.setText(postData.getCategory(this));
        Glide.with(this)
                .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                .load(postData.getPostUri())
                .into(ivPost);
        tvDesc.setText(postData.getDesc());

        ivPost.setOnClickListener(view -> {
            Intent imageIntent = new Intent(this, ImagePreviewActivity.class);
            imageIntent.putExtra(IntentNameExtra.IMAGE_URL_DATA, postData.getPostUri());
            startActivity(imageIntent);
        });

        toggleAdditionalVisibility(!postData.isNotCheckedOrApproved());
    }

    private void deleteData(String postId) {
        Task<Void> postTask = postReference.document(postId).delete();
        Task<Void> likeTask = likeReference.document(postId).delete();

        Tasks.whenAll(postTask, likeTask).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(PostDetailActivity.this, "Error occurred when deleting the post.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PostDetailActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadData() {
        toggleLoadingVisibility(true);
        // NOTE: Ketika post belum dicek oleh admin atau tidak diterima, ambil data postingan saja.
        if (postData.isNotCheckedOrApproved()) {
            loadPostDetail(true);
            return;
        }

        init();

        Tasks.whenAll(postSource.getTask(), likeSource.getTask()).addOnCompleteListener(task -> {
            if (postSource.getTask().isSuccessful() && postSource.getTask().getResult() != null) {
                setPostData(postSource.getTask().getResult());
            } else {
                Toast.makeText(this, "Error occurred when load post data", Toast.LENGTH_SHORT).show();
            }

            if (likeSource.getTask().isSuccessful() && likeSource.getTask().getResult() != null) {
                List<String> userLikes = likeSource.getTask().getResult();
                for (String userId : userLikes) {
                    if (currentUser.getUid().equals(userId)) {
                        isPostLiked = true;
                        toggleLikeButton();
                        break;
                    }
                }
                tvLikeCount.setText(Helper.getTotalLikes(PostDetailActivity.this, userLikes.size()));
            } else {
                Toast.makeText(this, "Error occurred when load like data", Toast.LENGTH_SHORT).show();
            }

            toggleLoadingVisibility(false);
        });


        loadPostDetail(false);
        loadLikes(false);
        // loadComments();
    }

    // NOTE: isSingleLoading di loadPostDetail untuk membedakan postingan sudah diapprove/belum sehingga tidak perlu mengambil semua data
    // Ada kemungkinan nanti dihapus
    private void loadPostDetail(boolean isSingleLoading) {
        postReference.document(postData.getId()).get().addOnCompleteListener(task -> {
            toggleLoadingVisibility(false);
            if (task.getResult() != null && task.getResult().exists()) {
                PostMember data = task.getResult().toObject(PostMember.class);
                if (!isSingleLoading) {
                    postSource.trySetResult(data);
                } else {
                    setPostData(data);
                }
            } else {
                if (!isSingleLoading) {
                    postSource.trySetException(new Exception("Error occurred"));
                } else {
                    setPostData(null);
                }
            }
        });
    }

    // NOTE: isSingleLoading di loadLikes untuk update jumlah like ketika pengguna melakukan like/dislike pada postingan.
    private void loadLikes(boolean isSingleLoading) {
        likeReference.document(postData.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists() && task.getResult().getData() != null) {
                Map<String, Object> likes = task.getResult().getData();
                ArrayList<String> userLikes = new ArrayList<>();
                for (Map.Entry<String, Object> user : likes.entrySet()) {
                    userLikes.add(user.getKey());
                }
                if (!isSingleLoading) {
                    likeSource.trySetResult(userLikes);
                } else {
                    tvLikeCount.setText(Helper.getTotalLikes(PostDetailActivity.this, userLikes.size()));
                }
            } else {
                if (!isSingleLoading) {
                    likeSource.trySetException(new Exception("Get like fail"));
                } else {
                    Toast.makeText(this, "Error occurred when load like data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadComments() {

    }

    private void init() {
        if (postSource.getTask().isComplete()) {
            postSource = new TaskCompletionSource<>();
        }

        if (likeSource.getTask().isComplete()) {
            likeSource = new TaskCompletionSource<>();
        }
    }

    private void toggleLoadingVisibility(boolean isLoadingVisible) {
        container.setVisibility(isLoadingVisible ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(isLoadingVisible ? View.VISIBLE : View.GONE);
        if (swipeLayout.isRefreshing() && !isLoadingVisible) {
            swipeLayout.setRefreshing(false);
        }
        statusContainer.setVisibility(View.GONE);
    }

    private void toggleLikeButton() {
        Drawable drawable;
        if (isPostLiked) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ic_liked);
        } else {
            drawable = ContextCompat.getDrawable(this, R.drawable.ic_like);
        }

        ivLike.setImageDrawable(drawable);
    }

    private void toggleAdditionalVisibility(boolean isVisible) {
        statusContainer.setVisibility(!isVisible ? View.VISIBLE : View.GONE);
        likeContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        experienceContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        commentContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}