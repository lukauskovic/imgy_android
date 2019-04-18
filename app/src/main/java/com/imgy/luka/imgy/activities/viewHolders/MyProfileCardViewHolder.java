package com.imgy.luka.imgy.activities.viewHolders;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.activities.ChangePassword;
import com.imgy.luka.imgy.activities.Login;
import com.imgy.luka.imgy.utils.DisplayToast;

import java.lang.ref.WeakReference;

import static com.imgy.luka.imgy.activities.MyProfile.changeProfileImage;

public class MyProfileCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public View view;
    public TextView username;
    public TextView followingCount;
    public TextView followersCount;
    public ImageView profileImage;
    public TextView photosCount;
    private ImageView logoutButton;
    private ImageView settingButton;
    protected WeakReference<Activity> profileActivity;
    private PopupMenu popup;

    public MyProfileCardViewHolder(@NonNull View view, WeakReference<Activity> profileActivity) {
        super(view);
        this.view = view;
        this.profileActivity = profileActivity;
        this.username = view.findViewById(R.id.username);
        this.followingCount = view.findViewById(R.id.followingCount);
        this.followersCount = view.findViewById(R.id.followersCount);
        this.profileImage = view.findViewById(R.id.profileImage);
        this.logoutButton = view.findViewById(R.id.logoutButton);
        this.photosCount = view.findViewById(R.id.photosCount);
        logoutButton.setOnClickListener(this);
        this.settingButton = view.findViewById(R.id.settingsButton);
        this.settingButton.setOnClickListener(this);
        popup = new PopupMenu(view.getContext(), settingButton);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_popup, popup.getMenu());
    }

    @Override
    public void onClick(View view) {

        if(view == logoutButton){
            SharedPreferences pref = view.getContext().getSharedPreferences("prefs", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent signUpIntent = new Intent(view.getContext(), Login.class);
            view.getContext().startActivity(signUpIntent);
        }

        if (view == settingButton){
            popup.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeProfileImage:
                changeProfileImage(profileActivity);
                return true;
            case R.id.changePassword:
                changePassword();
                return true;
            default:
                return false;
        }
    }


    public void changePassword(){
        Intent changePasswordIntent = new Intent(profileActivity.get(), ChangePassword.class);
        profileActivity.get().startActivity(changePasswordIntent);
    }
}
