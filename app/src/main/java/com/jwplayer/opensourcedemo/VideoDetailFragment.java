package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class VideoDetailFragment extends JWPlayerSupportFragment {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Default file to play
     */
    private String file = "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videodetailfragment, container, false);

        mPlayerView = view.findViewById(R.id.playerFragment);
        TextView outputTextView = view.findViewById(R.id.output);

        setupJWPlayerFrag(file);

        if (getActivity().getWindow() != null) {
            // Keep the screen on during playback
            new KeepScreenOnHandler(mPlayerView, getActivity().getWindow());
        }

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView);

        return view;
    }


    private void setupJWPlayerFrag(String file) {
        Log.i("HYUNJOO", " setupJWPlayerFrag: " + file);

        PlayerConfig config = new PlayerConfig.Builder()
                .file(file)
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
    public void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    public void passFile(String fileClicked) {
        file = fileClicked;
    }
}
