package com.example.montessori.ui.user;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.model.User;
import com.example.montessori.util.Constants;
import com.example.montessori.util.DateTimeFormat;
import com.example.montessori.util.Helper;
import com.example.montessori.util.ReferenceConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    private static final int PICK_FILE = 1;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = auth.getCurrentUser();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference postDatabase = database.collection(ReferenceConstant.ALL_POSTS);
    private final CollectionReference userDatabase = database.collection(ReferenceConstant.USERS);
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference(ReferenceConstant.USER_POSTS);

    private ImageView imagePreview;
    private EditText etDesc;
    private ProgressBar progressBar;
    private LinearLayout container;
    private Spinner spinnerPem, spinnerUmur;

    private Uri selectedUri;

    private String currentUid;

    private final PostMember post = new PostMember();
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progressBar);

        spinnerPem = findViewById(R.id.spinnerPem);
        spinnerUmur = findViewById(R.id.spinnerUmur);
        etDesc = findViewById(R.id.et_desc_post);

        imagePreview = findViewById(R.id.img_post);
        Button btnChoose = findViewById(R.id.btn_choose_post);
        Button btnUpload = findViewById(R.id.btn_uploadfile_post);
        ImageButton btnBack = findViewById(R.id.btnBack);

        if (currentUser != null) {
            currentUid = currentUser.getUid();
        } else {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpload.setOnClickListener(view -> {
            try {
                uploadPost();
            } catch (Exception e) {
                Toast.makeText(this, "Error occurred when uploading.", Toast.LENGTH_SHORT).show();
                Log.e("PostActivity", "Error occurred: " + e.getLocalizedMessage());
            }
        });

        btnChoose.setOnClickListener(view -> chooseImage());
        btnBack.setOnClickListener(view -> finish());

        loadData();
    }

    private void uploadPost() {
        String categoryPem = spinnerPem.getSelectedItem().toString();
        String categoryAge = spinnerUmur.getSelectedItem().toString();
        String desc = etDesc.getText().toString();

        String time = Helper.convertToFormattedDate(
                DateTimeFormat.DEFAULT_DATETIME_FORMAT, Calendar.getInstance().getTime()
        );

        post.setId(Helper.generateId(userData.getUserName()));
        post.setName(userData.getFullName());
        post.setUsername(userData.getUserName());
        post.setUid(currentUid);
        post.setDesc(desc);
        post.setPem(categoryPem);
        post.setUmur(categoryAge);
        post.setApproved(!Helper.isNullOrBlank(userData.getRole()) && userData.getRole().equals(Constants.ROLE_PROFESSIONAL));
        post.setChecked(!Helper.isNullOrBlank(userData.getRole()) && userData.getRole().equals(Constants.ROLE_PROFESSIONAL));
        post.setTime(time);

        if (post.isDataFilled() && selectedUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
            UploadTask uploadTask = reference.putFile(selectedUri);

            uploadTask.continueWithTask((task) -> {
                if (!task.isSuccessful() && task.getException() != null) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful() & task.getResult() != null) {
                    Uri downloadUrl = task.getResult();
                    progressBar.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);

                    if (!Helper.isNullOrBlank(post.getType()) && post.getType().equals(Constants.IMAGE_TYPE)) {
                        post.setPostUri(downloadUrl.toString());

                        postDatabase.document(post.getId()).set(post);

                        Toast.makeText(PostActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Error: Unknown Type", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please fill all field", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        if (currentUser != null) {
            userDatabase.document(currentUser.getUid()).addSnapshotListener((value, error) -> {
                if (value != null && value.exists()) {
                    User data = value.toObject(User.class);
                    if (data != null) {
                        userData = data;
                    }
                } else {
                    Toast.makeText(this, "Error occurred when load user profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedUri = data.getData();
            if (selectedUri.toString().contains("images")) {
                Picasso.get().load(selectedUri).into(imagePreview);
                imagePreview.setVisibility(View.VISIBLE);
                post.setType(Constants.IMAGE_TYPE);
            } else {
                post.setType("");
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }
}