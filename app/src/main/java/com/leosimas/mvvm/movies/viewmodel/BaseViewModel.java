package com.leosimas.mvvm.movies.viewmodel;

import android.app.Application;

import com.leosimas.mvvm.movies.model.LoadingState;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    protected LoadingState loadingState = new LoadingState();
    protected MutableLiveData<LoadingState> loadingLive = new MutableLiveData<>();
    protected MutableLiveData<Integer> toastMessage = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadingState> getLoadingState() {
        return loadingLive;
    }

    public LiveData<Integer> getToastMessage() {
        return toastMessage;
    }
}
