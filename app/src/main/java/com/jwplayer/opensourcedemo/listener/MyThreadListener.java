package com.jwplayer.opensourcedemo.listener;

public interface MyThreadListener {
    void setupJWPlayer();

    void countDown(String count);

    void beforeExecute();

    void clear();
}
