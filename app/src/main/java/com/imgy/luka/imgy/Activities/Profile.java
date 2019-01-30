package com.imgy.luka.imgy.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.imgy.luka.imgy.Activities.feed_activity.Feed;
import com.imgy.luka.imgy.R;

public class Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private BottomNavigationView navigationView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        this.logoutButton = (Button) findViewById(R.id.logoutButton);
        this.logoutButton.setOnClickListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.navigation_profile);
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

    @Override
    public void onClick(View view) {

        if(view == logoutButton){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent signUpIntent = new Intent(this.getApplicationContext(), Login.class);
            this.startActivity(signUpIntent);
        }
    }
}
