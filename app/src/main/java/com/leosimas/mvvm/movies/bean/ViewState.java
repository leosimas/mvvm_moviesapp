package com.leosimas.mvvm.movies.bean;

public class ViewState {

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

    @Override
    public String toString() {
        return "ViewState{" +
                "loading=" + loading +
                ", error=" + error +
                '}';
    }
}
