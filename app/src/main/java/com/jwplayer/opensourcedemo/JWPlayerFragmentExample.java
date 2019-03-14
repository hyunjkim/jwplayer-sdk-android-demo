package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerFragmentExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener,
        MyClickListener {

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

    private LinearLayout mLinearLayout;
    private List<PlaylistItem> playlistItemList;
    private RecyclerView mRecyclerView;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);
        mLinearLayout = findViewById(R.id.activity_jwplayerfragment);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        // Set up JWPlayerView
        setupJWPlayer();

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView);

        // Get a reference to the CastManager
        mCastManager = CastManager.getInstance();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(new MyRecyclerAdapter(playlistItemList, this));
    }

    private void setupJWPlayer() {
        playlistItemList = createPlaylist();
        PlayerConfig config = getJWPlayerConfig(playlistItemList.get(0).getFile());
        addFragmentBackToStack(config);
        mPlayerView.addOnFullscreenListener(this);
    }

    private PlayerConfig getJWPlayerConfig(String videoURL) {
        return new PlayerConfig.Builder()
                .file(videoURL)
                .autostart(true)
                .build();
    }

    @Override
    public void click(int childAdapterPosition) {
        String video = playlistItemList.get(childAdapterPosition).getFile();
        addFragmentBackToStack(getJWPlayerConfig(video));
    }

    public void addFragmentBackToStack(PlayerConfig config) {

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment
                .newInstance(config);

        fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment_container, mPlayerFragment)
                .addToBackStack(null)
                .commit();

        // Make sure all the pending fragment transactions have been completed, otherwise mPlayerFragment.getPlayer() may return null
        fm.executePendingTransactions();

        // Get a reference to the JWPlayerView from the fragment
        mPlayerView = mPlayerFragment.getPlayer();
    }

    private List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4"
        };

        String[] mediaid = {
                "jumBvHdL",
                "tkM1zvBq",
                "RDn7eg0o",
                "i3q4gcBi",
                "iLwfYW2S",
                "8TbJTFy5",
                "jumBvHdL",
                "tkM1zvBq",
                "RDn7eg0o",
                "i3q4gcBi",
                "iLwfYW2S",
                "8TbJTFy5"
        };

        /*
         * String image file to bitmap to drawable
         * */
        for (int i = 0; i < playlist.length; i++) {
            PlaylistItem playlistItem = new PlaylistItem.Builder()
                    .file(playlist[i])
                    .title(mediaid[i])
                    .image("https://cdn.jwplayer.com/thumbs/" + mediaid[i] + "-320.jpg")
                    .build();
            playlistItemList.add(playlistItem);
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
            if(mPlayerFragment != null){
                Toast.makeText(this,"mPlayerFragment not empty", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerfragment, menu);
        // Register the MediaRouterButton on the JW Player SDK
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
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

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mLinearLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
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


}
