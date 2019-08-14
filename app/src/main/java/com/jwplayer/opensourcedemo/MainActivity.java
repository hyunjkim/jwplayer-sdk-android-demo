package com.jwplayer.opensourcedemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private  JWPlaylistAsyncTask asyncTask;
    private ProgressBar mProgressBar;

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


        // Get a reference to the CastContext
        CastContext.getSharedInstance(this);
    }

    @Override
    public void playlistUpdated(List<PlaylistItem> playlistItems) {
        mPlaylist = playlistItems;
        myRecyclerAdapter = new MyRecyclerAdapter();
        myRecyclerAdapter.setPlaylist(playlistItems);

        // Setup JWPlayer Fragment
        if(mVideoDetailFragment == null){
            mVideoDetailFragment = new VideoDetailFragment();
        }

        // Setup JWPlayer Video Fragment
        setupJWPlayerFrag(mVideoDetailFragment);

        // Setup RecyclerView
        setupRecyclerView();
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
    public void setupJWPlayerFrag(VideoDetailFragment fragment) {

        mVideoDetailFragment = fragment;

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
    private MyRecyclerItemTouchListener myRecyclerItemTouchListener;
    /*
     * Setup RecyclerView in Grid view
     * */
    private void setupRecyclerView() {
        Log.i("HYUNJOO", "SETUP RECYCLERVIEW");

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(myRecyclerAdapter);
        myRecyclerItemTouchListener =
                new MyRecyclerItemTouchListener(getApplicationContext(), mRecyclerView, new MyRecyclerItemTouchListener.MyClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        String fileClicked = mPlaylist.get(position).getSources().get(0).getFile();
                        Log.i("HYUNJOO", "RECYCLERVIEW TOUCH LISTENER:  " + fileClicked);
                        mVideoDetailFragment.onStop();
                        VideoDetailFragment frag = new VideoDetailFragment();
                        frag.passFile(fileClicked);
                        setupJWPlayerFrag(frag);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        // TODO: remove the item from the playlist
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
