package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.jwutil.Logger;
import com.jwplayer.opensourcedemo.listeners.JWAdEventHandler;
import com.jwplayer.opensourcedemo.listeners.JWEventHandler;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
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
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;

    private String ima = "PLAY IMA";
    private String vast = "PLAY VAST";
    private Button adButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        adButton = findViewById(R.id.ad_button);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

        outputTextView.setText(Logger.updateOutput("Build version: " + mPlayerView.getVersionCode() + "\r\n"));

        // If the player is not null
        // and the player state is playing, idle, or buffering
        // Load the new Ad
        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPlayerView.getState().equals(PlayerState.COMPLETE)) {
                    mPlayerView.pause();
                }
                if (adButton.getText().equals(ima)) {
                    setupJWPlayer("");
                    adButton.setText(vast);
                } else {
                    mPlayerView.playAd(AdSource.VAST, "https://playertest.longtailvideo.com/vast-30s-ad.xml");
                    adButton.setText(ima);
                }
            }
        });

        // Setup JWPlayer
        setupJWPlayer(vast);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        mCastContext = CastContext.getSharedInstance(this);
    }


     /**
      * This is an example of how to user the skip message
      * It can only be used with VAST Ads
      *
      * @see Advertising
      * */
    private void setupJWPlayer(String client) {

        String vpaid = "https://playertest.longtailvideo.com/vpaid2-jwp-30s.xml";
        String ad = "https://playertest.longtailvideo.com/vast-30s-ad.xml";


        // VAST button clicked
        if (adButton.getText().equals(vast)) {
            List<PlaylistItem> playlist = new ArrayList<>();
            PlaylistItem item = new PlaylistItem("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8");

            List<AdBreak> adSchedule = new ArrayList<AdBreak>() {{
                add(new AdBreak("pre", AdSource.VAST, ad));
            }};

            item.setAdSchedule(adSchedule);

            Advertising advertising = new Advertising(AdSource.VAST, adSchedule);
            advertising.setSkipOffset(3);
            advertising.setSkipMessage("SKIP TEST 123");

            PlayerConfig config = new PlayerConfig.Builder()
                    .playlist(playlist)
                    .autostart(true)
                    .advertising(advertising)
                    .build();

            mPlayerView.setup(config);

        }

        // IMA button clicked
        else {
            List<PlaylistItem> playlist = new ArrayList<>();
            List<AdBreak> adSchedule = new ArrayList<>();

            PlaylistItem item = new PlaylistItem("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8");
            adSchedule.add(new AdBreak("pre", AdSource.IMA, ad));
            ImaAdvertising advertising = new ImaAdvertising(adSchedule);

            PlayerConfig config = new PlayerConfig.Builder()
                    .playlist(playlist)
                    .autostart(true)
                    .advertising(advertising)
                    .build();

            mPlayerView.setup(config);
        }


    }


    /*
     * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false);
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
        Logger.reset();
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
