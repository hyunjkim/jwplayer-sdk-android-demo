package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.cast.CastEvents;
import com.longtailvideo.jwplayer.cast.CastManager;

public class MyCastListener implements CastEvents.ConnectionListener {

    private CastManager mCastManager;

    MyCastListener(CastManager castManager){
        mCastManager = castManager;
    }

    @Override
    public void onConnected() {
        if(mCastManager.isConnected()) LogUtil.log("Cast Connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        LogUtil.log("Cast Connection Suspended " + i) ;
    }

    @Override
    public void onDisconnected() {
        LogUtil.log("Cast Disconnected!");
    }

    @Override
    public void onConnectionFailed() {
        LogUtil.log("Cast Connection Failed!");
    }

    @Override
    public void onConnectivityRecovered() {
        LogUtil.log("Cast Connectivity Recovered!");
    }
}
