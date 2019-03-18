package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.handler.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.myutil.Logger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener,
        View.OnClickListener,
        VideoPlayerEvents.OnControlBarVisibilityListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;


    private AudioManager audioManager;
    private Button volumnUpBtn, volumnDownBtn;
    private TextView outputTextView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        Logger.newInstance();

        mPlayerView = findViewById(R.id.jwplayer);
        outputTextView = findViewById(R.id.output);
        scrollView = findViewById(R.id.scroll);

        volumnUpBtn = findViewById(R.id.volume_up);
        volumnDownBtn = findViewById(R.id.volume_down);
        volumnUpBtn.setOnClickListener(this);
        volumnDownBtn.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        }

        // Setup JWPlayer
        setupJWPlayer();

        outputTextView.setText(Logger.printBuildVersion(mPlayerView.getVersionCode()));

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);
        mPlayerView.addOnControlBarVisibilityListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

    }

    /*
    * Whenever the Control Bar is visible, show the volume buttons
    * */
    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {

        boolean isControlBarVisibile = controlBarVisibilityEvent.isVisible();

        print(" onControlBarVisibilityChanged(): " + isControlBarVisibile);

        if (isControlBarVisibile) {
            print(" onControlBarVisibilityChanged(): show volumes");
            volumnUpBtn.setVisibility(View.VISIBLE);
            volumnDownBtn.setVisibility(View.VISIBLE);
        } else {
            print(" onControlBarVisibilityChanged(): hide volumes");
            volumnUpBtn.setVisibility(View.GONE);
            volumnDownBtn.setVisibility(View.GONE);
        }

    }

    /*
    * Volume up & down button controls
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volume_up:
                print("Volume up!");
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                break;
            case R.id.volume_down:
                print("Volume down!");
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                break;
        }
    }

    /*
     * Setup JW Player
     * */
    private void setupJWPlayer() {

        List<PlaylistItem> playlistItemList = createPlaylist();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .preload(true)
                .allowCrossProtocolRedirects(true)
                .build();

        mPlayerView.setup(config);
    }

    /*
     * Create a Playlist Example
     * */
    private List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    private void print(String s) {
        outputTextView.setText(Logger.updateOutput("JWPLAYERVIEWEXAMPLE " + s));
        scrollView.scrollTo(0, outputTextView.getBottom());

    }
    /*
     * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
//		mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false);
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
