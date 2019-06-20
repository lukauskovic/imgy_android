package com.imgy.luka.imgy.activities;

import android.app.Activity;
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
import com.imgy.luka.imgy.adapters.PublicProfileAdapter;
import com.imgy.luka.imgy.networking.GetMyProfile;
import com.imgy.luka.imgy.networking.GetPublicProfile;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.objects.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.imgy.luka.imgy.constants.AppConstants.FEED_TAKE_VALUE;

public class PublicProfile extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static User user;
    private static String userId;

    private static ProgressBar progressBar; //Solve progress bar

    static ArrayList<Item> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        initialiseUi();
        userId = getIntent().getStringExtra("userId");
        new GetPublicProfile(this, userId).execute(1);
    }

    private void initialiseUi(){
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.profileItems);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static void initAdapter(WeakReference<Activity> activity, ArrayList<Item> data) {
        adapter = new PublicProfileAdapter(activity , data, user, recyclerView);
        recyclerView.setAdapter(adapter);
        items = data;
        ((PublicProfileAdapter) adapter).setOnLoadMoreListener(new PublicProfileAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new GetPublicProfile(activity.get(), userId).execute((items.size() + 1) / 5 + 1);
            }
        });
    }

    public static void onSuccessfulFollow(){
        ((PublicProfileAdapter) adapter).setUser(user);
        adapter.notifyDataSetChanged();

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
        ((PublicProfileAdapter) adapter).setLoaded();
        if (data.size() < FEED_TAKE_VALUE) ((PublicProfileAdapter) adapter).setEndOfTheList();
    }

    public static void setUser(User foundUser) {
        user = foundUser;
    }
}
