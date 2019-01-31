package com.leosimas.mvvm.movies.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.leosimas.mvvm.movies.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class InfiniteRecyclerViewAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_TRY_AGAIN = 2;

    protected List<T> list;
    private boolean hasLoadingFailed = false;
    private View.OnClickListener tryAgainOnClickListener;

    public InfiniteRecyclerViewAdapter(List<T> list) {
        this.list = list;
    }

    public void setTryAgainOnClickListener(View.OnClickListener tryAgainOnClickListener) {
        this.tryAgainOnClickListener = tryAgainOnClickListener;
    }

    public void setHasLoadingFailed(boolean hasLoadingFailed) {
        this.hasLoadingFailed = hasLoadingFailed;
    }

    public boolean hasLoadingFailed() {
        return hasLoadingFailed;
    }

    public int getItemViewType(int position) {
        if (list.get(position) != null) {
            return VIEW_TYPE_ITEM;
        }
        return hasLoadingFailed ? VIEW_TYPE_TRY_AGAIN : VIEW_TYPE_LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if ( viewType == VIEW_TYPE_ITEM ) {
            return this.onCreateItemViewHolder(parent);
        } else if ( viewType == VIEW_TYPE_TRY_AGAIN ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_try_again, parent, false);
            return new TryAgainViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ( list.get(position) != null ) {
            this.onBindItemViewHolder((K) holder, position);
        } else if ( getItemViewType(position) == VIEW_TYPE_TRY_AGAIN ) {
            holder.itemView.setOnClickListener( tryAgainOnClickListener );
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<T> getList() {
        return list;
    }

    protected abstract K onCreateItemViewHolder(@NonNull ViewGroup parent);
    protected abstract void onBindItemViewHolder(@NonNull K holder, int position);

    private static class TryAgainViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TryAgainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
