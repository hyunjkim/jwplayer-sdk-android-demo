package com.jwplayer.opensourcedemo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String,Drawable> images;
    private AssetManager manager;
    private RecyclerView mRecyclerView;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        mLinearLayout = findViewById(R.id.activity_jwplayerfragment);

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
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        createBitmapToDrawable();
        mRecyclerView.setAdapter(new MyRecyclerAdapter(playlistItemList, this));
    }

    private void createBitmapToDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            playlistItemList.forEach(e -> {
                InputStream inputStream = null;
                try {
                    inputStream = manager.open(e.getImage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                images.put(e.getMediaId(), new BitmapDrawable(getResources(),bitmap));
            });
        }
    }


    private void setupJWPlayer() {
        playlistItemList = createPlaylist();

        PlayerConfig config = new PlayerConfig.Builder()
                .file(playlistItemList.get(0).getFile())
                .build();

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment
                .newInstance(config);

        // Attach the Fragment to our layout
        attachFragment();

        // Get a reference to the JWPlayerView from the fragment
        mPlayerView = mPlayerFragment.getPlayer();
        mPlayerView.addOnFullscreenListener(this);
    }

    @Override
    public void click(int childAdapterPosition) {
        mPlayerView.load(playlistItemList.get(childAdapterPosition));
        mPlayerView.play();
    }

    public void attachFragment(){
        fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment_container, mPlayerFragment)
                .commit();

        // Make sure all the pending fragment transactions have been completed, otherwise mPlayerFragment.getPlayer() may return null
        fm.executePendingTransactions();
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
        for(int i = 0; i < playlist.length; i++){
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

}
