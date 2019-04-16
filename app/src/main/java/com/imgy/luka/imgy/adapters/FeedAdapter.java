package com.imgy.luka.imgy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imgy.luka.imgy.activities.viewHolders.ItemViewHolder;
import com.imgy.luka.imgy.activities.viewHolders.ProgressViewHolder;

import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter {

    private List<Item> itemList;
    protected Context context;
    private int firstVisibleItem, totalItemCount;
    private boolean loading;
    private boolean endOfTheList;

    private OnLoadMoreListener onLoadMoreListener;

    public FeedAdapter(Context context, List<Item> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.context = context;
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
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).username.setText(itemList.get(position).getUsername());
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