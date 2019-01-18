package com.jwplayer.opensourcedemo.listeners;

import android.graphics.Color;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;

import com.google.android.gms.cast.CastDevice;
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
        print("connected!");

    }

    @Override
    public void onConnectionSuspended(int i) {
        print("onConnectionSuspended()");

    }

    @Override
    public void onDisconnected() {
        print("onDisconnnected()");
    }

    @Override
    public void onConnectionFailed() {
        print("onConnectionFailed()");
    }

    @Override
    public void onConnectivityRecovered() {
        print("onConnectivityRecovered()");
    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo routeInfo) {
        print("detected!");
        visible();
    }

    private void print(String s) {
        Log.i("JWEVENTHANDLER", "(CHROMECAST): "+ s);
    }

    @Override
    public void onCastAvailabilityChanged(boolean b) {
        if(!b) gone();
        else visible();
    }

    @Override
    public void onDeviceSelected(CastDevice castDevice) {
//        print("onDeviceSelected (model#): "+castDevice.getModelName());
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
