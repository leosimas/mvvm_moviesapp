package com.leosimas.mvvm.movies.model;

import java.util.Date;

public class Movie {

    protected int id;
    protected String title;
//    @SerializedName("original_title")
    protected String originalTitle;
    protected String overview;
//    @SerializedName("release_date")
    protected Date releaseDate;
//    @SerializedName("poster_path")
    protected String posterPath;
//    @SerializedName("backdrop_path")
    protected String backdropPath;
//    @SerializedName("genre_ids")
//    protected int[] genreIds;
//    protected List<Genre> genres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

}
