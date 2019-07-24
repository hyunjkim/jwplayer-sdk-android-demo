package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SampleAds;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.jwplayer.opensourcedemo.util.JWLogger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdvertisingBase;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity implements
        View.OnClickListener,
        VideoPlayerEvents.OnFullscreenListener,
        MyThreadListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;


    private SampleAds mSampleAds;
    private SamplePlaylist mSamplePlaylist;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        et = findViewById(R.id.enter_url);
        findViewById(R.id.media_btn).setOnClickListener(this);
        findViewById(R.id.playlist_btn).setOnClickListener(this);
        findViewById(R.id.ad_btn).setOnClickListener(this);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Print JWPlayerVersion
        outputTextView.setText(JWLogger.generateLogLine("Build Version: " + mPlayerView.getVersionCode()));

        // I need to track the JSON advertising
        MyThreadListener listener = this;

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                print("onCreate() - setupJWPlayer");

                // I need to know when my SampleAd is ready to setup JWPlayerView
                mSampleAds = new SampleAds(listener);
                mSamplePlaylist = new SamplePlaylist(listener);

                // Get JSON Advertising Schedule
                mSampleAds.getJSONAdvertising("02U1YHTW");
                mSamplePlaylist.getJSONPlaylistItem("jumBvHdL");
            }
        });

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    /**
    * When user clicks the button, ID will be passed, Get json response
    *
     * @see SamplePlaylist#getJSONPlaylistItem(java.lang.String)
     * @see SamplePlaylist#getJSONPlaylist(java.lang.String)
     * @see SampleAds#getJSONAdvertising(java.lang.String)
    * */
    @Override
    public void onClick(View v) {
        String id = et.getText().toString();
        if (id.length() > 0) {
            switch (v.getId()) {
                case R.id.media_btn:
                    runOnUiThread(() -> mSamplePlaylist.getJSONPlaylistItem(id));
                    mSampleAds = null;
                    break;
                case R.id.playlist_btn:
                    runOnUiThread(() -> mSamplePlaylist.getJSONPlaylist(id));
                    mSampleAds = null;
                    break;
                case R.id.ad_btn:
                    runOnUiThread(() -> mSampleAds.getJSONAdvertising(id));
                    break;
            }
            et.setText("");
        }

    }

    /**
     * Setup JWPlayerView Example
     */
    @Override
    public void setupJWPlayer() {

        print("setupJWPlayer()");

        List<PlaylistItem> playlistItemList = SamplePlaylist.getPlaylist();

        AdvertisingBase advertising = null;

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .allowCrossProtocolRedirects(true)
                .autostart(true)
                .build();

        // If I get nothing back from the JSONAdvertising method, then I will have fallback
        if (mSampleAds != null) {
            advertising = mSampleAds.getClient().equals("ima") ? mSampleAds.getImaAd() : mSampleAds.getVastAd();
            config.setAdvertising(advertising);
        }
        mPlayerView.setup(config);

        SamplePlaylist.stopThreads();
        SampleAds.stopThreads();
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


    /*
     * Upper right corner, drop down menu XML
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton on the JW Player SDK
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);

        return true;
    }

    /*
     * The items to add to the list on the drop down menu
     * */
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

    private void print(String s) {
        Log.i("JWPLAYERVIEWEXAMPLE", "JSON OBJECT RESPONSE: " + s);
    }

}