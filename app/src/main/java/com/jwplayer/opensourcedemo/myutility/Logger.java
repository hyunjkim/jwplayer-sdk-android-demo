package com.jwplayer.opensourcedemo.myutility;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private static final String TAG = "JWPLAYER-LOGGER";
    private static StringBuilder sb;
    private static DateFormat dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US);

    public static String log(String s){
       if(sb == null) sb = new StringBuilder();
       sb.append(dateFormat.format(new Date())).append(" ").append(s).append("\r\n");
       return sb.toString();
    }

    public static void logEvent(String s){
        Log.i(TAG, "- (JWEVENT) - "+ s + "\r\n");
    }

    public static void logError(String s, Throwable t){
        Log.i(TAG, s, t);
    }

    public static void logCastSession(String s) {
        Log.i(TAG, "- (JW CastSession) - " + s);
    }

    public static void logAd(String s) {
        Log.i(TAG, "- (ADEVENT) - " + s + "\r\n");
    }
}
