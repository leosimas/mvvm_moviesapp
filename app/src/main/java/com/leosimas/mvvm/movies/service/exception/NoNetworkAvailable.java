package com.leosimas.mvvm.movies.service.exception;

import java.io.IOException;

public class NoNetworkAvailable extends IOException {

    public NoNetworkAvailable() {
        super("No network available");
    }
}
