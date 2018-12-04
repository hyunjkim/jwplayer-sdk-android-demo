package com.jwplayer.opensourcedemo;

import android.os.Build;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.RelatedCloseEvent;
import com.longtailvideo.jwplayer.events.RelatedOpenEvent;
import com.longtailvideo.jwplayer.events.RelatedPlayEvent;
import com.longtailvideo.jwplayer.events.listeners.RelatedPluginEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class JWRelatedHandler implements
        RelatedPluginEvents.OnRelatedCloseListener,
        RelatedPluginEvents.OnRelatedOpenListener,
        RelatedPluginEvents.OnRelatedPlayListener{

    private TextView mOutput;
    private ScrollView mScroll;
    private final StringBuilder outputStringBuilder = new StringBuilder();

    JWRelatedHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {

        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(outputStringBuilder.append("Build version: ").append(jwPlayerView.getVersionCode()).append("\r\n"));

        // Subscribe to allEventHandler: Player events
        jwPlayerView.addOnRelatedOpenListener(this);
        jwPlayerView.addOnRelatedCloseListener(this);
        jwPlayerView.addOnRelatedPlayListener(this);
    }


    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s){
        Log.i("JWEVENTHANDLER" ,s);
    }

    @Override
    public void onRelatedClose(RelatedCloseEvent relatedCloseEvent) {
        updateOutput("onRelatedClose() "+relatedCloseEvent.getMethod());
        print("onRelatedClose: " + relatedCloseEvent.getMethod());
    }

    @Override
    public void onRelatedOpen(RelatedOpenEvent relatedOpenEvent) {
        updateOutput("onRelatedOpen() "+relatedOpenEvent.getUrl());
        print("onRelatedOpen method: "+ relatedOpenEvent.getMethod());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            relatedOpenEvent.getItems().forEach(e->print("onRelatedOpen item: "+e.getFile()));
        }
        print("onRelatedOpen url: "+ relatedOpenEvent.getUrl());

    }

    @Override
    public void onRelatedPlay(RelatedPlayEvent relatedPlayEvent) {
        updateOutput("onRelatedPlay() " + relatedPlayEvent.getPosition());
        print("onRelatedPlay position: " + relatedPlayEvent.getPosition());
        print("onRelatedPlay file: " + relatedPlayEvent.getItem().getFile());
        print("onRelatedPlay auto? " + relatedPlayEvent.getAuto());

    }
}
