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

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

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
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        TextView outputTextView = (TextView)findViewById(R.id.output);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);

        setupJWPlayer();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);
    }

    private void setupJWPlayer() {

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment.newInstance(new PlayerConfig.Builder()
                .playlist(createDRMPlaylist())
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

    private List<PlaylistItem> createDRMPlaylist() {

        List<PlaylistItem> playlistItemList = new ArrayList<>();
        WidevineMediaDrmCallback widevineMediaDrmCallback = new WidevineMediaDrmCallback();

        String[] drmPlaylist = {
                "https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd",
                "https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd",
                "https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd",
        };

        String[] nonDRM = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
        };

        for(int i = 0; i < drmPlaylist.length; i++){

            List<MediaSource> mediaSourceList = new ArrayList<>();

            MediaSource ms = new MediaSource.Builder()
                    .file(drmPlaylist[i])
                    .type(MediaType.MPD)
                    .build();

            mediaSourceList.add(ms);

            // Add this for every other DRM content, to test if the content still works when it is mixed
            if(i < nonDRM.length) {
                playlistItemList.add(new PlaylistItem.Builder()
                        .file(nonDRM[i])
                        .build());
            }

            playlistItemList.add(new PlaylistItem.Builder()
                    .sources(mediaSourceList)
                    .mediaDrmCallback(widevineMediaDrmCallback)
                    .build());
        }
        return playlistItemList;
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
