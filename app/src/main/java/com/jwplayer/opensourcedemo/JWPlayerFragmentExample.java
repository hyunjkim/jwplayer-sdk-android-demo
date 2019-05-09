package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.myutility.Logger;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class JWPlayerFragmentExample extends AppCompatActivity{

    /**
     * A reference to the {@link JWPlayerSupportFragment}.
     */
    private JWPlayerSupportFragment mPlayerFragment;

    /**
     * A reference to the {@link JWPlayerView} used by the JWPlayerSupportFragment.
     */
    private JWPlayerView mPlayerView;

    private LinearLayout mLinearLayout;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        mToolbar = findViewById(R.id.my_toolbar);
        mLinearLayout = findViewById(R.id.linear_layout);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);

        // Setup JW PlayerView
        setupJWPlayer();

        // Display JWPlayer Version
        Logger.refresh();
        outputTextView.setText(Logger.log(mPlayerView.getVersionCode() + "JWPLAYERFRAGMENT"));

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Adevent handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);
    }
    /*
     * Setup JWPlayer
     */
    private void setupJWPlayer() {

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment.newInstance(new PlayerConfig.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .build());

        // Attach the Fragment to our layout
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, mPlayerFragment);
        ft.commit();

        // Make sure all the pending fragment transactions have been completed, otherwise
        // mPlayerFragment.getPlayer() may return null
        fm.executePendingTransactions();

        // Get a reference to the JWPlayerView from the fragment
        mPlayerView = mPlayerFragment.getPlayer();

        // setup Action Bar
        setupActionBar();
    }

    /*
     * Custom ToolBar
     *
     * More info: https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L125
     */
    private void setupActionBar() {
        Logger.logEvent("FRAGMENTEXAMPLE - setupActionBar");
        setSupportActionBar(mToolbar);

        // Do not display the Default App Name Title
        if (getSupportActionBar() != null && !mPlayerView.getFullscreen()) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        Logger.logEvent("FRAGMENTEXAMPLE - onConfigurationChanged()");
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        setupActionBar();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_jwplayerfragment, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.switch_to_view) {
            Intent i = new Intent(this, JWPlayerViewExample.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
