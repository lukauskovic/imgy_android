package com.imgy.luka.imgy.activities.viewHolders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.activities.PublicProfile;
import com.imgy.luka.imgy.activities.Register;
import com.imgy.luka.imgy.activities.Upload;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    public View view;
    public TextView username;
    public ImageView image;
    public TextView description;


    public ItemViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        username = view.findViewById(R.id.username);
        image = view.findViewById(R.id.image);
        description = view.findViewById(R.id.description);
    }
}
