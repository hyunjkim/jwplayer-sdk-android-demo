package com.jwplayer.opensourcedemo;

import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.longtailvideo.jwplayer.cast.CastEvents;

class MyCastListener implements
        CastEvents.ConnectionListener,
        CastEvents.DeviceListener{

    private MediaRouteButton mCustomBtn;

    MyCastListener(MediaRouteButton mCustomCastBtn) {
        mCustomBtn = mCustomCastBtn;
    }

    @Override
    public void onConnected() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mCustomBtn.getContext(), "Cast Connection Suspended!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Disconnected!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Failed!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectivityRecovered() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Connectivity Recovered!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo routeInfo) {
        Toast.makeText(mCustomBtn.getContext(), "Cast Device Detected!", Toast.LENGTH_SHORT).show();
        print("Cast Device Deteced? " + routeInfo.isEnabled());
        print("Cast Device State? " + routeInfo.getConnectionState());
        if(routeInfo.isEnabled()){
            mCustomBtn.setVisibility(View.VISIBLE);
        } else  mCustomBtn.setVisibility(View.GONE);
    }

    @Override
    public void onCastAvailabilityChanged(boolean b) {
        Toast.makeText(mCustomBtn.getContext(), "Cast Availability Changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceSelected(CastDevice castDevice) {
        Toast.makeText(mCustomBtn.getContext(), "Cast Device Selected!", Toast.LENGTH_SHORT).show();
        print("Cast Device Selected: (id) " + castDevice.getDeviceId());
    }
    private void print(String s){
        Log.i("JWEVENTHANDLER",s);
    }
}
