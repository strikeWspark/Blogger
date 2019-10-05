package com.example.r.blogger.Data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.r.blogger.Model.Blog;
import com.example.r.blogger.R;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Blog blog = blogList.get(position);
        String imageUrl = null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDescription());


        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimeStamp())).getTime());

        holder.timeStamp.setText(formattedDate);

        imageUrl = blog.getImage();

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public AppCompatImageView image;
        public TextView desc;
        public TextView timeStamp;
        String userID;



        public ViewHolder(@NonNull View view,Context ctx) {
            super(view);
            context = ctx;

            title = (TextView) view.findViewById(R.id.blg_post_title_list);
            image = (AppCompatImageView) view.findViewById(R.id.blg_post_image_list);
            desc = (TextView) view.findViewById(R.id.blg_post_text_list);
            timeStamp = (TextView) view.findViewById(R.id.blg_time_stamp_list);

            userID = null;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //we can go to next activity
                }
            });


        }
    }
}
