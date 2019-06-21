package com.imgy.luka.imgy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imgy.luka.imgy.activities.PublicProfile;
import com.imgy.luka.imgy.activities.Register;
import com.imgy.luka.imgy.activities.viewHolders.ItemViewHolder;
import com.imgy.luka.imgy.activities.viewHolders.ProgressViewHolder;

import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.networking.LikeRequest;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.R;
import com.squareup.picasso.Picasso;


import java.lang.ref.WeakReference;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter {

    private List<Item> itemList;
    private final WeakReference<Activity> feedActivity;
    private int firstVisibleItem, totalItemCount;
    private boolean loading;
    private boolean endOfTheList;

    private OnLoadMoreListener onLoadMoreListener;

    public FeedAdapter(WeakReference<Activity> activity, List<Item> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.feedActivity = activity;
        if (itemList.size() < AppConstants.FEED_TAKE_VALUE) setEndOfTheList();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition(); 

                    if (!loading && totalItemCount <= firstVisibleItem + 2 && !endOfTheList) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        if (i == 1) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
            viewHolder = new ItemViewHolder(layoutView);
        } else {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_bar_bottom, viewGroup, false);
            viewHolder = new ProgressViewHolder(layoutView);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? 1 : 0; //fix this
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SharedPreferences pref = feedActivity.get().getSharedPreferences("prefs", 0);
        String id = pref.getString("id", "");
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).username.setText(itemList.get(position).getUsername());
            ((ItemViewHolder) holder).username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = itemList.get(holder.getAdapterPosition()).getUserId();
                    Intent publicProfileIntent = new Intent(feedActivity.get(), PublicProfile.class);
                    publicProfileIntent.putExtra("userId", userId);
                    feedActivity.get().startActivity(publicProfileIntent);
                }
            });

            if (itemList.get(position).isLiked(id)){
                ((ItemViewHolder) holder).likeItem(itemList.get(position).getLikesCount());
            }
            else ((ItemViewHolder) holder).unlikeItem(itemList.get(position).getLikesCount());
            ((ItemViewHolder) holder).likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = itemList.get(holder.getAdapterPosition());
                    ((ItemViewHolder) holder).likeButton.setEnabled(false);
                    new LikeRequest(feedActivity).execute(item.getId());
                    if (item.isLiked(id)){
                        item.getLikes().remove(id);
                        item.setLikesCount(item.getLikesCount() - 1);
                        ((ItemViewHolder) holder).unlikeItem(item.getLikesCount());
                    }
                    else{
                        item.getLikes().add(id);
                        item.setLikesCount(item.getLikesCount() + 1);
                        ((ItemViewHolder) holder).likeItem(item.getLikesCount());
                    }
                }
            });

            ((ItemViewHolder) holder).description.setText(itemList.get(position).getDescription());
            Picasso.get().load(itemList.get(position).getImageUrl()).fit().centerInside().into(((ItemViewHolder) holder).image);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoad() {
        loading = true;
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

    public void setEndOfTheList() {
        this.endOfTheList = true;
    }
}