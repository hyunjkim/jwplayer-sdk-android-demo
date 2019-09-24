package com.jwplayer.opensourcedemo;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class VideoDetailFragment extends Fragment implements VideoPlayerEvents.OnFullscreenListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Default file to play
     */
    private String file;
    private LinearLayout mLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            file = getArguments().getString("file");
        } else {
            file = "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_videodetailfragment, container, false);
        mLinearLayout = view.findViewById(R.id.layout_videodetailfragment);

        mPlayerView = view.findViewById(R.id.playerFragment);
        TextView outputTextView = view.findViewById(R.id.output);

        // Add Fullscreen Listener
        mPlayerView.addOnFullscreenListener(this);

        // Setup JW Player Frag
        setupJWPlayerFrag(file);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getActivity().getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView);

        return view;
    }

    private void setupJWPlayerFrag(String file) {
        PlayerConfig config = new PlayerConfig.Builder()
                .file(file)
                .autostart(true)
                .build();

        mPlayerView.setup(config);
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

    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreenEvent true if the player is fullscreen
     */
    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mLinearLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }
}
