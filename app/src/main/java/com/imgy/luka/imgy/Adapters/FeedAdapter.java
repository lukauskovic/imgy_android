package com.imgy.luka.imgy.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.imgy.luka.imgy.Activities.feed_activity.FeedViewHolder;
import com.imgy.luka.imgy.Activities.feed_activity.ProgressViewHolder;

import com.imgy.luka.imgy.Objects.FeedItem;
import com.imgy.luka.imgy.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private List<FeedItem> itemList;
    protected Context context;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public FeedAdapter(Context context, List<FeedItem> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.context = context;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= lastVisibleItem + 3) {
                        if (onLoadMoreListener != null) {
                            loading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FeedViewHolder viewHolder = null;
        if (i == 1) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
            viewHolder = new FeedViewHolder(layoutView);
        } else {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
            viewHolder = new FeedViewHolder(layoutView);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {
            ((FeedViewHolder) holder).username.setText(itemList.get(position).getUsername());
            ((FeedViewHolder) holder).description.setText(itemList.get(position).getDescription());
            Picasso.get().load(itemList.get(position).getImageUrl()).fit().centerInside().into(holder.image);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoad() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }
}