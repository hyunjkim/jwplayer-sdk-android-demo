package com.jwplayer.opensourcedemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Logger {

    private static StringBuilder outputStringBuilder = new StringBuilder();
    private static String dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US).format(new Date());

    static String generateLogLine(String output) {
        if (outputStringBuilder == null) outputStringBuilder = new StringBuilder();
        outputStringBuilder.append(dateFormat).append(" ").append(output).append("\r\n");
        return outputStringBuilder.toString();
    }
}