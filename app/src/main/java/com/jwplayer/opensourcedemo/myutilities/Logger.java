package com.jwplayer.opensourcedemo.myutilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private static StringBuilder outputStringBuilder;

    public Logger(){
        outputStringBuilder =  new StringBuilder();
    }

     public static String updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        return outputStringBuilder.toString();
    }

    public static String printBuildVersion(String jwPlayerVersion) {
        return outputStringBuilder.append("Build version: ").append(jwPlayerVersion).append("\r\n").toString();
    }
}