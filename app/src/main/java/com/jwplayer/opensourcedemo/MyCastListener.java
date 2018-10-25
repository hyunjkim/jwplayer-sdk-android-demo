package com.jwplayer.opensourcedemo;

import android.widget.Toast;

import com.longtailvideo.jwplayer.cast.CastEvents;

import androidx.mediarouter.app.MediaRouteButton;

class MyCastListener implements CastEvents.ConnectionListener{

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
}
