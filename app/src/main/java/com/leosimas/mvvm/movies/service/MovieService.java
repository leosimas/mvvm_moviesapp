package com.leosimas.mvvm.movies.service;

import com.leosimas.mvvm.movies.bean.GenreResult;
import com.leosimas.mvvm.movies.bean.MoviePage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MovieService {

    @GET("genre/movie/list")
    Call<GenreResult> genres(@QueryMap Map<String, String> queryMap);

    @GET("movie/upcoming")
    Call<MoviePage> upcoming(@QueryMap Map<String, String> queryMap);

    @GET("search/movie")
    Call<MoviePage> search(@QueryMap Map<String, String> queryMap);

}
