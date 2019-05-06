package com.jwplayer.opensourcedemo.handlers;

import android.graphics.Color;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class CustomCastStateListener implements
        CastStateListener,
        VideoPlayerEvents.OnControlBarVisibilityListener {

    private MediaRouteButton mChromeCastButton;

    public CustomCastStateListener(MediaRouteButton castbutton) {
        mChromeCastButton = castbutton;
    }

    @Override
    public void onCastStateChanged(int i) {
        String state = "";
        switch (i) {
            case CastState.CONNECTED: // 4
                state = "CONNECTED";
                break;
            case CastState.CONNECTING: // 3
                state = "CONNECTING";
                break;
            case CastState.NOT_CONNECTED: // 2
                state = "NOT_CONNECTED";
                break;
            case CastState.NO_DEVICES_AVAILABLE: // 1
                state = "NO_DEVICES_AVAILABLE";
                break;
        }
        Log.i("HYUNJOO", "onCastStateChanged: " + state);
    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        if (controlBarVisibilityEvent.isVisible()) visible();
        else gone();
    }

    private void visible() {
        mChromeCastButton.setVisibility(View.VISIBLE);
        mChromeCastButton.setBackgroundColor(Color.CYAN);
        mChromeCastButton.bringToFront();
    }

    private void gone() {
        mChromeCastButton.setVisibility(View.INVISIBLE);
    }
}
