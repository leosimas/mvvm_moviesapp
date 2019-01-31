package com.leosimas.mvvm.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePage {

    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
