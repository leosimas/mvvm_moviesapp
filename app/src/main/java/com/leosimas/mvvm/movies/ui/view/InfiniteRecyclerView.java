package com.leosimas.mvvm.movies.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.leosimas.mvvm.movies.ui.adapter.InfiniteRecyclerViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InfiniteRecyclerView extends RecyclerView {

    private boolean isLoading = false;
    private InfiniteScrollListener infiniteScrollListener;
    private InfiniteRecyclerViewAdapter adapter;

    public InfiniteRecyclerView(Context context) {
        super(context);
    }

    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initInfiniteRecyclerView(Context context, InfiniteRecyclerViewAdapter adapter, InfiniteScrollListener infiniteScrollListener) {
        this.adapter = adapter;
        this.infiniteScrollListener = infiniteScrollListener;
        adapter.setTryAgainOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
            }
        });

        this.setLayoutManager( new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        this.setAdapter(adapter);
        this.initScrollListener();
    }

    private void initScrollListener() {
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isLoading() || !infiniteScrollListener.hasMorePages()
                        || adapter.hasLoadingFailed() ) {
                    return;
                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (position > -1 && position >= getAdapter().getItemCount() - 2) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showLoading();
                            infiniteScrollListener.loadNextPage();
                        }
                    });
                }
            }
        });
    }

    private synchronized boolean isLoading() {
        return isLoading;
    }

    private synchronized void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void showLoading() {
        if ( isLoading() ) {
            return;
        }
        setLoading(true);

        if (adapter.hasLoadingFailed()) {
            adapter.setHasLoadingFailed(false);
            adapter.notifyItemChanged(adapter.getList().size() - 1);
        } else {
            adapter.getList().add(null);
            adapter.notifyItemInserted(adapter.getList().size() - 1);
        }
    }

    public void showError() {
        adapter.setHasLoadingFailed(true);
        setLoading(false);
        adapter.notifyItemChanged(adapter.getList().size() - 1 );
    }

    public void showList(List newList) {
        adapter.getList().clear();
        adapter.getList().addAll(newList);
        adapter.notifyDataSetChanged();
        setLoading(false);
    }

    public interface InfiniteScrollListener {
        void loadNextPage();
        boolean hasMorePages();
    }

}
