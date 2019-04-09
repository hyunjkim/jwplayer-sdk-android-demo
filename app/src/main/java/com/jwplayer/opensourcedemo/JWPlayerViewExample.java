package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
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


public class JWPlayerViewExample extends AppCompatActivity
        implements VideoPlayerEvents.OnFullscreenListener {

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

    private String ad = "vast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        Button adTestButton = findViewById(R.id.ad_test_button);

        adTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adTestButton.getText().toString().equals("Vast")) {
                    ad = "vast";
                    adTestButton.setText(String.format("%s", "Ima"));
                } else {
                    ad = "ima";
                    adTestButton.setText(String.format("%s", "Vast"));
                }
                playAd();
            }

        });

        // Setup JW Player
        setupJWPlayer();

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        mEventHandler = new JWEventHandler(mPlayerView, outputTextView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    private void playAd() {
        if (mPlayerView != null) {
            if (mPlayerView.getState().equals(PlayerState.PLAYING)) {
                mPlayerView.stop();
            }
            switch (ad) {
                case "vast":
                    mPlayerView.playAd(AdSource.VAST, "https://playertest.longtailvideo.com/vast-30s-ad.xml");
                    break;
                default:
                    mPlayerView.playAd(AdSource.IMA, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=");
                    break;

            }
        } else setupJWPlayer();
    }

    /*
     * Setup JW Player
     */
    private void setupJWPlayer() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        PlaylistItem item = getPlaylistItem();
        playlistItemList.add(item);

        List<AdBreak> adbreakList = getAdBreakList();
        Advertising advertising = (ad.equals("ima")) ? getImaAd(adbreakList) : getVASTAd(adbreakList);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .preload(true)
                .build();

        mPlayerView.setup(config);
    }

    /*
     * AdBreakList Example
     */
    private List<AdBreak> getAdBreakList() {
        List<AdBreak> adbreakList = new ArrayList<>();
        adbreakList.add(getAdBreak());
        return adbreakList;
    }

    /*
     * Simple Playlist Item Example
     */
    private PlaylistItem getPlaylistItem() {
        return new PlaylistItem.Builder()
                .file("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .build();
    }

    /*
     * AdBreak Example
     */
    private AdBreak getAdBreak() {
        String jwvasttag = "https://playertest.longtailvideo.com/vast-30s-ad.xml";
        String ima = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";
        return ad.equals("ima") ? new AdBreak("pre", AdSource.IMA, ima) : new AdBreak("pre", AdSource.VAST, jwvasttag);
    }

    /*
     * VAST Ad Example
     */
    private Advertising getVASTAd(List<AdBreak> adbreakList) {
        return new Advertising(AdSource.VAST, adbreakList);
    }

    /*
     * IMA Ad Example
     */
    private ImaAdvertising getImaAd(List<AdBreak> adbreakList) {
        return new ImaAdvertising(adbreakList);
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

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);

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
