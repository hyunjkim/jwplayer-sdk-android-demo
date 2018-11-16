package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;

class JWRelatedHandler implements com.longtailvideo.jwplayer.events.listeners.a.b{


    JWRelatedHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview, String myMethod, String url) {
        // Subscribe to allEventHandler: Player events
    }

    private void print(String s){
        Log.i("JWEVENTHANDLER" ,s);
    }

}
