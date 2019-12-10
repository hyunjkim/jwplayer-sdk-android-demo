package com.jwplayer.opensourcedemo.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jwplayer.opensourcedemo.JWPlayerViewExample;
import com.jwplayer.opensourcedemo.api.JWURLSignature;
import com.jwplayer.opensourcedemo.data.JWPlayerConfig;
import com.jwplayer.opensourcedemo.listeners.JWPlayerCallback;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.android.exoplayer2.util.Util.toByteArray;

public class JWSetupPlayerConfig extends Activity implements JWPlayerCallback {

    private static final String TAG = "JWEVENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Get your player ID here
        String playerId = "add_your_player_id";

        JWURLSignature jwurlSignature = new JWURLSignature();
        jwurlSignature.createSignature(playerId, 50);

        String configURL = "https://api.jwplatform.com/v1/players/show" + jwurlSignature.getPlayerApiUrlSignature();

        new MyAsyncTask(this).execute(configURL);

    }

    // Setup JWPlayer Config
    @Override
    public void setupJWPlayer() {
        Intent intent = new Intent(this, JWPlayerViewExample.class);
        startActivity(intent);
    }

    public static class MyAsyncTask extends AsyncTask<String, String, PlayerConfig>{

        private PlayerConfig mConfig;

        private JWPlayerCallback mListener;

        public MyAsyncTask(JWPlayerCallback listener) {
            super();
            mListener = listener;
        }

        @Override
        protected PlayerConfig doInBackground(String... strings) {
            try {
                byte[] response = executePost(strings[0]);
                String strResponse = new String(response);

                Log.i(TAG, "(JWSetupPlayerConfig) " + strResponse);

                mConfig = PlayerConfig.parseJson(strResponse);

            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "(JWSetupPlayerConfig) ERROR - 1 : " + e.getCause());
                Log.i(TAG, "(JWSetupPlayerConfig) ERROR - 2 : " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(PlayerConfig playerConfig) {
            super.onPostExecute(playerConfig);
            JWPlayerConfig.setConfig(mConfig);
            mListener.setupJWPlayer();
        }

        byte[] executePost(String url) throws IOException {
            Log.i(TAG, "(executePost) " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            // Read and return the response body.
            try (InputStream inputStream = urlConnection.getInputStream()) {
                return toByteArray(inputStream);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
