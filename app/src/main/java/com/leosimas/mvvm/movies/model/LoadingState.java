package com.leosimas.mvvm.movies.model;

public class LoadingState {

    private boolean loading;
    private boolean error;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
