package com.example.r.blogger.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.r.blogger.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

public class CreateAccount extends AppCompatActivity {

    private Toolbar signup_toolbar;
    private TextInputLayout nameinput;
    private TextInputEditText nameedit;
    private TextInputLayout usernameinput;
    private TextInputEditText usernameedit;
    private TextInputLayout createpasswordinput;
    private TextInputEditText createpasswordedit;
    private MaterialButton createaccount;
    private DatabaseReference mDatabasereference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogressdialog;
    private AppCompatImageButton profilepic;
    private Uri resulturi = null;
    private final static int GALLERY_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blg_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabasereference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        mprogressdialog = new ProgressDialog(this);

        signup_toolbar = (Toolbar) findViewById(R.id.blg_signup_toolbar);
        setSupportActionBar(signup_toolbar);

        signup_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccount.this,MainActivity.class));
                finish();
            }
        });


        nameinput = (TextInputLayout) findViewById(R.id.blg_name_signupinput);
        nameedit = (TextInputEditText) findViewById(R.id.blg_name_signupedit);

        usernameinput = (TextInputLayout) findViewById(R.id.blg_username_signupinput);
        usernameedit = (TextInputEditText) findViewById(R.id.blg_username_signupedit);

        createpasswordinput = (TextInputLayout) findViewById(R.id.blg_password_signipinput);
        createpasswordedit = (TextInputEditText) findViewById(R.id.blg_password_signupedit);

        profilepic = (AppCompatImageButton) findViewById(R.id.blg_pro_pic);
        createaccount = (MaterialButton) findViewById(R.id.blg_signupButton);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {

        final String name = nameedit.getText().toString().trim();
        final String email = usernameedit.getText().toString().trim();
        String password = createpasswordedit.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mprogressdialog.setMessage("Creating Account...");
            mprogressdialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null && resulturi.getLastPathSegment() != null){
                                StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                                        .child(resulturi.getLastPathSegment());

                                imagePath.putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String userid = mAuth.getCurrentUser().getUid();

                                        DatabaseReference currenUserDb = mDatabasereference.child(userid);
                                        currenUserDb.child("name").setValue(name);
                                        currenUserDb.child("username").setValue(email);
                                        currenUserDb.child("image").setValue(resulturi.toString());

                                        mprogressdialog.dismiss();

                                        Intent intent = new Intent(CreateAccount.this,PostListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });



                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            Uri mImageUri = data.getData();
            profilepic.setImageURI(mImageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blg_signup_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.blg_terms:
                Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blg_about:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
