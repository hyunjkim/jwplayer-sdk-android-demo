package com.jwplayer.opensourcedemo;

import android.graphics.Color;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import static com.google.android.gms.cast.framework.CastState.NO_DEVICES_AVAILABLE;
import static com.google.android.gms.cast.framework.CastState.CONNECTED;
import static com.google.android.gms.cast.framework.CastState.CONNECTING;
import static com.google.android.gms.cast.framework.CastState.NOT_CONNECTED;

public class MyCastListener implements
        CastStateListener,
        VideoPlayerEvents.OnControlBarVisibilityListener{

    private MediaRouteButton mCastButton;
    private boolean isCastAvailable = false;

    MyCastListener(MediaRouteButton mMediaRouteButton, JWPlayerView jwPlayerView) {
        mCastButton = mMediaRouteButton;
        jwPlayerView.addOnControlBarVisibilityListener(this);
    }

    MyCastListener(JWPlayerView jwPlayerView) {
        jwPlayerView.addOnControlBarVisibilityListener(this);
    }

    @Override
    public void onCastStateChanged(int castState) {

        String current = "";

        switch (castState) {
            case NO_DEVICES_AVAILABLE:
                current = "Current state - NO_DEVICES_AVAILABLE";
                break;
            case NOT_CONNECTED:
                current = "Current state - NOT_CONNECTED";
                break;
            case CONNECTING:
                current = "Current state - CONNECTING";
                break;
            case CONNECTED:
                current = "Current state - CONNECTED";
                break;
        }

        print("onCastStateChanged " + current);

        isCastAvailable = castState != CastState.NO_DEVICES_AVAILABLE;
    }

    private void showCastButton() {
        print("show cast button");
        mCastButton.setVisibility(View.VISIBLE);
        mCastButton.setBackgroundColor(Color.WHITE);
    }

    private void hideCastButton() {
        print("hide cast button");
        mCastButton.setVisibility(View.GONE);
    }

    private void print(String s) {
        Log.i("MyCast Listener", "(CAST) " + s);
    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBar) {
        if(mCastButton != null){
            if (isCastAvailable && controlBar.isVisible()) {
                showCastButton();
            } else {
                hideCastButton();
            }
        }
    }

}