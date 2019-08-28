package com.jwplayer.opensourcedemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyThreadListener {

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Reference to the {@link FrameLayout}
     */
    private FrameLayout fragmentContainer;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private VideoDetailFragment mVideoDetailFragment;
    private List<PlaylistItem> mPlaylist;
    private JWPlaylistAsyncTask asyncTask;
    private ProgressBar mProgressBar;
    private MyRecyclerItemTouchListener myRecyclerItemTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mainactivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        fragmentContainer = findViewById(R.id.container);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.rv);

        // Get the Playerlist in the background
        asyncTask = new JWPlaylistAsyncTask(this);
        asyncTask.execute("l3EGQ9TD");

        // Initialize JWPlayerView Fragment
        mVideoDetailFragment = new VideoDetailFragment();

        // Initialize RecyclerView
        setupRecyclerView();

        // Get a reference to the CastContext
        CastContext.getSharedInstance(this);
    }

    @Override
    public void playlistUpdated(List<PlaylistItem> playlistItems) {
        mPlaylist = playlistItems;
        myRecyclerAdapter.setPlaylist(playlistItems);

        // Stop the current player
        if (mVideoDetailFragment != null) {
            mVideoDetailFragment.onStop();
        }

        // Setup JWPlayer Video Fragment
        setupJWPlayerFrag();
    }

    @Override
    public void showProgressBar(String value) {
        int percent = Integer.parseInt(value);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mProgressBar.setProgress(percent, true);
        } else {
            mProgressBar.setIndeterminate(true);
        }
    }

    /*
     * JWPlayerView using Support Fragments in One Activity Example
     * */
    public void setupJWPlayerFrag() {

        // Attach the Fragment to our layout
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager
                .beginTransaction()
                .replace(fragmentContainer.getId(), mVideoDetailFragment)
                .addToBackStack(null)
                .commit();

        // Make sure all the pending fragment transactions have been completed, otherwise
        mFragmentManager.executePendingTransactions();
    }

    /*
     * Setup RecyclerView in Grid view
     * */
    private void setupRecyclerView() {
        // Initialize Recycler Layout and Adapter
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(myRecyclerAdapter);
        myRecyclerItemTouchListener =
                new MyRecyclerItemTouchListener(this, mRecyclerView, new MyRecyclerItemTouchListener.MyClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        // Get the File from the static playlist
                        String fileClicked = mPlaylist.get(position).getSources().get(0).getFile();

                        // Make sure to stop the current JWPlayerView
                        if (mVideoDetailFragment != null) {

                            // Release resources related to JWPlayerView
                            mVideoDetailFragment.onStop();
                        }

                        // Re-Instantiate JWPlayerView
                        mVideoDetailFragment = new VideoDetailFragment();

                        // Pass the file I am using to play
                        mVideoDetailFragment.passFile(fileClicked);

                        // Begin to replace the older container with the new view
                        setupJWPlayerFrag();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                });
        mRecyclerView.addOnItemTouchListener(myRecyclerItemTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    protected void onStop() {
        Log.i("HYUNJOO", "onStop() - remove onItem Touch Listener");
        mRecyclerView.removeOnItemTouchListener(myRecyclerItemTouchListener);
        mRecyclerView = null;
        asyncTask = null;
        myRecyclerItemTouchListener = null;
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
