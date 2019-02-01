package com.leosimas.mvvm.movies.ui.adapter;

import android.view.ViewGroup;

import com.leosimas.mvvm.movies.bean.Movie;
import com.leosimas.mvvm.movies.ui.view.movie.MovieView;
import com.leosimas.mvvm.movies.ui.view.movie.MovieView_;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesViewAdapter extends InfiniteRecyclerViewAdapter<Movie, MoviesViewAdapter.MovieViewHolder> {

    public MoviesViewAdapter() {
        super(new ArrayList<Movie>());
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateItemViewHolder(@NonNull ViewGroup parent) {
        MovieView movieView = MovieView_.build(parent.getContext());
        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindItemViewHolder(@NonNull MoviesViewAdapter.MovieViewHolder holder, int position) {
        Movie movie = list.get(position);
        holder.movieView.bind(movie);
    }

    public void setList(List<Movie> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        MovieView movieView;

        public MovieViewHolder(MovieView movieView) {
            super(movieView);
            this.movieView = movieView;
        }

    }

}
