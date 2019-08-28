package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class MainActivity extends AppCompatActivity implements
        MyRecyclerAdapter.MyFragmentCallbackListener {

    private View fragmentContainer;
    private JWPlayerView mPlayer;
    private JWPlayerSupportFragment jwsupportfragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.container);

        // Begin showing JWPlayerView
        beginJWSupportFragmentTransaction();

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        // Get a reference to the CastContext
        CastContext.getSharedInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);
        return true;
    }

    /**
     * {@link - https://developer.android.com/training/basics/fragments/communicating}
     */
    @Override
    public void replaceFragment(int position) {

        String mediaUrl = "https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";

        if (position > -1) {
            mediaUrl = SamplePlaylist.getPlaylistMediaFile(position);
        }
        PlayerConfig config = new PlayerConfig.Builder()
                .file(mediaUrl)
                .autostart(true)
                .build();

        // If JWPlayerSupportFragment is not null, stop
        if(jwsupportfragment != null) {
            jwsupportfragment.onStop();
        }

        // Replace the JWSupportFragment with a new JWPlayerSupportFragment + Config
        jwsupportfragment = JWPlayerSupportFragment.newInstance(config);

        // Begin Fragment Transaction
        beginJWSupportFragmentTransaction();
    }

    /**
     * Begin Fragment Transaction with JWPlayerSupportFragment
     */
    public void beginJWSupportFragmentTransaction() {
        if (jwsupportfragment == null) {
            jwsupportfragment = JWPlayerSupportFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), jwsupportfragment)
                .addToBackStack(null)
                .commit();

        mPlayer = jwsupportfragment.getPlayer();

    }

    @Override
    public void onStart() {
        Log.i("HYUNJOO", "onStart()");
        super.onStart();
        if (mPlayer != null) {
            mPlayer.onStart();
        }
    }


    @Override
    public void onResume() {
        Log.i("HYUNJOO", "onResume()");
        // Let JW Player know that the app has returned from the background
        super.onResume();
        if (mPlayer != null) {
            mPlayer.onResume();
        }
    }

    @Override
    public void onPause() {
        Log.i("HYUNJOO", "onPause()");
        // Let JW Player know that the app is going to the background
        if (mPlayer != null) {
            mPlayer.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("HYUNJOO", "onStop()");
        super.onStop();
        if (mPlayer != null) {
            mPlayer.onStop();
        }
    }

    @Override
    public void onDestroy() {
        Log.i("HYUNJOO", "onDestroy()");
        // Let JW Player know that the app is being destroyed
        if (mPlayer != null) {
            mPlayer.onDestroy();
        }

        super.onDestroy();
    }
}
