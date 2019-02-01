package com.leosimas.mvvm.movies.ui.activity.main;

import android.app.Application;

import com.leosimas.mvvm.movies.R;
import com.leosimas.mvvm.movies.bean.Movie;
import com.leosimas.mvvm.movies.bean.MoviePage;
import com.leosimas.mvvm.movies.service.CallAdapter;
import com.leosimas.mvvm.movies.service.MoviesAPI;
import com.leosimas.mvvm.movies.service.MoviesAPI_;
import com.leosimas.mvvm.movies.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends BaseViewModel {

    private MoviesAPI api;

    private MoviePage upcomingMoviesLastPage;
    private MoviePage currentMoviesLastPage = new MoviePage();

    private List<Movie> upcomingMovies = new ArrayList<>();
    private List<Movie> searchMovies = new ArrayList<>();

    private CallAdapter<MoviePage> callAdapter;

    private String lastSearchQuery = "";

    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<Boolean> searchState = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        api = MoviesAPI_.getInstance_( getApplication().getApplicationContext() );
        searchState.setValue(false);
    }

    public @NonNull LiveData<List<Movie>> getMovies() {
        if (movies.getValue() == null && !isLoading()) {
            this.requestMovies();
        }
        return movies;
    }

    public @NonNull LiveData<Boolean> getSearchState() {
        return searchState;
    }

    public void requestMovies() {
        this.requestMovies(null);
    }

    public void searchMovies(String query) {
        this.requestMovies(query);
    }

    private void requestMovies(String query) {
        int nextPage = currentMoviesLastPage.getPage() + 1;
        if (searchState.getValue()) {
            if ( query == null ) {
                query = lastSearchQuery;
            } else if (query.isEmpty()) {
                toastMessage.setValue(R.string.type_movie_name);
                return;
            } else if (!lastSearchQuery.equals(query)) {
                lastSearchQuery = query;
                searchMovies.clear();
                currentMoviesLastPage = new MoviePage();
            }

            callAdapter = api.search(query, nextPage);
        } else {
            callAdapter = api.upcoming(nextPage);
        }

        setLoadingState();

        callAdapter.enqueue(new MoviesAPI.Callback<MoviePage>() {
            @Override
            public void onSuccess(MoviePage result) {
                List<Movie> list;
                if (searchState.getValue()) {
                    list = searchMovies;
                    if (list.isEmpty()) {
                        toastMessage.setValue(R.string.no_movie_found);
                    }
                } else {
                    upcomingMoviesLastPage = result;
                    list = upcomingMovies;
                }

                setNormalState();

                currentMoviesLastPage = result;
                list.addAll( result.getResults() );
                movies.setValue( list );
            }

            @Override
            public void onFailure() {
                setErrorState();
            }
        });
    }

    public void setSearchMode(boolean searchMode) {
        callAdapter.cancel();

        List<Movie> list;
        if (searchMode) {
            list = searchMovies;
            currentMoviesLastPage = new MoviePage();
        } else {
            list = upcomingMovies;
            currentMoviesLastPage = upcomingMoviesLastPage;
        }

        searchMovies.clear();

        movies.setValue(list);

        searchState.setValue(searchMode);
    }

    public boolean hasMorePages() {
        return currentMoviesLastPage.getTotalPages() > currentMoviesLastPage.getPage();
    }
}
