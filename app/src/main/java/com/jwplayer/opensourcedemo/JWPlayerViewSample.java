package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.BufferEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.ReadyEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

/*
 * JWPlayerViewSample Fragment
 * */
public class JWPlayerViewSample extends Fragment implements View.OnClickListener {

    final String TAB_JWPLAYER = "JWPlayerViewSample";

    private JWPlayerView mPlayerView;
    private boolean userClickedPause;
    private PlaylistItem pi;
    private PlayerConfig config;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_jwplayerview, container, false);

        mPlayerView = v.findViewById(R.id.jwplayer);
        v.findViewById(R.id.pausebtn).setOnClickListener(this);
        v.findViewById(R.id.loadbtn).setOnClickListener(this);
        userClickedPause = false;

        // Load a media source
        pi = new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .title("BipBop")
                .description("A video player testing video.")
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            playlist.add(pi);
        }

        config = new PlayerConfig.Builder()
                .playlist(playlist)
                .autostart(false)
                .displayDescription(true)
                .build();

        // NOTE: Fired when the player enters the BUFFERING state.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnBufferListener.html
        mPlayerView.addOnBufferListener(new VideoPlayerEvents.OnBufferListener() {
            @Override
            public void onBuffer(BufferEvent bufferEvent) {
                if (userClickedPause) {
                    if (mPlayerView != null) {
                        Log.i(TAB_JWPLAYER, "onbufferlistener - PAUSE! ");
                        mPlayerView.pause();
                    }
                }
            }
        });

        // NOTE: Signifies when the player has been initialized and is ready for playback.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnReadyListener.html
        mPlayerView.addOnReadyListener(new VideoPlayerEvents.OnReadyListener() {
            @Override
            public void onReady(ReadyEvent readyEvent) {
                Log.i(TAB_JWPLAYER, "onReady() ");
                userClickedPause = false;
            }
        });

        // NOTE: Fired when the player enters the PLAYING state.
        // https://s3.amazonaws.com/developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnPlayListener.html
        mPlayerView.addOnPlayListener(new VideoPlayerEvents.OnPlayListener() {
            @Override
            public void onPlay(PlayEvent playEvent) {
                Log.i(TAB_JWPLAYER, "onPlay() ");
                userClickedPause = false;
            }
        });

        // NOTE: These are just to see the entire log, Click Check All from App > in Android Studio Logcat filter for "callbackscreen"
        CallbackScreen callback = v.findViewById(R.id.callback_screen);
        callback.registerListeners(mPlayerView);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (config != null) {
            mPlayerView.setup(config);
        }
    }

    /*
     * Pause and Load Example
     * */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.pausebtn) {
            if (mPlayerView != null) {
                userClickedPause = true;
                Log.i(TAB_JWPLAYER, "Clicked Pause + current Player state is " + mPlayerView.getState());
                mPlayerView.pause();
            }
        } else {
            if (mPlayerView != null) {
                userClickedPause = false;
                Log.i(TAB_JWPLAYER, "Clicked Load + current Player state is " + mPlayerView.getState());
                mPlayerView.stop();
                mPlayerView.load(pi);
            }
        }
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

}