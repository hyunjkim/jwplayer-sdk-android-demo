package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Stretching None
        findViewById(R.id.none).setOnClickListener(v -> {
            if (mPlayerView.getConfig() != null) {
                setupStretching(PlayerConfig.STRETCHING_NONE);
            }
        });
        // Stretching Exact fit
        findViewById(R.id.fit).setOnClickListener(v -> {
            if (mPlayerView.getConfig() != null) {
                setupStretching(PlayerConfig.STRETCHING_EXACT_FIT);
            }
        });
        // Stretching Fill
        findViewById(R.id.fill).setOnClickListener(v -> {
            if (mPlayerView.getConfig() != null) {
                setupStretching(PlayerConfig.STRETCHING_FILL);
            }
        });
        // Stretching Uniform
        findViewById(R.id.uniform).setOnClickListener(v -> {
            if (mPlayerView.getConfig() != null) {
                setupStretching(PlayerConfig.STRETCHING_UNIFORM);
            }
        });

        String video = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";

        // Build Player Config
        PlayerConfig config = new PlayerConfig.Builder()
                .file(video)
                .autostart(true)
                .build();

        // Setup JWPlayer
        mPlayerView.setup(config);

        // Initialize event listeners
        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
    }

    /*
    * Setup Stretching here
    * */
    public void setupStretching(String stretch) {

        Toast.makeText(this, stretch, Toast.LENGTH_SHORT).show();

        // Get Config
        PlayerConfig mConfig = mPlayerView.getConfig();

        // Set the Stretching
        mConfig.setStretching(stretch);

        // Setup the Config
        mPlayerView.setup(mConfig);
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
}
