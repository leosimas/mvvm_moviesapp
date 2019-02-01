package com.leosimas.mvvm.movies.viewmodel;

import android.app.Application;

import com.leosimas.mvvm.movies.bean.ViewState;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    protected MessageLiveData toastMessage = new MessageLiveData();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    public MessageLiveData getToastMessage() {
        return toastMessage;
    }

    protected void setLoadingState() {
        setViewState(false, true);
    }

    protected void setErrorState() {
        setViewState(true, false);
    }

    protected void setNormalState() {
        setViewState(false, false);
    }

    private void setViewState(boolean isError, boolean isLoading) {
        ViewState value = viewState.getValue();
        if (value == null) {
            value = new ViewState();
        }

        value.setError(isError);
        value.setLoading(isLoading);
        viewState.setValue(value);
    }

    protected boolean isLoading() {
        ViewState value = viewState.getValue();
        if (value == null) {
            return false;
        }

        return value.isLoading();
    }
}
