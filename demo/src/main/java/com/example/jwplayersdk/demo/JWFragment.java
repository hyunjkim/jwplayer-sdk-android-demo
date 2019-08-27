package com.example.jwplayersdk.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jwplayersdk.demo.samples.SamplePlaylist;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class JWFragment extends JWPlayerSupportFragment {

    private JWPlayerView mPlayer;
    private int position = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_jwplayer, container, false);
        mPlayer = view.findViewById(R.id.jwplayer);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Activity has completed Activity#onCreate()
        setupJWPlayer(position);
    }

    /*
     * This is JW PlayerView
     * */
    private void setupJWPlayer(int position) {

        String mediaUrl = "https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4";

        if (position > -1) {
            mediaUrl = SamplePlaylist.getPlaylistMediaFile(position);
        }
        PlayerConfig config = new PlayerConfig.Builder()
                .file(mediaUrl)
                .autostart(true)
                .build();

        mPlayer.setup(config);
    }


    public void updateJWPlayerView(int position) {
        Log.i("HYUNJOO", "updateJWPlayerView(): " + position);

        if (mPlayer != null) {
            mPlayer.onStop();
        }
        setupJWPlayer(position);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mainactivity, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getContext(), menu,
                R.id.media_route_menu_item);
    }
}
