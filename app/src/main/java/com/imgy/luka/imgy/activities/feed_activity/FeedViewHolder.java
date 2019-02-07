package com.imgy.luka.imgy.Activities.feed_activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imgy.luka.imgy.R;

public class FeedViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView username;
    public ImageView image;
    public TextView description;


    public FeedViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        username = view.findViewById(R.id.username);
        image = view.findViewById(R.id.image);
        description = view.findViewById(R.id.description);
    }

}
