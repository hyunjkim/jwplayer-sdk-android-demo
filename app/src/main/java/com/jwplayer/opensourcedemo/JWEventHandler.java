package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements
        VideoPlayerEvents.OnCompleteListener {
    TextView mOutput;
    JWPlayerView mPlayer;
    boolean isBunnyVideo = true;
    private String TAG = JWEventHandler.class.getName();
    private String file;

    JWEventHandler(JWPlayerView jwPlayerView, TextView output) {
        mPlayer = jwPlayerView;
        mOutput = output;
        // Subscribe to all JW Player events
        jwPlayerView.addOnCompleteListener(this);
    }

    private void updateOutput(String output) {
        mOutput.setText(output);
    }

    /**
     * Regular playback events below here
     */

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        Log.d(TAG, "onComplete");
        updateOutput("onComplete()");
        mPlayer.load(new PlaylistItem("https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4"));
    }
}
