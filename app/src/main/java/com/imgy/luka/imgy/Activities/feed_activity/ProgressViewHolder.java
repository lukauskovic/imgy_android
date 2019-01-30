package com.imgy.luka.imgy.Activities.feed_activity;

import android.view.View;
import android.widget.ProgressBar;
import com.imgy.luka.imgy.R;

public class ProgressViewHolder extends FeedViewHolder {

    public ProgressBar progressBar;

    public ProgressViewHolder(View view) {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.item_progress_bar);
    }
}
