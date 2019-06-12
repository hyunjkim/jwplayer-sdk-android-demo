package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.RelatedConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import static com.longtailvideo.jwplayer.configuration.RelatedConfig.RELATED_DISPLAY_MODE_OVERLAY;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

        mPlayerView.addOnFullscreenListener(this);

        // Log JWPlayerVersion Build number
        outputTextView.setText(Logger.generateLogLine("Build Version: " + mPlayerView.getVersionCode()));

        // Setup JWPlayer with Related Plugin
        setupOverlay();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);


    }

    /*
     * Setup RelatedConfig Example with JWPlayerView
     * */
    private void setupOverlay() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "https://cdn.jwplayer.com/manifests/tkM1zvBq.m3u8",
                "http://content.jwplatform.com/videos/RDn7eg0o.mp4",
                "https://content.jwplatform.com/videos/i3q4gcBi.mp4"
        };

        String [] mediaid = {"jumBvHdL","tkM1zvBq","RDn7eg0o","i3q4gcBi"};

        String relatedFile = "https://content.bitsontherun.com/feeds/482jsTAr.rss";

        for (String each : playlist) {

            String id = mediaid[playlistItemList.size()];
            String image = String.format("https://cdn.jwplayer.com/v2/media/%s/poster.jpg",id);

            PlaylistItem item = new PlaylistItem.Builder()
                    .file(each)
                    .image(image)
                    .title("Hi There: "+ id)
                    .description("My Description: "+ id)
                    .recommendations(relatedFile)
                    .build();

            playlistItemList.add(item);
        }

        RelatedConfig relatedConfig = new RelatedConfig.Builder()
                .file(relatedFile)
                .autoPlayMessage("HYUNJOO Message!")
                .autoPlayTimer(3)
                .onComplete(RelatedConfig.RELATED_ON_COMPLETE_AUTOPLAY)
                .onClick(RelatedConfig.RELATED_ON_CLICK_PLAY)
                .displayMode(RELATED_DISPLAY_MODE_OVERLAY)
                .build();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
//                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .relatedConfig(relatedConfig)
                .allowCrossProtocolRedirects(true)
                .preload(true)
                .displayTitle(true)
                .displayDescription(true)
                .autostart(false)
                .build();

        mPlayerView.setup(config);
    }

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
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
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
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton on the JW Player SDK
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
