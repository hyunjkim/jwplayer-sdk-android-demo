package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.BufferEvent;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.DisplayClickEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.IdleEvent;
import com.longtailvideo.jwplayer.events.MuteEvent;
import com.longtailvideo.jwplayer.events.PauseEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.PlaylistCompleteEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.SeekedEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JWSeekHandler implements
        VideoPlayerEvents.OnSetupErrorListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnBufferListener,
        VideoPlayerEvents.OnIdleListener,
        VideoPlayerEvents.OnErrorListener,
        VideoPlayerEvents.OnSeekListener,
        VideoPlayerEvents.OnPlaylistCompleteListener,
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnDisplayClickListener,
        VideoPlayerEvents.OnMuteListener,
        VideoPlayerEvents.OnSeekedListener,
        AdvertisingEvents.OnAdPlayListener{

    private TextView mOutput;
    private ScrollView mScroll;
    private final StringBuilder outputStringBuilder = new StringBuilder();


    JWSeekHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(outputStringBuilder.append("Build version: ").append(jwPlayerView.getVersionCode()).append("\r\n"));

    // Subscribe to allEventHandler: Player events
        jwPlayerView.addOnSetupErrorListener(this);
        jwPlayerView.addOnPlayListener(this);
        jwPlayerView.addOnPauseListener(this);
        jwPlayerView.addOnBufferListener(this);
        jwPlayerView.addOnIdleListener(this);
        jwPlayerView.addOnErrorListener(this);
        jwPlayerView.addOnSeekListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnDisplayClickListener(this);
        jwPlayerView.addOnMuteListener(this);
        jwPlayerView.addOnSeekedListener(this);
        jwPlayerView.addOnAdPlayListener(this);
    }

    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s){
        Log.i("JWSEEKHANDLER",s);
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput("onAdPlay: "+ adPlayEvent.getTag());
        print("onAdPlay: "+ adPlayEvent.getTag());
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        updateOutput("onBuffer: "+ bufferEvent.getOldState());
        print("onBuffer: "+ bufferEvent.getOldState());
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        updateOutput("onComplete ");
        print("onComplete ");

    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        updateOutput("onDisplayClick ");
        print("onDisplayClick ");

    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        updateOutput("onError: "+ errorEvent.getException());
        Log.i("JWSEEKHANDLER","onError: "+ errorEvent.getMessage(),errorEvent.getException());

    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        updateOutput("onIdle: "+ idleEvent.getOldState());
        print("onIdle: "+ idleEvent.getOldState());

    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        updateOutput("onMute: "+ muteEvent.getMute());
        print("onMute: "+ muteEvent.getMute());

    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        updateOutput("onPause: "+ pauseEvent.getOldState());
        print("onPause: "+ pauseEvent.getOldState());

    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        updateOutput("onPlay: "+ playEvent.getOldState());
        print("onPlay: "+ playEvent.getOldState());

    }

    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        updateOutput("onPlaylistComplete ");
        print("onPlaylistComplete ");

    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        updateOutput("onSeek: "+ seekEvent.getPosition());
        updateOutput("onSeek: "+ seekEvent.getOffset());
        print("onSeek position: "+ seekEvent.getPosition());
        print("onSeek offset: "+ seekEvent.getOffset());

    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        updateOutput("onSeeked ");
        print("onSeeked ");

    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        updateOutput("onSetupError: "+ setupErrorEvent.getMessage());
        print("onSetupError: "+ setupErrorEvent.getMessage());

    }

}
