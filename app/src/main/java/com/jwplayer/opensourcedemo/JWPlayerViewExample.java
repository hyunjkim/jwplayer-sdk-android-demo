package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.jwplayer.opensourcedemo.handlers.CustomSessionHandler;
import com.jwplayer.opensourcedemo.listeners.CustomSessionListener;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.listeners.ValidateMenuListener;
import com.jwplayer.opensourcedemo.myutility.Logger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerViewExample extends AppCompatActivity implements
        ValidateMenuListener,
        CustomSessionListener,
        VideoPlayerEvents.OnFullscreenListener {


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

    /*
     * Reference to {@link Toolbar}
     * */
    private Toolbar mToolbar;
    /*
     *  Copied from https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L195
     */
    private IntroductoryOverlay mIntroductoryOverlay;

    private CastStateListener mCastStateListener;
    private MenuItem item;
    private CastSession mCastSession;
    private CustomSessionHandler customSessionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        // https://github.com/googlecast/CastVideos-android/blob/master/res/layout/video_browser.xml
        mToolbar = findViewById(R.id.my_toolbar);

        // Setup ActionBar and styling the Toolbar: https://guides.codepath.com/android/using-the-app-toolbar#styling-the-toolbar
        setupActionBar();

        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);

        outputTextView.setText(Logger.log(mPlayerView.getVersionCode()));

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Setup JWPlayer
        setupJWPlayerPlaylistItem();
//		setupJWPlayerPlayConfigWithEmptyCaptions();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Adevent handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the CastStateListener - https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L114
        mCastStateListener = newState -> {
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                Log.i("HYUNJOO", "mCastStateListener ");
                showIntroductoryOverlay();
            }
        };

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

        // Instantiate the Session Handler and inokes invalidateOptionMenu
        customSessionHandler = new CustomSessionHandler(this, this, mCastSession);
    }

    /*
     * Custom ToolBar
     * More info: https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L125
     */
    private void setupActionBar() {

        // Set my toolbar in the layout as my actionbar
        setSupportActionBar(mToolbar);

        // Do not display the Default App Name Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    private void setupJWPlayerPlaylistItem() {
        String bipbop = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";
        String mp4 = "https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";

        PlaylistItem video = new PlaylistItem.Builder()
                .file(mp4)
                .build();

        mPlayerView.load(video);
    }

    private void setupJWPlayerPlayConfigWithEmptyCaptions() {
        String captionVideo = "http://cdnbakmi.kaltura.com/p/243342/sp/24334200/playManifest/entryId/0_uka1msg4/flavorIds/1_vqhfu6uy,1_80sohj7p/format/applehttp/protocol/http/a.m3u8";

        PlaylistItem video = new PlaylistItem.Builder()
                .file(captionVideo)
                .build();

        List<Caption> captionTracks = new ArrayList<>();
        Caption emptyCaps = new Caption("file_en.srt");
        captionTracks.add(emptyCaps);
        video.setCaptions(captionTracks);

        List<PlaylistItem> item = new ArrayList<>();
        item.add(video);
        mPlayerView.setup(new PlayerConfig.Builder()
                .playlist(item)
                .build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        Log.i("HYUNJOO", "onConfigurationChanged()");
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        invalidateOptionsMenu();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        mCastContext.addCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().addSessionManagerListener(customSessionHandler, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this)
                    .getSessionManager()
                    .getCurrentCastSession();
        }
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        mPlayerView.onPause();
        mCastContext.removeCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().removeSessionManagerListener(customSessionHandler, CastSession.class);
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
    public void onFullscreen(FullscreenEvent fullscreenEvent) {

        if (mToolbar != null) {
            if (fullscreenEvent.getFullscreen()) {
                mToolbar.hideOverflowMenu();
            } else {
                mToolbar.showOverflowMenu();
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton
        item = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }

    /*
     * Credits to Google:
     *
     * Google Documentation: https://developers.google.com/android/reference/com/google/android/gms/cast/framework/IntroductoryOverlay
     * Github: https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L191
     * */
    public void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((item != null) && item.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(JWPlayerViewExample.this, item)
                            .setTitleText("Cast")
                            .setOverlayColor(R.color.white)
                            .setSingleTime()
                            .setOnOverlayDismissedListener(new IntroductoryOverlay.OnOverlayDismissedListener() {
                                @Override
                                public void onOverlayDismissed() {
                                    mIntroductoryOverlay = null;
                                }
                            })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.switch_to_fragment) {
            Intent i = new Intent(this, JWPlayerFragmentExample.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callInvalidateOptionMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void castSessionInformation(CastSession session) {
        mCastSession = session;
    }
}
