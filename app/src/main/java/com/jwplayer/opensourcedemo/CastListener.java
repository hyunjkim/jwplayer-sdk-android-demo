package com.jwplayer.opensourcedemo;

import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.View;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastEvents;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

class CastListener implements
        CastEvents.ConnectionListener,
        VideoPlayerEvents.OnControlBarVisibilityListener {

    private CastManager mCastManager;
    private MediaRouteButton mCastButton;
    private JWPlayerView mPlayerView;
    private boolean castAvailable;

    CastListener(JWPlayerView mPlayerView, CastManager mCastManager, MediaRouteButton mediaRouteButton) {
        castAvailable = false;
        this.mPlayerView = mPlayerView;
        this.mCastManager = mCastManager;
        mCastButton = mediaRouteButton;

        mPlayerView.addOnControlBarVisibilityListener(this);
    }

    @Override
    public void onConnected() {
        Log.i("HYUNJOO", "Connected!!! ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onConnectivityRecovered() {

    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        if(controlBarVisibilityEvent.isVisible()){
            show();
            Log.i("CASTLISTENER", "onControlBarVisibilityChanged available");
        } else {
            Log.i("CASTLISTENER", "onControlBarVisibilityChanged not available");
            hide();
        }
    }

    public void show(){
        mCastButton.bringToFront();
        mCastButton.setVisibility(View.VISIBLE);
    }
    public void hide(){
        mCastButton.setVisibility(View.GONE);
    }

}
