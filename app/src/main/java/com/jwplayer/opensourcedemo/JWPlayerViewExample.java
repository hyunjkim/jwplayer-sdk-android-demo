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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.asynctask.AdvertisingAsyncTask;
import com.jwplayer.opensourcedemo.asynctask.PlaylistIdAysncTask;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SampleAds;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.jwplayer.opensourcedemo.util.JWLogger;
import com.jwplayer.opensourcedemo.asynctask.MediaIdAsyncTask;
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
    private TextView countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        et = findViewById(R.id.enter_url);
        countdown = findViewById(R.id.countdown);
        findViewById(R.id.media_btn).setOnClickListener(this);
        findViewById(R.id.playlist_btn).setOnClickListener(this);
        findViewById(R.id.ad_btn).setOnClickListener(this);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Print JWPlayerVersion
        outputTextView.setText(JWLogger.generateLogLine("Build Version: " + mPlayerView.getVersionCode()));

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        print("onCreate() - setupJWPlayer");
        // I need to know when my SampleAd is ready to setup JWPlayerView
        new MediaIdAsyncTask(this).execute("jumBvHdL");

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    /**
     * When user clicks the button, ID will be passed, Get json response
     */
    @Override
    public void onClick(View v) {
        String id = et.getText().toString();
        if (id.length() > 0) {
            switch (v.getId()) {
                case R.id.media_btn:
                    new MediaIdAsyncTask(this).execute(id);
                    break;
                case R.id.playlist_btn:
                    new PlaylistIdAysncTask(this).execute(id);
                    mSampleAds = null;
                    break;
                case R.id.ad_btn:
                    new AdvertisingAsyncTask(this).execute(id);
                    break;
            }
            et.setText("");
        }

    }
    @Override
    public void clear(){
        String reset = "0:00";
        countdown.setText(reset);
    };

    @Override
    public void countDown(String count) {
        Log.i("HYUNJOO", "countdown: " + count);
        String loading = "Loading: " + count;
        countdown.setText(loading);
    }

    @Override
    public void beforeExecute() {
        String downloading = "Please wait...It is downloading";
        countdown.setText(downloading);
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