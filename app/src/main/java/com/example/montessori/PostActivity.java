package com.example.montessori;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.montessori.model.PostMember;
import com.example.montessori.util.Constants;
import com.example.montessori.util.DateTimeFormat;
import com.example.montessori.util.ReferenceConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    private ImageView imageView;
    private Uri selectedUri;
    private static final int PICK_FILE = 1;
    private UploadTask uploadTask;
    private EditText etdesc;
    private VideoView videoView;
    private Button BtnChoose, BtnUpload;
    private ProgressBar progressBar;
    private ConstraintLayout container;
    private Spinner spinnerPem, SpinnerUmur;
    String name, url;
    StorageReference storageReference;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference imageDatabase, videoDatabase, allDatabase;

    MediaController mediaController;
    String type;

    PostMember post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post = new PostMember();
        mediaController = new MediaController(this);

        imageView = findViewById(R.id.img_post);
        videoView = findViewById(R.id.vid_post);
        BtnChoose = findViewById(R.id.btn_choose_post);
        BtnUpload = findViewById(R.id.btn_uploadfile_post);
        etdesc = findViewById(R.id.et_desc_post);
        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.container);
        spinnerPem = findViewById(R.id.spinnerPem);
        SpinnerUmur = findViewById(R.id.spinnerUmur);

        storageReference = FirebaseStorage.getInstance().getReference(ReferenceConstant.USER_POSTS);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        imageDatabase = firestore.collection(ReferenceConstant.ALL_IMAGES);
        videoDatabase = firestore.collection(ReferenceConstant.ALL_VIDEOS);
        allDatabase = firestore.collection(ReferenceConstant.ALL_POSTS);

        /*imageDatabase = database.getReference(ReferenceConstant.ALL_IMAGES).child(currentUid);
        videoDatabase = database.getReference(ReferenceConstant.ALL_VIDEOS).child(currentUid);
        allDatabase = database.getReference(ReferenceConstant.ALL_POSTS);*/

        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dopost();
            }
        });

        BtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE || requestCode == RESULT_OK || data != null || data.getData() != null) {
            selectedUri = data.getData();
            if (selectedUri.toString().contains("images")) {
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = Constants.IMAGE_TYPE;
            } else if (selectedUri.toString().contains("video")) {
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = Constants.VIDEO_TYPE;
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        DocumentReference documentReference = firestore.collection("User").document(currentuid);

        documentReference.get()
                .addOnCompleteListener((task) -> {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("Name");
                    } else {
                        Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void Dopost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        String pembelajaran = spinnerPem.getSelectedItem().toString();
        String umur = SpinnerUmur.getSelectedItem().toString();

        String desc = etdesc.getText().toString();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat(DateTimeFormat.DEFAULT_DATE_FORMAT);
        final String savedate = currentdate.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat(DateTimeFormat.DEFAULT_TIME_FORMAT);
        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        if (TextUtils.isEmpty(desc) || selectedUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);

            post.setDesc(desc);
            post.setName(name);
            post.setTime(time);
            post.setUid(currentuid);
            // TODO: Rapikan URL.
            post.setUrl(url);
            post.setType(type);
            post.setPem(pembelajaran);
            post.setUmur(umur);

            Task<Uri> uriTask = uploadTask.continueWithTask((task) -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful() & task.getResult() != null) {
                        Uri downloadUrl = task.getResult();
                        progressBar.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);

                        if (type.equals(Constants.IMAGE_TYPE)) {
                            post.setPostUri(downloadUrl.toString());

                            //for image
                            imageDatabase.add(post);
                            /*String id = imageDatabase.push().getKey();
                            imageDatabase.child(id).setValue(post);*/
                            //for both
                            allDatabase.add(post);
                            /*String id1 = allDatabase.push().getKey();
                            allDatabase.child(id1).setValue(post);*/

                            Toast.makeText(PostActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, UserDashboardActivity.class));
                            finish();

                        } else if (type.equals(Constants.VIDEO_TYPE)) {
                            post.setPostUri(downloadUrl.toString());

                            //for videos
                            videoDatabase.add(post);
                            /*String id3 = videoDatabase.push().getKey();
                            videoDatabase.child(id3).setValue(post);*/
                            //for both
                            allDatabase.add(post);
                            /*String id4 = allDatabase.push().getKey();
                            allDatabase.child(id4).setValue(post);*/
                            Toast.makeText(PostActivity.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PostActivity.this, "Error: Unknown Type", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please fill all field", Toast.LENGTH_SHORT).show();
        }
    }
}