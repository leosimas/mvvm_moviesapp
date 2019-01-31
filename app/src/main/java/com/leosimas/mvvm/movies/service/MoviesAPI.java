package com.leosimas.mvvm.movies.service;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leosimas.mvvm.movies.BuildConfig;
import com.leosimas.mvvm.movies.model.Genre;
import com.leosimas.mvvm.movies.model.GenreResult;
import com.leosimas.mvvm.movies.model.Movie;
import com.leosimas.mvvm.movies.model.MoviePage;
import com.leosimas.mvvm.movies.service.exception.NoNetworkAvailable;
import com.leosimas.mvvm.movies.service.json.DateDeserializer;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class MoviesAPI {

    private static final String API_URL = "https://api.themoviedb.org/3/";
    private static final String BASE_IMAGES_URL = "https://image.tmdb.org/t/p/";
    private static final String API_KEY = "1f54bd990f1cdfb230adb312546d765d";

    private MovieService service;
    private String language;
    private SparseArray<Genre> genres;

    @SystemService
    ConnectivityManager connectivityManager;

    public MoviesAPI() {
        Gson gson = this.createGson();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createHttpClient())
                .build();

        service = retrofit.create(MovieService.class);

        Locale locale = Locale.getDefault();
        language = locale.getLanguage() + "-" + locale.getCountry();
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    private OkHttpClient createHttpClient() {
        final int timeout = 30;
        return new OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        if (!isNetworkAvailable()) {
                            throw new NoNetworkAvailable();
                        }

                        Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
            .build();
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getPosterFullUrl(Movie movie) {
        if (movie.getPosterPath() == null) {
            return null;
        }
        return BASE_IMAGES_URL + "/w300" + movie.getPosterPath();
    }

    public static String getBackdropFullUrl(Movie movie) {
        if (movie.getBackdropPath() == null) {
            return null;
        }
        return BASE_IMAGES_URL + "/w500" + movie.getBackdropPath();
    }

    private Map<String, String> createQueryMap() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("api_key", API_KEY);
        queryMap.put("language", language);
        return queryMap;
    }

    public CallAdapter<MoviePage> upcoming(int page) {
        Map<String, String> queryMap = this.createQueryMap();
        queryMap.put("page", ""+page);

        CallAdapter<MoviePage> callAdapter = new CallAdapter<>(service.upcoming(queryMap));
        callAdapter.setInterceptor(new CallAdapter.Interceptor<MoviePage>() {
            @Override
            public void before(CallAdapter.Task task) {
                if (genres == null) {
                    callGenres(task);
                } else {
                    task.onDone(true);
                }
            }

            @Override
            public void after(MoviePage result) {
                fillGenres(result.getResults());
            }
        });
        return callAdapter;
    }

    private void fillGenres(List<Movie> movies) {
        if (movies == null) {
            return;
        }

        for (Movie m : movies) {
            ArrayList<Genre> list = new ArrayList<>();
            for (int genreId : m.getGenreIds()) {
                list.add( genres.get(genreId) );
            }
            m.setGenres( list );
        }
    }

    private void callGenres(final CallAdapter.Task task) {
        CallAdapter<GenreResult> callAdapter = genres();
        callAdapter.enqueue(new Callback<GenreResult>() {
            @Override
            public void onSuccess(GenreResult result) {
                genres = new SparseArray<>();
                for (Genre g : result.getGenres()) {
                    genres.put(g.getId(), g);
                }
                task.onDone(true);
            }

            @Override
            public void onFailure() {
                task.onDone(false);
            }
        });
    }

    public CallAdapter<MoviePage> search(String query, int page) {
        Map<String, String> queryMap = this.createQueryMap();
        queryMap.put("query", query);
        queryMap.put("page", ""+page);
        return new CallAdapter<>(service.search(queryMap));
    }

    public CallAdapter<GenreResult> genres() {
        return new CallAdapter<>(service.genres( this.createQueryMap() ));
    }

    public interface Callback<T> {

        void onSuccess(T result);
        void onFailure();

    }

}
