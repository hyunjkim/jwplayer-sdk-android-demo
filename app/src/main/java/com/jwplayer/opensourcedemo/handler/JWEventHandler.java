package com.jwplayer.opensourcedemo.handler;

import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.myutil.Logger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.DisplayClickEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.FirstFrameEvent;
import com.longtailvideo.jwplayer.events.ReadyEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements
        VideoPlayerEvents.OnSetupErrorListener,
        VideoPlayerEvents.OnErrorListener,
        VideoPlayerEvents.OnControlBarVisibilityListener,
        VideoPlayerEvents.OnFirstFrameListener,
        VideoPlayerEvents.OnDisplayClickListener ,
        VideoPlayerEvents.OnReadyListener {

    private JWPlayerView mPlayer;
    private TextView mOutput;
    private ScrollView mScroll;

    public JWEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(Logger.printBuildVersion(mPlayer.getVersionCode()));

        // Subscribe to allEventHandler: Player events
        mPlayer.addOnControlBarVisibilityListener(this);
        mPlayer.addOnFirstFrameListener(this);
        mPlayer.addOnReadyListener(this);
        mPlayer.addOnErrorListener(this);
        mPlayer.addOnDisplayClickListener(this);
        mPlayer.addOnSetupErrorListener(this);

    }

    private void updateOutput(String output) {
        mOutput.setText(Logger.updateOutput(output));
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s) {
        Log.i("JWEVENTHANDLER", s);
    }


    /**
     * Regular playback events below here
     */

    @Override
    public void onError(ErrorEvent errorEvent) {
        updateOutput(" onError: " + errorEvent.getMessage());
        Exception exception = errorEvent.getException();
        Log.i("JWPLAYER-LOG", "onError: " + errorEvent.getMessage(), exception);
    }


    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        updateOutput(" " + "onFirstFrame: " + firstFrameEvent.getLoadTime());
        print(" " + "onFirstFrame: " + firstFrameEvent.getLoadTime());
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        updateOutput(" " + "onSetupError " + setupErrorEvent.getMessage());
        print(" " + "onSetupError " + setupErrorEvent.getMessage());
    }


    @Override
    public void onReady(ReadyEvent readyEvent) {
        updateOutput(" " + "onReady " + readyEvent.getSetupTime());
        print(" " + "onReady " + readyEvent.getSetupTime());
    }


    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        boolean isVisible = controlBarVisibilityEvent.isVisible();
        Handler h = new Handler();
        Runnable hideControls = () -> {
            mPlayer.setControls(false);
            updateOutput(" hide controls: " + isVisible + "\r\n");
            print(" hide controls: " + isVisible + "\r\n");
        };
        h.postDelayed(hideControls, 1000);

        updateOutput(" onControlBarVisibilityChanged(): " + isVisible + "\r\n");

    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        mPlayer.setControls(true);

        updateOutput(" onDisplayClick()");
        print(" onDisplayClick()");
    }
}
