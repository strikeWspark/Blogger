package com.example.r.blogger.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.r.blogger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private TextInputLayout usernameInputlayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordInputlayout;
    private TextInputEditText passwordEditText;
    private MaterialButton nextbutton;
    private MaterialButton signupButton;
    private ProgressDialog signProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signProgress = new ProgressDialog(this);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                mUser = firebaseAuth.getCurrentUser();

                if(mUser != null){
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        };
        usernameInputlayout = (TextInputLayout) findViewById(R.id.blg_usernameID_input);
        usernameEditText = (TextInputEditText) findViewById(R.id.blg_usernameID_edit);
        passwordInputlayout = (TextInputLayout) findViewById(R.id.blg_passwordID_input);
        passwordEditText = (TextInputEditText)findViewById(R.id.blg_passwordID_edit);
        nextbutton = (MaterialButton) findViewById(R.id.blg_next_button);
        signupButton = (MaterialButton) findViewById(R.id.blg_signup_login);

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(usernameEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())) {


                    signProgress.setMessage("Signing In");
                    signProgress.show();
                    signProgress.setCancelable(false);
                    String email = usernameEditText.getText().toString();
                    String pwd = passwordEditText.getText().toString();

                    login(email,pwd);

                }else{
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccount.class));
            }
        });





    }

    private void login(String email, String pwd) {

        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this,PostListActivity.class));
                                finish();
                                signProgress.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this,"Not Signed In",Toast.LENGTH_LONG).show();
                                signProgress.dismiss();
                            }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


}
