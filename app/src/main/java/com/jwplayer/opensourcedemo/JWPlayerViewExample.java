package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.handlers.CustomCastStateListener;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
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
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;

    /*
     * Reference to custom {@link MediaRouteButton}
     * */
    private MediaRouteButton mChromecastbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        mChromecastbtn = findViewById(R.id.chromecast_btn);
        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);

        // Instantiate CastStateListener
        CustomCastStateListener customCastStateListener = new CustomCastStateListener(mChromecastbtn);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Setup JWPlayer
        setupJWPlayerPlaylistItem();
//		setupJWPlayerPlayConfigWithEmptyCaptions();

        // Handle hiding/showing of MediaRouteButton
        mPlayerView.addOnControlBarVisibilityListener(customCastStateListener);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Adevent handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

        // Adding the custom cast button
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mChromecastbtn);

        // Set the Cast State Listener to the Cast Context
        mCastContext.addCastStateListener(customCastStateListener);

        // Attach session event listener
//        mCastContext.getSessionManager().addSessionManagerListener(this);
    }


    private void setupJWPlayerPlaylistItem() {
        String bipbop = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";
        
        PlaylistItem video = new PlaylistItem.Builder()
                .file(bipbop)
                .build();
        mPlayerView.load(video);
    }

    private void setupJWPlayerPlayConfigWithEmptyCaptions() {
        String captionVideo = "http://cdnbakmi.kaltura.com/p/243342/sp/24334200/playManifest/entryId/0_uka1msg4/flavorIds/1_vqhfu6uy,1_80sohj7p/format/applehttp/protocol/http/a.m3u8";

        PlaylistItem video = new PlaylistItem.Builder()
                .file(captionVideo)
                .build();

        List<Caption> captionTracks = new ArrayList<>();
        Caption emptyCaps = new Caption("file_en.srt");
        captionTracks.add(emptyCaps);
        video.setCaptions(captionTracks);

        List<PlaylistItem> item = new ArrayList<>();
        item.add(video);
        mPlayerView.setup(new PlayerConfig.Builder()
                .playlist(item)
                .build());
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
//        mCastContext.getSessionManager().removeSessionManagerListener(this);
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
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

//        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
//                R.id.media_route_menu_item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_fragment:
                Intent i = new Intent(this, JWPlayerFragmentExample.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
