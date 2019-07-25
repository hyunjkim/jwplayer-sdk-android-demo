package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.sample.SamplePlaylist;
import com.jwplayer.opensourcedemo.utilities.JWLogger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER;


public class JWPlayerViewExample extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;
    /**
     * Reference to the {@link CastManager}
     */
    private CastManager mCastManager;
    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mLinearLayout;
    private int currentOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mLinearLayout = findViewById(R.id.linearlayout);
        mPlayerView = findViewById(R.id.jwplayer);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        outputTextView.setText(JWLogger.generateLogLine("Build version: " + mPlayerView.getVersionCode()));

        // Setup JWPlayer
        setupJWPlayer();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastManager
        mCastManager = CastManager.getInstance();
    }


    /*
    * Setup JWPlayer
    * */
    private void setupJWPlayer() {

        List<PlaylistItem> mediaSourceExample = SamplePlaylist.getMediaSourceExample();
        mPlayerView.load(mediaSourceExample);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        // Set fullscreen when the device is rotated to landscape
        currentOrientation = newConfig.orientation;
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
    public void onFullscreen(boolean fullscreenEvent) {

        View jwplayer = mLinearLayout.getChildAt(0); // this is technically the FrameLayout within the LinearLayout

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (fullscreenEvent) {
                if (currentOrientation == SCREEN_ORIENTATION_LANDSCAPE || currentOrientation == SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
                }
                // Hide Action bar
                actionBar.hide();

                // Maximize Player
                LinearLayout.LayoutParams toFullscreen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                jwplayer.setLayoutParams(toFullscreen);
            } else {

                if (currentOrientation == SCREEN_ORIENTATION_LANDSCAPE || currentOrientation == SCREEN_ORIENTATION_USER)
                setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

                // Show Action bar
                actionBar.show();

                // Minimize JW Player
                LinearLayout.LayoutParams toMinimize = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
                jwplayer.setLayoutParams(toMinimize);
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent);

        // Reset Orientation settings to user's choice
        setRequestedOrientation(SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
        // Register the MediaRouterButton on the JW Player SDK
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
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
