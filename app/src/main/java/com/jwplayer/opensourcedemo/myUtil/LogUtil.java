package com.jwplayer.opensourcedemo.myUtil;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "JWPLAYER-LOG: ";

    public static void logEvent(String s){
        Log.i(TAG, "- (JWEVENT) -"+s);
    }

    public static void logError(String s, Throwable t){
        Log.i(TAG, s, t);
    }

    public static void logcast(String s) {
        Log.i(TAG, "(JWCASTHANDLER)" + s);
    }

    public static void logAd(String s) {
        Log.i(TAG, " - (ADEVENT) - " + s);
    }
}
