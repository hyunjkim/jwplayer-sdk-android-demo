package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.myutilities.Logger;
import com.jwplayer.opensourcedemo.samples.MySampleAd;
import com.jwplayer.opensourcedemo.samples.MySamplePlaylist;
import com.jwplayer.opensourcedemo.samples.MySampleSkin;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        new Logger();

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

        // Setup JWPlayer
        setupJWPlayer();

        // Print the current Version of JWPlayer SDK
        outputTextView.setText(Logger.printBuildVersion(mPlayerView.getVersionCode()));

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        CastContext.getSharedInstance(this);
    }

    private void setupJWPlayer() {

        List<PlaylistItem> playlistItemList = MySamplePlaylist.myCustomPlaylist();
        List<PlaylistItem>  getSampleDRM = MySamplePlaylist.getSampleDRM();

        // Ima Tag Example
        ImaAdvertising imaAdvertising = MySampleAd.getImaAd();

        // VAST Tag Example
        Advertising vastAdvertising = MySampleAd.getVastAd();

        // SkinConfig Example
        SkinConfig skinConfig = MySampleSkin.getSkinConfig();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(getSampleDRM)
                .autostart(true)
                .preload(true)
                .allowCrossProtocolRedirects(true)
                .skinConfig(skinConfig)
//				.advertising(imaAdvertising)
//				.advertising(vastAdvertising)
                .build();

        mPlayerView.setup(config);
    }

    /*
     * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
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
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
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
