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
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.jwplayer.opensourcedemo.async.JWPlaylistAsyncTask;
import com.jwplayer.opensourcedemo.async.MyThreadListener;
import com.jwplayer.opensourcedemo.recyclerview.MyRecyclerAdapter;
import com.jwplayer.opensourcedemo.recyclerview.MyRecyclerItemTouchListener;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyThreadListener {
    /**
     * Reference to the {@link CastSession}
     */
    private final SessionManagerListener<CastSession> mSessionManagerListener = new MySessionManagerListener();
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
    private CastSession mCastSession;
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

        // Initialize RecyclerView
        setupRecyclerView();

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
        mCastContext.addCastStateListener(i -> {
            String caststate = "";
            switch (i) {
                case 1:
                    caststate = "on devices available";
                    break;
                case 2:
                    caststate = "not connected";
                    break;
                case 3:
                    caststate = "connecting";
                    break;
                case 4:
                    caststate = "connected";
                    break;
            }
            Log.i("HYUNJOO", "onCastStateChanged() " + caststate);
        });
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

    @Override
    public void playlistUpdated(List<PlaylistItem> playlistItems) {

        // Destroy this so that is doesn't update the old activity
        asyncTask.cancel(true);
        asyncTask = null;

        mPlaylist = playlistItems;
        myRecyclerAdapter.setPlaylist(playlistItems);

        // Stop the current player
        if (mVideoDetailFragment != null) {
            mVideoDetailFragment.onStop();
        } else {
            // Initialize JWPlayerView Fragment
            mVideoDetailFragment = new VideoDetailFragment();
        }

        // Setup JWPlayer Video Fragment
        setupJWPlayerFrag();
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

        //retain their instance for orientation reasons
        mVideoDetailFragment.setRetainInstance(true);

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
                        Bundle bundle = new Bundle();
                        bundle.putString("mediaurl", fileClicked);
                        mVideoDetailFragment.setArguments(bundle);

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
    protected void onResume() {
        Log.i("HYUNJOO", "MainActivity -  onResume()");
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).getSessionManager()
                    .getCurrentCastSession();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("HYUNJOO", "MainActivity - onPause() ");
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("HYUNJOO", "onStop()");
        super.onStop();
        asyncTask = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnItemTouchListener(myRecyclerItemTouchListener);
        mRecyclerView = null;
        myRecyclerItemTouchListener = null;
    }

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            supportInvalidateOptionsMenu();
            Log.i("HYUNJOO", "onSessionEnded");
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            supportInvalidateOptionsMenu();
            Log.i("HYUNJOO", "onSessionResumed");
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            supportInvalidateOptionsMenu();
            Log.i("HYUNJOO", "onSessionStarted");
        }

        @Override
        public void onSessionStarting(CastSession session) {
            Log.i("HYUNJOO", "onSessionStarting");
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
            Log.i("HYUNJOO", "onSessionStartFailed");
        }

        @Override
        public void onSessionEnding(CastSession session) {
            Log.i("HYUNJOO", "onSessionEnding");
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
            Log.i("HYUNJOO", "onSessionResuming");
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
            Log.i("HYUNJOO", "onSessionResumeFailed");
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
            Log.i("HYUNJOO", "onSessionSuspended");
        }
    }
}
