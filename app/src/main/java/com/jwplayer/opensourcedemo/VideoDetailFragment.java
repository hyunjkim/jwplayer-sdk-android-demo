package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class VideoDetailFragment extends Fragment {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    private String file = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_videodetailfragment, container, false);

        mPlayerView = view.findViewById(R.id.playerFragment);
        TextView outputTextView = view.findViewById(R.id.output);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getActivity().getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView);

        // Setup JWPlayerView
        setupJWPlayerFrag(file);

        return view;
    }

    /*
    * Setup JWPlayerView
    * */
    private void setupJWPlayerFrag(String url) {

        PlayerConfig config = new PlayerConfig.Builder()
                .file(url)
                .autostart(true)
                .build();

        mPlayerView.setup(config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jwplayerview, menu);
        CastButtonFactory.setUpMediaRouteButton(getContext(), menu, R.id.media_route_menu_item);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onStart()");
            mPlayerView.onStart();
        }
    }

    @Override
    public void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onResume()");
            mPlayerView.onResume();
        }
    }

    @Override
    public void onPause() {
        // Let JW Player know that the app is going to the background
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onPause()");
            mPlayerView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onStop()");
            mPlayerView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        // Let JW Player know that the app is being destroyed
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onDestroy()");
            mPlayerView.onDestroy();
        }
        super.onDestroy();
    }

    public void passFile(String fileClicked) {
        file = fileClicked;
    }
}
