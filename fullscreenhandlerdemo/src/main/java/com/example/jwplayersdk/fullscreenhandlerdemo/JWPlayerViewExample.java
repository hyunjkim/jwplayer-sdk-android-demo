package com.example.jwplayersdk.fullscreenhandlerdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity
        implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;

    private CallbackScreen mCallbackScreen;

    // Minimized View Size
    private ConstraintLayout.LayoutParams minimizedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Save the values Minimized layout param values
        minimizedView = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // My Customer Fullscreen Handler
        // Fullscreen Vertically and Horizontally; NOT only horizontally
        mPlayerView.setFullscreenHandler(new FullscreenHandler() {

            @Override
            public void onFullscreenRequested() {

                // Set window fullscreen and remove title bar, and force landscape orientation
                ConstraintLayout.LayoutParams fullscreenParams =
                        new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                ConstraintLayout.LayoutParams.MATCH_PARENT);

                // Set to Fullscreen
                mPlayerView.setLayoutParams(fullscreenParams);

            }

            @Override
            public void onFullscreenExitRequested() {

                // Minimize the screen
                mPlayerView.setLayoutParams(minimizedView);

            }

            @Override
            public void onResume() {
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onDestroy() {
            }

            @Override
            public void onAllowRotationChanged(boolean b) {
            }

            @Override
            public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            }

            @Override
            public void setUseFullscreenLayoutFlags(boolean b) {
            }
        });


        // Event Logging
        mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);

        List<PlaylistItem> item = new ArrayList<>();

        String url = "https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";
        String url1 = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";

        // Load a media source
        PlaylistItem pi = new PlaylistItem.Builder()
                .file(url)
                .build();

        // Load a media source
        PlaylistItem pi1 = new PlaylistItem.Builder()
                .file(url1)
                .build();

        item.add(pi);
        item.add(pi1);

        // Load and Play
        mPlayerView.load(item);

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
