package com.example.montessori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    ImageView imageView;
    private Uri selectedUri;
    private static final int PICK_FILE = 1;
    UploadTask uploadTask;
    EditText etdesc;
    VideoView videoView;
    Button BtnChoose, BtnUpload;
    String name,url;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1,db2,db3;

    MediaController mediaController;
    String type;

    Postmember postmember;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postmember = new Postmember();
        mediaController = new MediaController(this);

        imageView = findViewById(R.id.img_post);
        videoView = findViewById(R.id.vid_post);
        BtnChoose = findViewById(R.id.btn_choose_post);
        BtnUpload = findViewById(R.id.btn_uploadfile_post);
        etdesc = findViewById(R.id.et_desc_post);


        storageReference = FirebaseStorage.getInstance().getReference("User Posts");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        db1 = database.getReference("All Images").child(currentuid);
        db2 = database.getReference("All Videos").child(currentuid);
        db3 = database.getReference("All Post");
        
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

        if (requestCode == PICK_FILE || requestCode == RESULT_OK ||

        data != null || data.getData() != null) {
            selectedUri = data.getData();
            if (selectedUri.toString().contains("images")) {
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            }else if (selectedUri.toString().contains("video")) {
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = "vv";
            }else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private String getFileExt (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("User").document(currentuid);

        documentReference.get()
                .addOnCompleteListener((task) -> {
                    if (task.getResult().exists()) {
                        name = task.getResult().getString("Name");
                    }else {
                        Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void Dopost(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        String desc = etdesc.getText().toString();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentdate.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate +":"+ savetime;

        if (TextUtils.isEmpty(desc) || selectedUri != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "." +getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);

            Task<Uri> uriTask = uploadTask.continueWithTask((task) -> {
                if (!task.isSuccessful()) {
                    throw  task.getException();
                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();

                        if (type.equals("iv")){
                            postmember.setDesc(desc);
                            postmember.setName(name);
                            postmember.setPostUri(downloadUrl.toString());
                            postmember.setTime(time);
                            postmember.setUid(currentuid);
                            postmember.setUrl(url);
                            postmember.setType("iv");

                            //for image
                            String id = db1.push().getKey();
                            db1.child(id).setValue(postmember);
                            //for both
                            String id1 = db3.push().getKey();
                            db3.child(id1).setValue(postmember);

                            Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();

                        }else if (type.equals("vv")){
                            postmember.setDesc(desc);
                            postmember.setName(name);
                            postmember.setPostUri(downloadUrl.toString());
                            postmember.setTime(time);
                            postmember.setUid(currentuid);
                            postmember.setUrl(url);
                            postmember.setType("vv");

                            //for videos
                            String id3 = db2.push().getKey();
                            db1.child(id3).setValue(postmember);
                            //for both
                            String id4 = db3.push().getKey();
                            db3.child(id4).setValue(postmember);

                            Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(PostActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


        }else {
            Toast.makeText(this, "Please fill all fieald", Toast.LENGTH_SHORT).show();
        }

    }
}