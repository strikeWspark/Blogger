package com.example.r.blogger.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.r.blogger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostList extends AppCompatActivity {

    private ImageButton mPostImage;
    private TextInputLayout title_input;
    private TextInputEditText title_text;
    private EditText post_desc;
    private MaterialButton uploadbutton;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mPostDatabase;
    private FirebaseDatabase mDatabase;
    private ProgressDialog mProgress;
    private Uri mimageUri;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_list);


        mProgress = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance();
        mPostDatabase = mDatabase.getReference().child("MBlog");
        Toast.makeText(getApplicationContext(),"Database referenced",Toast.LENGTH_LONG).show();
        mPostDatabase.setValue("dryfire");



        mPostImage = (ImageButton) findViewById(R.id.blg_add_image_button);
        title_input = (TextInputLayout) findViewById(R.id.blg_add_post_input);
        title_text = (TextInputEditText) findViewById(R.id.blg_add_post_text);
        post_desc = (EditText) findViewById(R.id.blg_post_desc);
        uploadbutton = (MaterialButton) findViewById(R.id.blg_upload_button);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Posting to our database
                startPosting();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null ){
            mimageUri = data.getData();
            mPostImage.setImageURI(mimageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPosting() {

        mProgress.setMessage("Posting to Blog...");
        mProgress.show();

        final String titleVal = title_text.getText().toString().trim();
        final String descVal = post_desc.getText().toString().trim();

        Log.d("tilte",titleVal);
        Log.d("desc",descVal);



        if(!TextUtils.isEmpty(titleVal ) && !TextUtils.isEmpty(descVal) && mimageUri != null) {

            //Start the uploading...
            Log.d("posting","to blog");

            StorageReference filepath = mStorage.child("MBlog_images").child(mimageUri.getLastPathSegment());
            Log.d("third","line");
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    DatabaseReference newPost = mPostDatabase.push();

                   Map<String,String> dataToSave = new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("description",descVal);
                    dataToSave.put("timeStamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userID",mUser.getEmail());

                    //newPost.child("title").setValue(titleVal);
                    //newPost.child("description").setValue(descVal);

                    newPost.setValue(dataToSave);

                    mProgress.dismiss();

                    startActivity(new Intent(AddPostList.this,PostListActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        }

    }
}
