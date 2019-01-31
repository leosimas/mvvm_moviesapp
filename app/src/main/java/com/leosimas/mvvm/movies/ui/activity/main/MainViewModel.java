package com.leosimas.mvvm.movies.ui.activity.main;

import android.app.Application;

import com.leosimas.mvvm.movies.R;
import com.leosimas.mvvm.movies.model.Movie;
import com.leosimas.mvvm.movies.model.MoviePage;
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

    private boolean searchMode = false;

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
        return movies;
    }

    public @NonNull LiveData<Boolean> getSearchState() {
        return searchState;
    }

    public boolean isSearchMode() {
        return searchMode;
    }

    public void requestMovies() {
        this.requestMovies(null);
    }

    public void searchMovies(String query) {
        this.requestMovies(query);
    }

    private void requestMovies(String query) {
        int nextPage = currentMoviesLastPage.getPage() + 1;
        if (searchMode) {
            if ( query == null ) {
                query = lastSearchQuery;
            } else if (query.isEmpty()) {
                toastMessage.postValue(R.string.type_movie_name);
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

        loadingState.setError(false);
        loadingState.setLoading(true);
        loadingLive.postValue(loadingState);

        callAdapter.enqueue(new MoviesAPI.Callback<MoviePage>() {
            @Override
            public void onSuccess(MoviePage result) {
                List<Movie> list;
                if (searchMode) {
                    list = searchMovies;
                    if (list.isEmpty()) {
                        toastMessage.postValue(R.string.no_movie_found);
                    }
                } else {
                    upcomingMoviesLastPage = result;
                    list = upcomingMovies;
                }

                loadingState.setError(false);
                loadingState.setLoading(false);
                loadingLive.postValue(loadingState);

                currentMoviesLastPage = result;
                list.addAll( result.getResults() );
                movies.setValue( list );
            }

            @Override
            public void onFailure() {
                loadingState.setError(true);
                loadingState.setLoading(false);
                loadingLive.postValue(loadingState);
            }
        });
    }

    public void setSearchMode(boolean searchMode) {
        this.searchMode = searchMode;
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
