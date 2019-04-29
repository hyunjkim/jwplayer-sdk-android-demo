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
import com.google.android.gms.cast.framework.CastContext;
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
    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;
    /**
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;
    private TextView outputTextView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        outputTextView = (TextView) findViewById(R.id.output);
        scrollView = (ScrollView) findViewById(R.id.scroll);


        // Setup JWPlayer
        setupJWPlayer();

        // Display JWPlayer version
        Logger.newStringBuilder();
        String jwplayerBuildVersion = Logger.updateOutput("Build Version: " + mPlayerView.getVersionCode() + "\r\nJWPlayer Fragment Example");
        outputTextView.setText(jwplayerBuildVersion);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    private void setupJWPlayer() {

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        SkinConfig skinConfig = new SkinConfig.Builder()
                .name("bekle")
                .url("https://ssl.p.jwpcdn.com/player/v/7.2.3/skins/bekle.css")
                .build();

        mPlayerFragment = JWPlayerSupportFragment.newInstance(new PlayerConfig.Builder()
                .file("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .skinConfig(skinConfig)
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

        // Register the MediaRouterButton on the JW Player SDK
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
