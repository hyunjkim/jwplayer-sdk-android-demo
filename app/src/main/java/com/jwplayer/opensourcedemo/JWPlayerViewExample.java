package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.jwutil.Logger;
import com.jwplayer.opensourcedemo.listeners.JWAdEventHandler;
import com.jwplayer.opensourcedemo.listeners.JWEventHandler;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

//import com.jwplayer.opensourcedemo.listeners.JWAdEventHandler;

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
     * {@link - http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html}
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

        findViewById(R.id.audioplaylist1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupJWPlayer("1");
            }
        });

        findViewById(R.id.audioplaylist2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupJWPlayer("");
            }
        });

        // Print JWPlayer Version
        outputTextView.setText(Logger.generateLogLine("JWPlayerViewExample \r\nBuild version: " + mPlayerView.getVersionCode()));

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Setup JWPlayer
        setupJWPlayer("");

        // CastContext is lazily initialized when the CastContext.getSharedInstance() is called.
        mCastContext = CastContext.getSharedInstance(this);
    }

    /**
     * Setup JW Player
     * <p>
     * 1 - PlayerConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlayerConfig.Builder.html}
     * 2 - LogoConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/LogoConfig.html}
     * 3 - PlaybackRateConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlaybackRateConfig.html}
     * 4 - CaptionsConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/CaptionsConfig.html}
     * 5 - RelatedConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/RelatedConfig.html}
     * 6 - SharingConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SharingConfig.html}
     * 7 - SkinConfig - {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SkinConfig.Builder.html}
     * <p>
     * More info about our Player Configuration and other available Configurations:
     * {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/package-summary.html}
     */
    private void setupJWPlayer(String setup) {

        List<PlaylistItem> playlistItemList;

        if(setup.equals("1")){
            playlistItemList = SamplePlaylist.createAudioPlaylist();
        } else {
            playlistItemList = SamplePlaylist.createAudioMediaSourcePlaylist();
        }

        // PlayerConfig
        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .preload(true)
                .allowCrossProtocolRedirects(true)
                .build();

        mPlayerView.setup(config);
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
