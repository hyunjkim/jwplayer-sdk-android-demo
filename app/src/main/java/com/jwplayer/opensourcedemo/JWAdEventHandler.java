package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AdBreakEndEvent;
import com.longtailvideo.jwplayer.events.AdBreakStartEvent;
import com.longtailvideo.jwplayer.events.AdClickEvent;
import com.longtailvideo.jwplayer.events.AdCompanionsEvent;
import com.longtailvideo.jwplayer.events.AdCompleteEvent;
import com.longtailvideo.jwplayer.events.AdErrorEvent;
import com.longtailvideo.jwplayer.events.AdImpressionEvent;
import com.longtailvideo.jwplayer.events.AdPauseEvent;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdRequestEvent;
import com.longtailvideo.jwplayer.events.AdScheduleEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdStartedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.BeforeCompleteEvent;
import com.longtailvideo.jwplayer.events.BeforePlayEvent;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JWAdEventHandler implements
        VideoPlayerEvents.OnCompleteListener,

        AdvertisingEvents.OnAdBreakEndListener,
        AdvertisingEvents.OnAdBreakStartListener,
        AdvertisingEvents.OnAdClickListener,
        AdvertisingEvents.OnAdCompanionsListener,
        AdvertisingEvents.OnAdCompleteListener,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdImpressionListener,
        AdvertisingEvents.OnAdPauseListener,
        AdvertisingEvents.OnAdPlayListener,
        AdvertisingEvents.OnAdRequestListener,
        AdvertisingEvents.OnAdScheduleListener,
        AdvertisingEvents.OnAdSkippedListener,
        AdvertisingEvents.OnAdStartedListener,
        AdvertisingEvents.OnAdTimeListener,
        AdvertisingEvents.OnBeforeCompleteListener,
        AdvertisingEvents.OnBeforePlayListener {

    private JWPlayerViewExample mPlayerViewExample;
    private JWPlayerFragmentExample mPlayerFragmentExample;
    private JWPlayerView mPlayer;


    JWAdEventHandler(JWPlayerViewExample jwPlayerViewExample, JWPlayerView jwPlayerView) {
        mPlayerViewExample = jwPlayerViewExample;
        mPlayer = jwPlayerView;
        addListeners();
    }

    JWAdEventHandler(JWPlayerFragmentExample jwPlayerFragmentExample, JWPlayerView jwPlayerView) {
        mPlayerFragmentExample = jwPlayerFragmentExample;
        mPlayer = jwPlayerView;
        addListeners();
    }

    private void addListeners() {

        // Subscribe to allEventHandler: Player events
        mPlayer.addOnCompleteListener(this);
        mPlayer.addOnAdBreakEndListener(this);
        mPlayer.addOnAdStartedListener(this);
        mPlayer.addOnAdClickListener(this);
        mPlayer.addOnAdCompanionsListener(this);
        mPlayer.addOnAdCompleteListener(this);
        mPlayer.addOnAdErrorListener(this);
        mPlayer.addOnAdImpressionListener(this);
        mPlayer.addOnAdPauseListener(this);
        mPlayer.addOnAdPlayListener(this);
        mPlayer.addOnAdSkippedListener(this);
        mPlayer.addOnAdRequestListener(this);
        mPlayer.addOnAdScheduleListener(this);
        mPlayer.addOnAdStartedListener(this);
        mPlayer.addOnAdTimeListener(this);
        mPlayer.addOnBeforePlayListener(this);
        mPlayer.addOnBeforeCompleteListener(this);
    }

    private void print(String s){
        Log.i("JWEVENTHANDLER", " (AdEvent) "+ s);
    }

    private void logToPlayer(String output){
        if(mPlayerViewExample==null){
            mPlayerFragmentExample.logout(output);
        } else mPlayerViewExample.logout(output);
    }

    @Override
    public void onAdBreakEnd(AdBreakEndEvent adBreakEndEvent) {
        logToPlayer("onAdBreakEnd: ");
        print("onAdBreakEnd: ");

    }

    @Override
    public void onAdBreakStart(AdBreakStartEvent adBreakStartEvent) {
        logToPlayer("onAdBreakStart() ");
        print("onAdBreakStart: ");

    }

    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        logToPlayer("onAdClick tag: "+ adClickEvent.getTag());
        print("onAdClick tag: "+ adClickEvent.getTag());

    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        logToPlayer("onAdCompanions: ");
        print("onAdCompanions: ");

    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        logToPlayer("onAdComplete: " + adCompleteEvent.getTag());
        print("onAdComplete: " + adCompleteEvent.getTag());

    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        logToPlayer("onAdError: " + adErrorEvent.getMessage());
        print("onAdError: " + adErrorEvent.getMessage());
        print("onAdError: " + adErrorEvent.getTag());

    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        logToPlayer("onAdImpression: " + adImpressionEvent.getTag());
        print("onAdImpression: " +  adImpressionEvent.getTag());

    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        logToPlayer("onAdPause()");
        print("onAdPause: " + adPauseEvent.getTag());

    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        logToPlayer("onAdPlay()" + adPlayEvent.getTag());
        print("onAdPlay()" + adPlayEvent.getTag());
    }

    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        logToPlayer("onAdRequest() tag: "+adRequestEvent.getTag());
        print("onAdRequest() tag: "+adRequestEvent.getTag());
        print("onAdRequest() position: "+adRequestEvent.getAdPosition());
    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        logToPlayer("onAdSchedule: "+adScheduleEvent.getTag());
        print("onAdSchedule: "+adScheduleEvent.getTag());

    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        logToPlayer("onAdSkipped: " + adSkippedEvent.getTag());
        print("onAdSkipped tag: "+adSkippedEvent.getTag());

    }

    @Override
    public void onAdStarted(AdStartedEvent adStartedEvent) {
        logToPlayer("onAdStarted: " + adStartedEvent.getTag());
        print("onAdStarted: " + adStartedEvent.getTag());

    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
    }


    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        logToPlayer("onBeforePlay() ");
        print("onBeforePlay() ");
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        logToPlayer("onBeforeComplete: ");
        print("onBeforeComplete() ");
    }


    @Override
    public void onComplete(CompleteEvent completeEvent) {
        logToPlayer("onComplete()");
        print("onComplete()");
    }

}
