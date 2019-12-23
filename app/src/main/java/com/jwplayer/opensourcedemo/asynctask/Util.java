package com.jwplayer.opensourcedemo.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.google.android.exoplayer2.util.Util.toByteArray;

public class Util {

    public static byte[] executePost(String requestURL)
            throws IOException {

        HttpURLConnection urlConnection = null;
        URL url = new URL(requestURL);

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            // Read and return the response body.
            try (InputStream inputStream = urlConnection.getInputStream()) {
                return toByteArray(inputStream);
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}