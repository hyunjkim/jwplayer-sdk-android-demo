package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jwplayer.opensourcedemo.data.JWPlayerConfig;
import com.jwplayer.opensourcedemo.listeners.CallbackScreen;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;


public class JWPlayerViewExample extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);
        mPlayerView = findViewById(R.id.jwplayer);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Event Logging
        CallbackScreen mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);

        PlayerConfig config = JWPlayerConfig.getConfig();

        if (config != null) {
            Toast.makeText(this, "getConfig", Toast.LENGTH_SHORT).show();
            mPlayerView.setup(config);
            if (config.getPlaylist() == null) {
                mPlayerView.load(new PlaylistItem("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8"));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE,
                true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

}
