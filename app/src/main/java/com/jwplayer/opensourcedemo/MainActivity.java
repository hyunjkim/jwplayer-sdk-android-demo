package com.jwplayer.opensourcedemo;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Reference to the {@link FrameLayout}
     */
    private FrameLayout fragmentContainer;
    private RecyclerView mRecyclerView;
    private List<PlaylistItem> playlistItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mainactivity);

        fragmentContainer = findViewById(R.id.container);
        mRecyclerView = findViewById(R.id.rv);

        Thread t = new Thread(() -> {
            // Setup the Playlist for the recyclerview
            playlistItemList = createPlaylist();
        });
        t.start();

        Handler handler = new Handler(getMainLooper());

        Runnable runnable = () -> {
            // Setup JWPlayerView Fragment, play the first item on the playlist
            setupJWPlayerFrag(new VideoDetailFragment());

            // Setup RecyclerView
            setupRecyclerView();
        };
        handler.postDelayed(runnable, 2000);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

    }

    /*
     * JWPlayerView using Support Fragments in One Activity Example
     * */
    public void setupJWPlayerFrag(Fragment fragment) {
        // Attach the Fragment to our layout
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragmentContainer.getId(), fragment);
        ft.addToBackStack(null);
        ft.commit();

        // Make sure all the pending fragment transactions have been completed, otherwise
        fm.executePendingTransactions();
    }

    /*
     * Default playlist
     * */
    private List<PlaylistItem> createPlaylist() {

        List<PlaylistItem> list = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                byte[] response = Util.executePost("https://cdn.jwplayer.com/v2/playlists/l3EGQ9TD");
                String json = new String(response, StandardCharsets.UTF_8);
                JSONArray playlist = new JSONObject(json).getJSONArray("playlist");
                list = PlaylistItem.listFromJson(playlist);

            } else {
                list = new ArrayList<>();
                list.add(
                        new PlaylistItem.Builder()
                                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                                .image("https://cdn.jwplayer.com/v2/media/jumBvHdL/poster.jpg")
                                .build());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
     * Setup RecyclerView in Grid view
     * */
    private void setupRecyclerView() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        MyRecyclerItemTouchListener myRecyclerItemTouchListener =
                new MyRecyclerItemTouchListener(this, mRecyclerView, new MyRecyclerItemTouchListener.MyClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        // Values are passing to activity & to fragment as well
                        Toast.makeText(MainActivity.this,
                                "Single Click on position :" + position,
                                Toast.LENGTH_SHORT).show();

                        String fileClicked = playlistItemList.get(position).getSources().get(0).getFile();

                        VideoDetailFragment videoDetailFragment = new VideoDetailFragment();
                        videoDetailFragment.passFile(fileClicked);

                        setupJWPlayerFrag(videoDetailFragment);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Toast.makeText(MainActivity.this,
                                "Long press on position :" + position,
                                Toast.LENGTH_LONG).show();
                        // TODO: remove the item from the playlist
                    }
                });

        mRecyclerView.addOnItemTouchListener(myRecyclerItemTouchListener);
        mRecyclerView.setAdapter(new MyRecyclerAdapter(playlistItemList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
        // Register the MediaRouterButton on the JW Player SDK

        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }
}
