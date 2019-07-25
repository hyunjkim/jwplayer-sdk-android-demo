package com.jwplayer.opensourcedemo.handlers;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.utilities.JWLogger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AdClickEvent;
import com.longtailvideo.jwplayer.events.AdCompleteEvent;
import com.longtailvideo.jwplayer.events.AdImpressionEvent;
import com.longtailvideo.jwplayer.events.AdPauseEvent;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.VisualQuality;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */

public class JWAdEventHandler implements
        VideoPlayerEvents.OnVisualQualityListener,
        AdvertisingEvents.OnAdClickListenerV2,
        AdvertisingEvents.OnAdCompleteListenerV2,
        AdvertisingEvents.OnAdSkippedListenerV2,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdImpressionListenerV2,
        AdvertisingEvents.OnAdTimeListenerV2,
        AdvertisingEvents.OnAdPauseListenerV2,
        AdvertisingEvents.OnAdPlayListenerV2,
        AdvertisingEvents.OnBeforePlayListener,
        AdvertisingEvents.OnBeforeCompleteListener {

    private TextView mOutput;
    private ScrollView mScroll;


    public JWAdEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mScroll = scrollview;
        mOutput = output;

        // Subscribe to all JW Player events
        jwPlayerView.addOnVisualQualityListener(this);
        jwPlayerView.addOnAdClickListener(this);
        jwPlayerView.addOnAdCompleteListener(this);
        jwPlayerView.addOnAdSkippedListener(this);
        jwPlayerView.addOnAdErrorListener(this);
        jwPlayerView.addOnAdImpressionListener(this);
        jwPlayerView.addOnAdTimeListener(this);
        jwPlayerView.addOnAdPauseListener(this);
        jwPlayerView.addOnAdPlayListener(this);
        jwPlayerView.addOnBeforePlayListener(this);
        jwPlayerView.addOnBeforeCompleteListener(this);
    }

    private void updateOutput(String output) {
        mOutput.setText(JWLogger.generateLogLine(output));
        mScroll.scrollTo(0, mScroll.getBottom());
    }

    private void print(String s) {
        Log.i("JWEVENTHANDLER", "(ADEVENT) "+ s);
    }

    /**
     * Regular playback events below here
     */

    @Override
    public void onBeforeComplete() {
        print("onBeforeComplete()");
        updateOutput("onBeforeComplete()");
    }

    @Override
    public void onBeforePlay() {
        print("onBeforePlay()");
        updateOutput("onBeforePlay()");
    }


    @Override
    public void onAdError(String tag, String message) {
        print("onAdError: (" + tag + "\", \"\n" + message + ")\r\n");
        updateOutput("onAdError: (" + tag + "\", \"\n" + message + ")\r\n");
    }

    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        print("onAdClick: (" + adClickEvent.getTag() + ")\r\n");
        updateOutput("onAdClick: (" + adClickEvent.getTag() + ")\r\n");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        print("onAdComplete: (" + adCompleteEvent.getTag() + ")\r\n");
        updateOutput("onAdComplete: (" + adCompleteEvent.getTag() + ")\r\n");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        print("onAdSkipped: (" + adSkippedEvent.getTag() + ")\r\n");
        updateOutput("onAdSkipped: (" + adSkippedEvent.getTag() + ")\r\n");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        print("onAdImpression("+
                "\r\ntag: " + adImpressionEvent.getTag() +
                "\r\nMediaFile: " + adImpressionEvent.getMediaFile() +
                "\r\nCreativeType: " + adImpressionEvent.getCreativeType() +
                "\r\nClient: " + adImpressionEvent.getClient() +
                "\r\nVastVersion: " + adImpressionEvent.getVastVersion() +
                "\r\nAdPosition: " + adImpressionEvent.getAdPosition().name());
        updateOutput("onAdImpression: (" + adImpressionEvent.getTag() + "\", \"" + adImpressionEvent.getCreativeType() + "\", \"" + adImpressionEvent.getAdPosition().name() + ")\r\n");

    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        print("onAdPause: (" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + ")\r\n");
        updateOutput("onAdPause: (" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + ")\r\n");
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        print("onAdPlay: (" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")\r\n");
        updateOutput("onAdPlay: (" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")\r\n");
    }

    @Override
    public void onVisualQuality(VisualQuality visualQuality) {
        print("onVisualQuality: (" + visualQuality.getQualityLevel().getLabel() + ")\r\n");
        updateOutput("onVisualQuality: (" + visualQuality.getQualityLevel().getLabel() + ")\r\n");
    }
}
