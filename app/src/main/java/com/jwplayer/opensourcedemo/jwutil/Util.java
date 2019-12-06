package com.jwplayer.opensourcedemo.jwutil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.android.exoplayer2.util.Util.toByteArray;

public class Util {

    public static byte[] executePost(String url) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
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
