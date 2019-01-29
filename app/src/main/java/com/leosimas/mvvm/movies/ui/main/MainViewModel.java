package com.leosimas.mvvm.movies.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leosimas.mvvm.movies.model.Movie;

import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> movies;

    public LiveData<List<Movie>> getMovies() {
        if (movies == null) {
            movies = new MutableLiveData<>();
        }
        return movies;
    }
}
