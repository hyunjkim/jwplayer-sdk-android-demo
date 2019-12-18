package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerViewExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Reference to the {@link ConstraintLayout}
     */
    private ConstraintLayout mConstraintLayout;

    /**
     * Reference to the {@link CallbackScreen}
     */
    private CallbackScreen mCallbackscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        mCallbackscreen = findViewById(R.id.callback_screen);
        mConstraintLayout = findViewById(R.id.activity_jwplayerview);

        // Setup Click listeners for the buttons
        findViewById(R.id.button1).setOnClickListener(v -> setupJWPlayer());
        findViewById(R.id.button2).setOnClickListener(v -> setupJWPlayerVTT());

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Register callback
        mCallbackscreen.registerListeners(mPlayerView);

        // Initalize embedded 608 captions test
        setupJWPlayer();

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    private void setupJWPlayer() {
        String vod = "https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";

        // List of Playlistitem
        List<PlaylistItem> item = new ArrayList<>();

        // Build a playlistitem
        PlaylistItem video = new PlaylistItem.Builder()
                .file(vod)
                .build();

        // Add the playlist item with the caption to a Playlist
        item.add(video);

        // Pass the playlist to the player config
        mPlayerView.setup(new PlayerConfig.Builder()
                .playlist(item)
                .build());
    }

    private void setupJWPlayerVTT() {

        String captionVideo = " https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";
        String vtt = "https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/webvtt/bunny.vtt";

        // Add captions here
        List<Caption> captionTracks = new ArrayList<>();

        Caption caption1 = new Caption.Builder()
                .file(vtt)
                .label("VTT")
                .build();

        captionTracks.add(caption1);

        // Build a playlistitem
        PlaylistItem video = new PlaylistItem.Builder()
                .file(captionVideo)
                .tracks(captionTracks) // Set the caption to the video
                .build();

        // Add the playlist item with the caption to a Playlist
        List<PlaylistItem> item = new ArrayList<>();
        item.add(video);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(item)
                .build();

        // Pass the playlist to the player config
        mPlayerView.setup(config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        mPlayerView.onDestroy();
        super.onDestroy();
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

    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreenEvent true if the player is fullscreen
     */
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

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mConstraintLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
        // Register the MediaRouterButton on the JW Player SDK
        // Register the MediaRouterButton on the JW Player SDK
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }
}
