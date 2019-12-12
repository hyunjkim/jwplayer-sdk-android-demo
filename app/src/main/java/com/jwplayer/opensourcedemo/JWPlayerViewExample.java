package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String mp3 = "https://ccrma.stanford.edu/~jos/mp3/oboe-bassoon.mp3";

        MediaSource ms = new MediaSource.Builder()
                .file(mp3)
                .type(MediaType.MP3)
                .build();
        mediaSourceList.add(ms);

        PlaylistItem item = new PlaylistItem.Builder()
                .sources(mediaSourceList)
                .image("https://d3el35u4qe4frz.cloudfront.net/bkaovAYt-480.jpg")
                .build();

        playlistItemList.add(item);

        SkinConfig skinConfig = new SkinConfig.Builder()
                .url("https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/css/smallplayer.css")
                .name("smallplayer")
                .build();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .skinConfig(skinConfig)
                .build();

        mPlayerView.setup(config);
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
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE,
                true);
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
}
