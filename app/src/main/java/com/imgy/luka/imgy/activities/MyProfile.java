package com.imgy.luka.imgy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.adapters.ProfileAdapter;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.networking.GetMyProfile;
import com.imgy.luka.imgy.networking.UploadProfilePicture;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.objects.User;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.imgy.luka.imgy.constants.AppConstants.FEED_TAKE_VALUE;

public class MyProfile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static User user;

    private static ProgressBar progressBar; //Solve progress bar

    static ArrayList<Item> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialiseUi();
        new GetMyProfile(this).execute(1);
    }

    private void initialiseUi(){
        this.navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.navigation_profile);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.profileItems);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static void initAdapter(WeakReference<Activity> activity, ArrayList<Item> data) {
        adapter = new ProfileAdapter(activity , data, user, recyclerView);
        recyclerView.setAdapter(adapter);
        items = data;
        ((ProfileAdapter) adapter).setOnLoadMoreListener(new ProfileAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new GetMyProfile(activity.get()).execute((items.size() + 1) / 5 + 1);
            }
        });
    }

    public static void updateAdapter(ArrayList<Item> data) {
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);
        items.remove(items.size() - 1);
        adapter.notifyItemRemoved(items.size());
        for (int i = 0; i < data.size(); i++) {
            items.add(data.get(i));
            adapter.notifyItemInserted(items.size());
        }
        ((ProfileAdapter) adapter).setLoaded();
        if (data.size() < FEED_TAKE_VALUE) ((ProfileAdapter) adapter).setEndOfTheList();
    }

    public static void setUser(User foundUser) {
        user = foundUser;
    }

    public static void changeProfileImage(WeakReference<Activity> profileActivity){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setBorderLineColor(Color.RED)
                .setGuidelinesColor(Color.GREEN)
                .setAspectRatio(1,1)
                .start(profileActivity.get());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                new UploadProfilePicture(this).execute(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                new DisplayToast(this, AppConstants.DEFAULT_ERROR_MESSAGE);
            }
        }
    }

    public static void onProfileImageChanged(String imageUrl){
        user.setProfileImageUrl(imageUrl);
        ((ProfileAdapter) adapter).setUser(user);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_feed) {
                startActivity(new Intent(this, Feed.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(0, 0);
            } else if (itemId == R.id.navigation_upload) {
                startActivity(new Intent(this, Upload.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(0, 0);
            } else if (itemId == R.id.navigation_profile) {
                return;
            }
            finish();
        }, 0);
        return true;
    }
}
