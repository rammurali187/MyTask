package com.ram.my.movies.ui.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    public interface OnLoadMoreCallback {
        void onLoadMore(int page, int totalItemsCount);
    }

    private OnLoadMoreCallback mCallback;

    public EndlessScrollListener() {}

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    public EndlessScrollListener setCallback(OnLoadMoreCallback callback) {
        mCallback = callback;
        return this;
    }


    @Override public abstract void onScrolled(RecyclerView recyclerView, int dx, int dy);

    public void onScrolled(int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }


        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }


    public void onLoadMore(int page, int totalItemsCount) {
        if (mCallback != null) {
            mCallback.onLoadMore(page, totalItemsCount);
        }
    }

    public static EndlessScrollListener fromGridLayoutManager(
            @NonNull final GridLayoutManager layoutManager,
            int visibleThreshold,
            int startPage) {

        return new EndlessScrollListener(visibleThreshold, startPage) {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return;

                final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                final int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                final int totalItemCount = layoutManager.getItemCount();

                onScrolled(firstVisibleItem, lastVisibleItem - firstVisibleItem, totalItemCount);
            }
        };
    }
}
