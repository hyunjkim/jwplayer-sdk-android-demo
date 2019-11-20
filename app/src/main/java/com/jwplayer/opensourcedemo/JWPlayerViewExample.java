package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwAdvertising;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwSettings;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity
        implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;

    private CastContext mCastContext;

    private CallbackScreen mCallbackScreen;

    private ConstraintLayout.LayoutParams minimizedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Save the values Minimized layout param values
        minimizedView = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        mPlayerView.setFullscreenHandler(new FullscreenHandler() {

            @Override
            public void onFullscreenRequested() {

                // Set window fullscreen and remove title bar, and force landscape orientation
                ConstraintLayout.LayoutParams fullscreenParams =
                        new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                ConstraintLayout.LayoutParams.MATCH_PARENT);

                mPlayerView.setLayoutParams(fullscreenParams);

            }

            @Override
            public void onFullscreenExitRequested() {

                mPlayerView.setLayoutParams(minimizedView);

            }

            @Override
            public void onResume() {
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onDestroy() {
            }

            @Override
            public void onAllowRotationChanged(boolean b) {
            }

            @Override
            public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            }

            @Override
            public void setUseFullscreenLayoutFlags(boolean b) {
            }
        });

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Event Logging
        mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);


        findViewById(R.id.hello_btn).setOnClickListener(v -> updateTitle("~Hello~"));
        findViewById(R.id.bye_btn).setOnClickListener(v -> updateTitle("Goodbye!"));

        // Load a media source
        PlaylistItem pi = new PlaylistItem.Builder()
                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .build();

        mPlayerView.load(pi);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    public void updateTitle(String name) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Declare that the options menu has changed, so should be recreated.
        // The onCreateOptionsMenu(Menu) method will be called the next time it needs to be displayed.
        invalidateOptionsMenu();
    }

    public void setupFreeWheel() {
        // Create your FreeWheel advertising settings
        int networkId = 42015;
        String serverId = "http://7cee0.v.fwmrm.net/";
        String profileId = "fw_tutorial_android";
        String sectionId = "fw_tutorial_android";
        String mediaId = "fw_simple_tutorial_asset";
        FwSettings settings = new FwSettings(networkId, serverId, profileId, sectionId, mediaId);

        // Create advertising object
        FwAdvertising advertising = new FwAdvertising(settings);

        // Construct a new AdBreak with a specific offset
        // This AdBreak will play a midroll at 10%
        AdBreak adBreak = new AdBreak("10%", AdSource.FW, "placeholder_string");

        // Add the AdBreak to a List
        List<AdBreak> schedule = new ArrayList<>();
        schedule.add(adBreak);

        // Build a PlaylistItem and assign the schedule
        PlaylistItem video = new PlaylistItem.Builder()
                .file("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .adSchedule(schedule)
                .build();

        // Add the PlaylistItem to a List
        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(video);

        // Build the PlayerConfig
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(advertising)
                .build();

        // Setup the player
        mPlayerView.setup(playerConfig);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        // Set fullscreen when the device is rotated to landscape
//        currentConfig = newConfig.orientation;
//        setRequestedOrientation(newConfig.orientation);

        super.onConfigurationChanged(newConfig);
    }


    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mPlayerView.setFullscreen(true, true);
//        } else {
//            mPlayerView.setFullscreen(false,false);
//        }
//        super.onConfigurationChanged(newConfig);
//    }
//
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
