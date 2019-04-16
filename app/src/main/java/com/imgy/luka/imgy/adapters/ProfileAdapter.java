package com.imgy.luka.imgy.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imgy.luka.imgy.activities.viewHolders.ItemViewHolder;
import com.imgy.luka.imgy.activities.viewHolders.MyProfileCardViewHolder;
import com.imgy.luka.imgy.activities.viewHolders.ProgressViewHolder;

import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.objects.User;
import com.squareup.picasso.Picasso;


import java.lang.ref.WeakReference;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter {

    private List<Item> itemList;

    private User user;
    private final WeakReference<Activity> profileActivity;
    private int firstVisibleItem, totalItemCount;
    private boolean loading;
    private boolean endOfTheList;

    private OnLoadMoreListener onLoadMoreListener;

    public ProfileAdapter(WeakReference<Activity> activity, List<Item> itemList, User user, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.profileActivity = activity;
        this.user = user;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 0) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_profile_card, viewGroup, false);
            viewHolder = new MyProfileCardViewHolder(layoutView, profileActivity);
        } else if (viewType == 1){
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
        if(position == 0) return position;
        else{
            return itemList.get(position - 1) != null ? 1 : 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            position = position - 1;
            ((ItemViewHolder) holder).username.setText(itemList.get(position).getUsername());
            ((ItemViewHolder) holder).description.setText(itemList.get(position).getDescription());
            Picasso.get().load(itemList.get(position).getImageUrl()).fit().centerInside().into(((ItemViewHolder) holder).image);
        } else if (holder instanceof MyProfileCardViewHolder) {
            ((MyProfileCardViewHolder) holder).username.setText(user.getUsername());
            ((MyProfileCardViewHolder) holder).followingCount.setText(user.getFollowingCount());
            ((MyProfileCardViewHolder) holder).followersCount.setText(user.getFollowersCount());
            ((MyProfileCardViewHolder) holder).photosCount.setText(user.getPhotosCount());
            if (user.getProfileImageUrl() != null) Picasso.get().load(user.getProfileImageUrl()).fit().centerInside().into( ((MyProfileCardViewHolder) holder).profileImage);
            else {
                Bitmap profileImage = BitmapFactory.decodeResource(profileActivity.get().getResources(),
                        R.drawable.default_profile_picture);
                ((MyProfileCardViewHolder) holder).profileImage.setImageBitmap(profileImage);

            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoad() {
        loading = true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size() + 1;
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