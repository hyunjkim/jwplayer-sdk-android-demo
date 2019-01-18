package com.jwplayer.opensourcedemo.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "JWPLAYER-LOGGER";

    public static void log(String s){
        Log.i(TAG, s);
    }

    public static void log(String s, Throwable t){
        Log.i(TAG, s, t);
    }

}
