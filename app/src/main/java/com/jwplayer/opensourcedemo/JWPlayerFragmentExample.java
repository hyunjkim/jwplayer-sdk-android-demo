package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.jwplayer.opensourcedemo.jwutil.Logger;
import com.jwplayer.opensourcedemo.listeners.JWEventHandler;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;

public class JWPlayerFragmentExample extends AppCompatActivity {

    /**
     * A reference to the {@link JWPlayerSupportFragment}.
     */
    private JWPlayerSupportFragment mPlayerFragment;

    /**
     * A reference to the {@link JWPlayerView} used by the JWPlayerSupportFragment.
     */
    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        TextView outputTextView = (TextView) findViewById(R.id.output);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);

        // Setup JWPlayerView
        setupJWPlayer();

        outputTextView.setText(Logger.generateLogLine("JWPlayerFragmentExample \r\nBuild version: " + mPlayerView.getVersionCode()));

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

    }

    /** Setup JW Player
     * More info about our Player Configuration and other available Configurations: {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/package-summary.html}
     * 1 - PlayerConfig
     * 2 - LogoConfig
     * 3 - PlaybackRateConfig
     * 4 - CaptionsConfig
     * 5 - RelatedConfig
     * 6 - SharingConfig
     * 7 - SkinConfig
     */
    private void setupJWPlayer() {

        String url = "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";

        SkinConfig skinConfig = new SkinConfig.Builder()
                .url("https://www.hostingat123.com/mycustomcss.css")
                .name("mycustomcss")
                .build();

        PlayerConfig config = new PlayerConfig.Builder()
                .file(url)
                .autostart(true)
                .skinConfig(skinConfig)
                .build();

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment.newInstance(config);

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
        getMenuInflater().inflate(R.menu.menu_jwplayerfragment, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_view:
                Intent i = new Intent(this, JWPlayerViewExample.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}