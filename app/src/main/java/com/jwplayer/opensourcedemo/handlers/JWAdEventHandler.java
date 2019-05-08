package com.jwplayer.opensourcedemo.handlers;

import android.os.Build;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.myutility.Logger;
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
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JWAdEventHandler implements
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

    private TextView mOutput;
    private ScrollView mScroll;
    private JWPlayerView mPlayer;


    public JWAdEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;

        // Subscribe to allEventHandler: Player events
        jwPlayerView.addOnAdBreakEndListener(this);
        jwPlayerView.addOnAdStartedListener(this);
        jwPlayerView.addOnAdClickListener(this);
        jwPlayerView.addOnAdCompanionsListener(this);
        jwPlayerView.addOnAdCompleteListener(this);
        jwPlayerView.addOnAdErrorListener(this);
        jwPlayerView.addOnAdImpressionListener(this);
        jwPlayerView.addOnAdPauseListener(this);
        jwPlayerView.addOnAdPlayListener(this);
        jwPlayerView.addOnAdSkippedListener(this);
        jwPlayerView.addOnAdRequestListener(this);
        jwPlayerView.addOnAdScheduleListener(this);
        jwPlayerView.addOnAdStartedListener(this);
        jwPlayerView.addOnAdTimeListener(this);
        jwPlayerView.addOnBeforeCompleteListener(this);
        jwPlayerView.addOnBeforePlayListener(this);
    }

    /*
    * Generate Log Line
    */
    private void generateLogLine(String output) {
        mOutput.setText(Logger.log(output));
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    @Override
    public void onAdBreakEnd(AdBreakEndEvent adBreakEndEvent) {
        generateLogLine("AdBreakEndEvent " + adBreakEndEvent.getAdPosition());
        Logger.logAd("AdBreakEndEvent Ad Position: " + adBreakEndEvent.getAdPosition());
    }

    @Override
    public void onAdBreakStart(AdBreakStartEvent adBreakStartEvent) {
        generateLogLine("AdBreakStartEvent " + adBreakStartEvent.getAdPosition());
        Logger.logAd("AdBreakStartEvent Ad Position: " + adBreakStartEvent.getAdPosition());
    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        generateLogLine("onAdSchedule " + adScheduleEvent.getTag());
        Logger.logAd("onAdSchedule: "+
                "\r\n\tclient: "+ adScheduleEvent.getClient() +
                "\r\n\ttag:" + adScheduleEvent.getTag());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adScheduleEvent.getVmapAdBreaks().forEach(e-> Logger.logAd(("onAdSchedule - VMAP Ad break: " + e.toJson().toString())));
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        generateLogLine("adErrorEvent: " + adErrorEvent.getMessage());
        Logger.logAd("adErrorEvent message: " + adErrorEvent.getMessage());
        Logger.logAd("adErrorEvent tag: " + adErrorEvent.getTag());
    }

    @Override
    public void onAdStarted(AdStartedEvent adStartedEvent) {

        generateLogLine("adStartedEvent: " + adStartedEvent.getCreativeType());
        Logger.logAd("adStartedEvent: " + adStartedEvent.getTag());
    }

    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        generateLogLine("onAdRequest: " + adRequestEvent.getTag());
        Logger.logAd("onAdRequest tag: " + adRequestEvent.getTag());
        Logger.logAd("onAdRequest position: " + adRequestEvent.getAdPosition());
        Logger.logAd("onAdRequest client: " + adRequestEvent.getClient());
        Logger.logAd("onAdRequest offset: " + adRequestEvent.getOffset());
    }


    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        generateLogLine("onAdClick(\"" + adClickEvent.getTag() + ")");
        Logger.logAd("nAdClick" + adClickEvent.getTag());
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        generateLogLine("onAdComplete(" + adCompleteEvent.getTag() + ")");
        Logger.logAd("onAdComplete tag: " + adCompleteEvent.getTag());
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        generateLogLine("onAdSkipped(" + adSkippedEvent.getTag() + ")");
        Logger.logAd("onAdSkipped tag: " + adSkippedEvent.getTag());
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        generateLogLine("onAdImpression(" +
                "\r\ntag: " + adImpressionEvent.getTag() +
                "\r\nAd Position: " + adImpressionEvent.getAdPosition().name() + ")");
        Logger.logAd("onAdImpression(" +
                "\r\ntag: " + adImpressionEvent.getTag() +
                "\r\nClient: " + adImpressionEvent.getClient() +
                "\r\nAd Position: " + adImpressionEvent.getAdPosition().name() +
                "\r\nCreative Type: " + adImpressionEvent.getCreativeType());
    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        generateLogLine("onAdPause(" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + ")");
        Logger.logAd("onAdPause: " + adPauseEvent.getTag() + " " + adPauseEvent.getOldState());
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        generateLogLine("onAdPlay(" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")");
        Logger.logAd("onAdPlay: " + adPlayEvent.getTag() + " " + adPlayEvent.getOldState());
    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        generateLogLine("onAdCompanions  tag:" + adCompanionsEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adCompanionsEvent
                    .getCompanions()
                    .forEach(e->{
                        Logger.logAd("onAdCompanions- " + e.getResource());
                        printCreatives(e.getCreativeViews());
                    });
        }
    }

    private void printCreatives(List<String> creative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && creative.size()>0) {
            creative.forEach(each -> Logger.logAd("onAdCompanions-Creative: " + each));
        }
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        generateLogLine("onBeforeComplete()");
        Logger.logAd("onBeforeComplete(): " +beforeCompleteEvent);
    }


    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        generateLogLine("onBeforePlay()");
        Logger.logAd("onBeforePlay()");
    }
}
