package com.imgy.luka.imgy.Activities.feed_activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.imgy.luka.imgy.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public ProgressViewHolder(View view) {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_bottom);
    }
}
