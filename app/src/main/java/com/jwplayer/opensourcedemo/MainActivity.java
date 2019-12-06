package com.jwplayer.opensourcedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.jwasynctask.JWManualAsyncTask;
import com.jwplayer.opensourcedemo.jwasynctask.JWTrendingAsyncTask;
import com.jwplayer.opensourcedemo.listeners.CallBackListener;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

public class MainActivity extends AppCompatActivity implements
        MyRecyclerAdapter.MyFragmentCallbackListener,
        CallBackListener {

    private JWManualAsyncTask manual;
    private JWTrendingAsyncTask trend;
    private JWPlayerView mPlayer;
    private RecyclerView manualrv;
    private RecyclerView trendingrv;
    private MyRecyclerAdapter manualAdapter, trendingAdapter;
    private ProgressBar progressbar1, progressbar2;
    private EditText firstrow_playlistid;
    private EditText secondrow_playlistid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer = findViewById(R.id.jwplayer);
        progressbar1 = findViewById(R.id.progressbar1);
        progressbar2 = findViewById(R.id.progressbar2);
        firstrow_playlistid = findViewById(R.id.firstrow_playlistid);
        secondrow_playlistid = findViewById(R.id.secondrow_playlistid);
        manualrv = findViewById(R.id.manual_recycler_view);
        trendingrv = findViewById(R.id.trending_recycler_view);

        manual = new JWManualAsyncTask(this);
        manual.execute("");
        trend = new JWTrendingAsyncTask(this);
        trend.execute("");

        LinearLayoutManager manualLayoutManager = new LinearLayoutManager(this);
        manualLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manualrv.setLayoutManager(manualLayoutManager);

        LinearLayoutManager trendingLayoutManager = new LinearLayoutManager(this);
        trendingLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trendingrv.setLayoutManager(trendingLayoutManager);

        manualAdapter = new MyRecyclerAdapter(this, "Manual", null);
        trendingAdapter = new MyRecyclerAdapter(this, "Trending", null);

        // Get a reference to the CastContext
        CastContext.getSharedInstance(this);
    }

    @Override
    public void reload(String playlistname, int position) {
        PlaylistItem item = SamplePlaylist.getPlaylistItem(playlistname).get(position);
        mPlayer.stop();
        mPlayer.load(item);
    }

    /*
     * Shows a ProgressBar while waiting for the playlist to finish loading
     * */
    @Override
    public void onShowProgressBar(String name) {
        if (name.equals("Manual")) {
            manualrv.setVisibility(View.GONE);
            progressbar1.setVisibility(View.VISIBLE);
        } else {
            trendingrv.setVisibility(View.GONE);
            progressbar2.setVisibility(View.VISIBLE);
        }
    }

    /*
     * Hides a ProgressBar
     * Playlist to should show
     * */
    @Override
    public void onHideProgressBar(String name) {

        if (SamplePlaylist.makeToast()) {
            Toast.makeText(this, " Manual/Trend Playlist Only", Toast.LENGTH_SHORT).show();
            SamplePlaylist.resetToast();
        }

        if (name.equals("Manual")) {
            manualAdapter.updateData(name);
            manualrv.setAdapter(manualAdapter);
            manualrv.setVisibility(View.VISIBLE);
            progressbar1.setVisibility(View.GONE);
        } else {
            trend.cancel(true);
            trendingAdapter.updateData(name);
            trendingrv.setAdapter(trendingAdapter);
            trendingrv.setVisibility(View.VISIBLE);
            progressbar2.setVisibility(View.GONE);
        }
    }

    /*
     * Loading a new Player ID
     * */
    public void getNewPlaylist(View enterbtn) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(enterbtn.getWindowToken(), 0);
        }

        if (enterbtn.getId() == R.id.firstrow_btn) {
            manual.cancel(true);
            manual = new JWManualAsyncTask(this);
            manual.execute(firstrow_playlistid.getText().toString());
            firstrow_playlistid.setText("");
        } else {
            trend.cancel(true);
            trend = new JWTrendingAsyncTask(this);
            trend.execute(secondrow_playlistid.getText().toString());
            secondrow_playlistid.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayer.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlayer.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.onDestroy();
    }
}
