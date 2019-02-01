package com.jwplayer.opensourcedemo.handlers;

import android.graphics.Color;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.CastDevice;
import com.jwplayer.opensourcedemo.myUtil.LogUtil;
import com.longtailvideo.jwplayer.cast.CastEvents;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class JWCastHandler implements
        CastEvents.DeviceListener,
        CastEvents.ConnectionListener,
        VideoPlayerEvents.OnControlBarVisibilityListener {

    private MediaRouteButton mChromecastbtn;

    public JWCastHandler(MediaRouteButton mChromecastbtn){
        this.mChromecastbtn = mChromecastbtn;
    }

    @Override
    public void onConnected() {
        LogUtil.logcast("connected!");

    }

    @Override
    public void onConnectionSuspended(int i) {
        LogUtil.logcast("onConnectionSuspended()");

    }

    @Override
    public void onDisconnected() {
        LogUtil.logcast("onDisconnnected()");
    }

    @Override
    public void onConnectionFailed() {
        LogUtil.logcast("onConnectionFailed()");
    }

    @Override
    public void onConnectivityRecovered() {
        LogUtil.logcast("onConnectivityRecovered()");
    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo routeInfo) {
        LogUtil.logcast("detected!");
        visible();
    }

    @Override
    public void onCastAvailabilityChanged(boolean b) {
        if(!b) gone();
        else visible();
    }

    @Override
    public void onDeviceSelected(CastDevice castDevice) {
        LogUtil.logcast("onDeviceSelected (model#): " + castDevice.getModelName());
    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        if(controlBarVisibilityEvent.isVisible()) visible();
        else gone();
    }

    private void visible(){
        mChromecastbtn.setVisibility(View.VISIBLE);
        mChromecastbtn.setBackgroundColor(Color.CYAN);
        mChromecastbtn.bringToFront();
    }

    private void gone(){
        mChromecastbtn.setVisibility(View.INVISIBLE);
    }

}
