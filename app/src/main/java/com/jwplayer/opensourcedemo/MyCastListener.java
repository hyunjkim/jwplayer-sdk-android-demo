package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.longtailvideo.jwplayer.cast.CastEvents;

import androidx.mediarouter.app.MediaRouteButton;
import androidx.mediarouter.media.MediaRouter;

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
        print("Cast Connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mCustomBtn.getContext(), "Cast Connection Suspended!", Toast.LENGTH_SHORT).show();
        print("Cast Connection Suspended");
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Disconnected!", Toast.LENGTH_SHORT).show();
        mCustomBtn.setVisibility(View.GONE);
        print("Cast Disconnected");

    }

    @Override
    public void onConnectionFailed() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Failed!", Toast.LENGTH_SHORT).show();
        print("Cast Failed!");

    }

    @Override
    public void onConnectivityRecovered() {
        Toast.makeText(mCustomBtn.getContext(), "Cast Connectivity Recovered!", Toast.LENGTH_SHORT).show();
        print("Cast Connectivity Recovered!");

    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo routeInfo) {
        if (routeInfo.isEnabled()){
            print("Cast Device Detected: "+routeInfo.getConnectionState());
            Toast.makeText(mCustomBtn.getContext(), "Cast Device Detected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCastAvailabilityChanged(boolean b) {
        print("Cast Availability Changed: "+ b );
        if(b) Toast.makeText(mCustomBtn.getContext(), "Cast Availability Changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceSelected(CastDevice castDevice) {
        Toast.makeText(mCustomBtn.getContext(), "on Device Selected!", Toast.LENGTH_SHORT).show();
    }

    private void print(String s) {
        Log.i("JWEVENTHANDLER", s);
    }
}
