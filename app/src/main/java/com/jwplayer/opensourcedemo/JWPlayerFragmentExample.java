package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.mediarouter.app.MediaRouteButton;


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
     * Reference to the {@link CastManager}
     */
    private CastManager mCastManager;

    /**
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;
    private  MyCastListener castListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView =findViewById(R.id.scroll);

        setupJWPlayer();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Get a reference to the CastManager
        mCastManager = CastManager.getInstance();
        MediaRouteButton mCustomCastBtn = findViewById(R.id.custom_mediaroutebtn_fragment);
        mCastManager.addMediaRouterButton(mCustomCastBtn);
        mCustomCastBtn.setBackgroundColor(Color.WHITE);
        mCustomCastBtn.setVisibility(View.VISIBLE);
        mCustomCastBtn.bringToFront();

        castListener= new MyCastListener(mCustomCastBtn);
        mCastManager.addConnectionListener(castListener);
        mCastManager.addDeviceListener(castListener);
    }

    private void setupJWPlayer() {

        mPlayerFragment = JWPlayerSupportFragment.newInstance(new PlayerConfig.Builder()
                .playlist(createPlaylist())
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

    private List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();


        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8"
        };

        for(String each : playlist){
            playlistItemList.add(new PlaylistItem(each));
        }

        return playlistItemList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
        mCastManager.removeConnectionListener(castListener);
        mCastManager.removeDeviceListener(castListener);
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
