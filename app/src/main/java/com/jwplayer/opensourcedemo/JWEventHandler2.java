package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler2 implements
        VideoPlayerEvents.OnErrorListenerV2,
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnPlaylistCompleteListener,
        VideoPlayerEvents.OnPlaylistListener,
        VideoPlayerEvents.OnPlayListener,
        AdvertisingEvents.OnAdTimeListenerV2,
        AdvertisingEvents.OnAdSkippedListenerV2,
        AdvertisingEvents.OnAdStartedListener,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdPlayListenerV2{

    private TextView mOutput;
    private ScrollView mScroll;
    private final StringBuilder outputStringBuilder = new StringBuilder();
    private JWPlayerView mPlayer;

    public JWEventHandler2(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(outputStringBuilder.append("Build version: ").append(jwPlayerView.getVersionCode()).append("\r\n"));

        // Subscribe to all JW Player events
        jwPlayerView.addOnAdStartedListener(this);
        jwPlayerView.addOnAdPlayListener(this);
        jwPlayerView.addOnAdTimeListener(this);
        jwPlayerView.addOnAdErrorListener(this);
        jwPlayerView.addOnAdSkippedListener(this);
        jwPlayerView.addOnErrorListener(this);
        jwPlayerView.addOnPlayListener(this);
        jwPlayerView.addOnPlaylistListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
    }

    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());

    }

    private void print(String s){
        Log.i("JWPLAYER-LOG", s);
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput(" onAdPlay()");
        print(" onAdPlay() duration" + mPlayer.getDuration());
    }


    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
//        print(" onAdTime " + adTimeEvent.getPosition());
    }

    @Override
    public void onAdStarted(String s, String s1) {
        updateOutput(" onAdStarted tag: " + s + "\nonAdStarted parameter: " + s1);
        print(" onAdStarted tag: " + s + "\nonAdStarted parameter: " + s1);
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {

        updateOutput( " onAdSkipped " + adSkippedEvent.getTag());
        print( " onAdSkipped " + adSkippedEvent.getTag());
    }

    @Override
    public void onAdError(String s, String s1) {
        updateOutput(" onAdError: " + s + "\nonAdError: " + s1);
        print(" onAdError: " + s + "\nonAdError: " + s1);
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        updateOutput(" onError: " + errorEvent.getMessage());
        print(" onError Message: " + errorEvent.getMessage() + "\nonError Exception: "+ errorEvent.getException());
    }

    @Override
    public void onPlay(PlayerState playerState) {
        updateOutput( " onPlaying mediaId: " + mPlayer.getPlaylistItem(mPlayer.getPlaylistIndex()).getMediaId());
        print( " onPlaying mediaId: " + mPlayer.getPlaylistItem(mPlayer.getPlaylistIndex()).getMediaId());
    }

    @Override
    public void onPlaylist(List<PlaylistItem> list) {
        updateOutput( " onPlaylist: " + list.size());
        print( " onPlaylist: " + list.size());
    }

    @Override
    public void onComplete() {
        updateOutput( " onComplete ");
        print( " onComplete ");
    }

    @Override
    public void onPlaylistComplete() {
        updateOutput( " onPlaylistComplete ");
        print( " onPlaylistComplete ");
    }
}