package com.imgy.luka.imgy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.imgy.luka.imgy.adapters.FeedAdapter;
import com.imgy.luka.imgy.networking.GetFeedItems;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.R;

import java.util.ArrayList;

import static com.imgy.luka.imgy.constants.AppConstants.FEED_TAKE_VALUE;

public class Feed extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static ProgressBar progressBar;

    static ArrayList<Item> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initialiseUI();
        progressBar.setVisibility(View.VISIBLE);
        new GetFeedItems(Feed.this).execute(1);
    }

    private void initialiseUI() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.navigation_feed);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.feedItems);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.item_progress_bar);
    }

    public static void initAdapter(Activity activity, ArrayList<Item> data) {
        adapter = new FeedAdapter(activity.getApplicationContext(), data, recyclerView);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        items = data;
        ((FeedAdapter) adapter).setOnLoadMoreListener(new FeedAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new GetFeedItems(activity).execute((items.size() + 1) / 5 + 1);
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
        ((FeedAdapter) adapter).setLoaded();
        if (data.size() < FEED_TAKE_VALUE) ((FeedAdapter) adapter).setEndOfTheList();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_upload) {
                startActivity(new Intent(this, Upload.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(0, 0);
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, MyProfile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(0, 0);
            } else if (itemId == R.id.navigation_feed) {
                return;
            }
            finish();
        }, 0);
        return true;
    }
}
