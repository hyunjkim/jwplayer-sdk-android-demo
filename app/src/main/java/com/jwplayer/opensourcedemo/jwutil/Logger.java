package com.jwplayer.opensourcedemo.jwutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    private static StringBuilder outputStringBuilder;
    private static boolean printedVersion = false;
    private static DateFormat dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US);

    public static String generateLogLine(String output) {
        if (outputStringBuilder == null) outputStringBuilder = new StringBuilder();
        outputStringBuilder.append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        return outputStringBuilder.toString();
    }

    public static String printBuildVersion(String jwPlayerVersion) {
        String version = "";

        if (!printedVersion) {
            version = outputStringBuilder.append("Build version: ").append(jwPlayerVersion).append("\r\n").toString();
            printedVersion = !printedVersion;
        }

        return version;
    }
}
