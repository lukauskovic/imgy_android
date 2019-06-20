package com.imgy.luka.imgy.activities.viewHolders;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.utils.DisplayToast;

import java.lang.ref.WeakReference;

import static android.view.View.GONE;

public class PublicProfileCardViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView username;
    public TextView followingCount;
    public TextView followersCount;
    public ImageView profileImage;
    public TextView photosCount;
    public Button followButton;
    protected WeakReference<Activity> publicProfileActivity;

    public PublicProfileCardViewHolder(@NonNull View view, WeakReference<Activity> publicProfileActivity) {
        super(view);
        this.view = view;
        this.publicProfileActivity = publicProfileActivity;
        this.username = view.findViewById(R.id.username);
        this.followingCount = view.findViewById(R.id.followingCount);
        this.followersCount = view.findViewById(R.id.followersCount);
        this.profileImage = view.findViewById(R.id.profileImage);
        this.followButton = view.findViewById(R.id.followButton);
        this.photosCount = view.findViewById(R.id.photosCount);
    }

    public void hideFollowButton(){
        this.followButton.setVisibility(GONE);
    }

    public void followButtonText(){
        this.followButton.setText("Follow");
        this.followButton.setEnabled(true);
    }

    public void unfollowButtonText(){
        this.followButton.setText("Unfollow");
        this.followButton.setEnabled(true);
    }


}
