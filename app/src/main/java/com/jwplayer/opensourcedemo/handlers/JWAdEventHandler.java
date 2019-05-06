package com.jwplayer.opensourcedemo.handlers;

import android.os.Build;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.myUtil.LogUtil;
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
    private final StringBuilder outputStringBuilder = new StringBuilder();
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

    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    @Override
    public void onAdBreakEnd(AdBreakEndEvent adBreakEndEvent) {
        updateOutput(" " + "AdBreakEndEvent " + adBreakEndEvent.getAdPosition());
        LogUtil.logAd("" + "AdBreakEndEvent " + adBreakEndEvent.getAdPosition());
    }

    @Override
    public void onAdBreakStart(AdBreakStartEvent adBreakStartEvent) {
        updateOutput(" " + "AdBreakStartEvent " + adBreakStartEvent.getAdPosition());
        LogUtil.logAd("" + "AdBreakStartEvent " + adBreakStartEvent.getAdPosition());
    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        updateOutput(" " + "onAdSchedule " + adScheduleEvent.getTag());
        LogUtil.logAd("" + "onAdSchedule " + adScheduleEvent.getClient());
        LogUtil.logAd("" + "onAdSchedule " + adScheduleEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adScheduleEvent.getVmapAdBreaks().forEach(e->LogUtil.logAd(("onAdSchedule-vmap ad break:" +e.toJson().toString())));
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        updateOutput(" " + "adErrorEvent: " + adErrorEvent.getMessage());
        LogUtil.logAd("" + "adErrorEvent message: " + adErrorEvent.getMessage());
        LogUtil.logAd("" + "adErrorEvent tag: " + adErrorEvent.getTag());
    }

    @Override
    public void onAdStarted(AdStartedEvent adStartedEvent) {

        updateOutput(" " + "adStartedEvent " + adStartedEvent.getCreativeType());
        LogUtil.logAd("" + "adStartedEvent " + adStartedEvent.getTag());
    }

    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        updateOutput(" " + "onAdRequest " + adRequestEvent.getTag());
        LogUtil.logAd("" + "onAdRequest tag: " + adRequestEvent.getTag());
        LogUtil.logAd("" + "onAdRequest position: " + adRequestEvent.getAdPosition());
        LogUtil.logAd("" + "onAdRequest client: " + adRequestEvent.getClient());
        LogUtil.logAd("" + "onAdRequest offset: " + adRequestEvent.getOffset());
    }


    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        updateOutput(" " + "onAdClick(\"" + adClickEvent.getTag() + ")\r\n");
        LogUtil.logAd("" + "onAdClick");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        updateOutput(" " + "onAdComplete(\"" + adCompleteEvent.getTag() + ")\r\n");
        LogUtil.logAd("" + "onAdComplete");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        updateOutput(" " + "onAdSkipped(\"" + adSkippedEvent.getTag() + ")\r\n");
        LogUtil.logAd("" + "onAdSkipped");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        updateOutput(" " + "onAdImpression(\"" + adImpressionEvent.getTag() + "\r\n" +
                " Video Type: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + ")\r\n");
        LogUtil.logAd("" + "onAdImpression(\"" + adImpressionEvent.getTag() + "\r\n" +
                " Video Type: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + ")\r\n");
    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        updateOutput(" " + "onAdPause(\"" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + "\")\n");
        LogUtil.logAd("" + "onAdPause" + adPauseEvent.getTag() + " " + adPauseEvent.getOldState());
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput(" " + "onAdPlay(\"" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")\r\n");
        LogUtil.logAd("" + "onAdPlay" + adPlayEvent.getTag() + " " + adPlayEvent.getOldState());
    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        updateOutput(" " + "onAdCompanions  tag:" + adCompanionsEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adCompanionsEvent
                    .getCompanions()
                    .forEach(e->{
                        LogUtil.logAd("onAdCompanions-" + e.getResource()+ "\n");
                        printCreatives(e.getCreativeViews());
                    });
        }
    }

    private void printCreatives(List<String> creative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && creative.size()>0) {
            creative.forEach(each -> LogUtil.logAd("onAdCompanions-Creative: " + each));
        }
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        updateOutput(" " + "onBeforeComplete()");
        LogUtil.logAd(" " + "onBeforeComplete(): " +beforeCompleteEvent);
    }


    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        updateOutput(" " + "onBeforePlay()");
        LogUtil.logAd(" " + "onBeforePlay()");
    }
}