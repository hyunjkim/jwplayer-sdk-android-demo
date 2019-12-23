package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jwplayer.opensourcedemo.asynctask.AdvertisingAsyncTask;
import com.jwplayer.opensourcedemo.asynctask.MediaIdAsyncTask;
import com.jwplayer.opensourcedemo.asynctask.PlaylistIdAysncTask;
import com.jwplayer.opensourcedemo.listener.CallbackScreen;
import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SampleAds;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.ads.AdvertisingBase;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity implements
        View.OnClickListener,
        MyThreadListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    private SampleAds mSampleAds;
    private EditText et;
    private TextView countdown;
    private MediaIdAsyncTask mediaAsync;
    private PlaylistIdAysncTask playAsync;
    private AdvertisingAsyncTask adAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        et = findViewById(R.id.enter_url);
        countdown = findViewById(R.id.countdown);

        findViewById(R.id.media_btn).setOnClickListener(this);
        findViewById(R.id.playlist_btn).setOnClickListener(this);
        findViewById(R.id.ad_btn).setOnClickListener(this);

        print("onCreate() - setupJWPlayer");

        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
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
                    mediaAsync = new MediaIdAsyncTask(this);
                    mediaAsync.execute(id);
                    break;
                case R.id.playlist_btn:
                    playAsync = new PlaylistIdAysncTask(this);
                    playAsync.execute(id);
                    break;
                case R.id.ad_btn:
                    mSampleAds = null;
                    adAsync = new AdvertisingAsyncTask(this);
                    adAsync.execute(id);
                    mSampleAds = new SampleAds();
                    break;
            }
            et.setText("");
        }

    }

    @Override
    public void clear() {
        String reset = "0:00";
        countdown.setText(reset);
    }

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
        cancelRunningAsyncTasks();
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

        // I need to know when my SampleAd is ready to setup JWPlayerView
        mediaAsync = new MediaIdAsyncTask(this);
        mediaAsync.execute("jumBvHdL");
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

    public void cancelRunningAsyncTasks() {
        print(" - cancel asyncs");
        if (mediaAsync != null) mediaAsync.cancel(true);
        if (playAsync != null) playAsync.cancel(true);
        if (adAsync != null) adAsync.cancel(true);
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        print(" - onDestroy()");
        mPlayerView.onDestroy();
        super.onDestroy();
        mediaAsync = null;
        playAsync = null;
        adAsync = null;
        cancelRunningAsyncTasks();
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

    private void print(String s) {
        Log.i("JWPLAYERVIEWEXAMPLE", " - HYUNJOO - " + s);
    }

}