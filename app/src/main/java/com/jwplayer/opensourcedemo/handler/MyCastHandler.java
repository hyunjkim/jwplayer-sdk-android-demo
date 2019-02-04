package com.jwplayer.opensourcedemo.handler;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.CastDevice;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastEvents;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import androidx.mediarouter.app.MediaRouteButton;
import androidx.mediarouter.media.MediaRouter;

public class MyCastHandler implements
        CastEvents.ConnectionListener,
        CastEvents.DeviceListener,
        VideoPlayerEvents.OnControlBarVisibilityListener {

    private MediaRouteButton mCastButton;
    private JWPlayerView mPlayer;
    private int defaultColor;

    public MyCastHandler(MediaRouteButton castButton, JWPlayerView mPlayerView){
        mCastButton = castButton;
        defaultColor = mCastButton.getSolidColor(); // if I want to use chromecast default color
        mPlayer = mPlayerView;
    }

    @Override
    public void onConnected() {

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
        visible();
    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo routeInfo) {
        visible();
    }

    @Override
    public void onCastAvailabilityChanged(boolean b) {
        print(" onCastAvailabilityChanged() "+ b);
        if(!b) {
            invisbile();
        } else {
            visible();
        }
    }

    private void invisbile() {
        mCastButton.setVisibility(View.INVISIBLE);
    }


    private void visible() {
        print("visible - show blue");
        mCastButton.setBackgroundColor(Color.BLUE);
        mCastButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeviceSelected(CastDevice castDevice) {

    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        boolean visible = controlBarVisibilityEvent.isVisible();
        print("onControlBarVisibilityChanged(): " + visible);
        if(!visible) invisbile();
        else visible();


    }

    private void print(String s){
        Log.i("JWEVENTHANDLER", "(CAST) - "+ s);
    }

}
