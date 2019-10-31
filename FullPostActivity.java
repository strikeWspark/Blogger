package com.example.r.blogger.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.r.blogger.Model.Blog;
import com.example.r.blogger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;


public class FullPostActivity extends AppCompatActivity {


    private Blog blog;
    private Toolbar toolbarfullscreen;
    private AppCompatImageView blogpic;
    private TextView titlefull;
    private TextView descriptionfull;
    private String blogid;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_post);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        blog = (Blog) getIntent().getSerializableExtra("blog");
        blogid = blog.getTimeStamp();
        System.out.println(blogid);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("MBlog").child(blogid);



        blogpic = (AppCompatImageView) findViewById(R.id.blg_image_full);
        titlefull = (TextView) findViewById(R.id.blg_title_full);
        descriptionfull = (TextView) findViewById(R.id.blg_description_full);
        toolbarfullscreen = (Toolbar) findViewById(R.id.blg_toolbar_full);

        setSupportActionBar(toolbarfullscreen);

        getBlogDetals(blogid);


        toolbarfullscreen.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FullPostActivity.this,PostListActivity.class));
                finish();
            }
        });
    }

    private void getBlogDetals(String blogid) {
        titlefull.setText(blog.getTitle());
        descriptionfull.setText(blog.getDescription());

        String blogurl = null;
        blogurl = blog.getImage();


        Picasso.with(getApplicationContext()).load(blogurl).into(blogpic);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blg_fullscreen_toolbar,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.blg_terms_full:
                Toast.makeText(this, "Terms and Condition", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blg_about_full:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
