package com.jwplayer.opensourcedemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.google.android.exoplayer2.util.Util.toByteArray;

class Util {

    static byte[] executePost(String requestURL)
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

    public static byte[] executePost(String url, byte[] data, Map<String, String> requestProperties)
            throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(data != null);
            urlConnection.setDoInput(true);
            if (requestProperties != null) {
                for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                    urlConnection.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
                }
            }
            // Write the request body, if there is one.
            if (data != null) {
                try (OutputStream out = urlConnection.getOutputStream()) {
                    out.write(data);
                }
            }
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