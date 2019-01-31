package com.leosimas.mvvm.movies.service;

import android.util.Log;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallAdapter<T> {

    private Call<T> call;
    private Interceptor<T> interceptor;

    CallAdapter(Call<T> call) {
        this.call = call;
    }

    public void cancel() {
        this.call.cancel();
    }

    public void enqueue(final MoviesAPI.Callback<T> callback) {
        if ( this.interceptor != null ) {
            this.interceptor.before(new Task() {
                @Override
                public void onDone(boolean isSuccessful) {
                    if ( isSuccessful ) {
                        doEnqueue(callback);
                    } else {
                        callback.onFailure();
                    }
                }
            });
            return;
        }

        this.doEnqueue(callback);
    }

    private void doEnqueue(final MoviesAPI.Callback<T> callback) {
        this.call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if ( !response.isSuccessful() ) {
                    Log.d("calladapter_service", "STATUS = " + response.code() +", " + response.toString());
                    callback.onFailure();
                    return;
                }

                T body = response.body();
                if (interceptor != null) {
                    interceptor.after(body);
                }
                callback.onSuccess(body);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                Log.d("calladapter_service", t.getMessage(), t);
                callback.onFailure();
            }
        });
    }

    void setInterceptor(Interceptor<T> interceptor) {
        this.interceptor = interceptor;
    }

    interface Interceptor<T> {
        void before(Task task);
        void after(T result);
    }

    interface Task {
        void onDone(boolean isSuccessful);
    }

}
