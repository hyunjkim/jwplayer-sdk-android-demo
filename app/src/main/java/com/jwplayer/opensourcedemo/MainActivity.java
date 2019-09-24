package com.jwplayer.opensourcedemo;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
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
    private VideoDetailFragment mCurrFragment = new VideoDetailFragment();
    private MyRecyclerAdapter mRecyclerViewAdapter;
    private MyRecyclerItemTouchListener myRecyclerItemTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mainactivity);

        fragmentContainer = findViewById(R.id.container);
        mRecyclerView = findViewById(R.id.rv);

        // Setup RecyclerView
        setupRecyclerView();

        AsyncTask.execute(() -> {
            // Setup the Playlist for the recyclerview
            playlistItemList = createPlaylist();
        });

        // Setup JWPlayerView Fragment, play the first item on the playlist
        setupJWPlayerFrag();

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

    }

    /*
     * JWPlayerView using Support Fragments in One Activity Example
     * */
    public void setupJWPlayerFrag() {

        if (mCurrFragment == null) {
            mCurrFragment = new VideoDetailFragment();
        }

        // Attach the Fragment to our layout
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(fragmentContainer.getId(), mCurrFragment)
                .addToBackStack(null)
                .commit();

        //retain their instance for orientation reasons
        mCurrFragment.setRetainInstance(true);

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
                list.add(new PlaylistItem.Builder()
                        .file("https://cdn-videos.akamaized.net/btv/desktop/fastly/us/live/primary.m3u8")
                        .image("https://cdn.jwplayer.com/v2/media/jumBvHdL/poster.jpg")
                        .build());

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

        mRecyclerViewAdapter.updatePlaylist(list);

        return list;
    }

    /*
     * Setup RecyclerView in Grid view
     * */
    private void setupRecyclerView() {

        mRecyclerViewAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        myRecyclerItemTouchListener = new MyRecyclerItemTouchListener(this, mRecyclerView, new MyRecyclerItemTouchListener.MyClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if (mCurrFragment != null) {

                    // Release resources related to JWPlayerView
                    mCurrFragment.onStop();
                }

                // Create new Fragment
                mCurrFragment = new VideoDetailFragment();

                // Toast
                Toast.makeText(MainActivity.this, "File :" + position, Toast.LENGTH_SHORT).show();

                // Values are passing to activity & to fragment as well
                PlaylistItem itemClicked = playlistItemList.get(position);

                String fileClicked;

                if (itemClicked.getSources().size() > 0) {
                    fileClicked = itemClicked.getSources().get(0).getFile();
                } else {
                    fileClicked = itemClicked.getFile();
                }

                Bundle bundle = new Bundle();
                bundle.putString("file", fileClicked);

                // Pass the file to the Fragment
                mCurrFragment.setArguments(bundle);

                // Setup the new Fragment
                setupJWPlayerFrag();
            }

            @Override
            public void onLongClick(View view, int position) {
                // TODO: remove the item from the playlist
                Toast.makeText(MainActivity.this, "Long press on position :" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.addOnItemTouchListener(myRecyclerItemTouchListener);
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
    protected void onDestroy() {
        super.onDestroy();

        // Remove listener
        mRecyclerView.removeOnItemTouchListener(myRecyclerItemTouchListener);
    }
}
