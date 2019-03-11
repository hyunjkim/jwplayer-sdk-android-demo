package com.jwplayer.opensourcedemo.listeners;

import android.os.Build;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.jwutil.Logger;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        AdvertisingEvents.OnBeforePlayListener{

    private TextView mOutput;
    private ScrollView mScroll;
    private JWPlayerView mPlayer;


    public JWAdEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(Logger.printBuildVersion(jwPlayerView.getVersionCode()));

        // Subscribe to allEventHandler: Player events
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
        mPlayer.addOnBeforeCompleteListener(this);
        mPlayer.addOnBeforePlayListener(this);
    }

    private void updateOutput(String output) {
        String adOutput = Logger.updateOutput(output);
        mOutput.setText(adOutput);
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s){
        Log.i("JWEVENT", " - (ADEVENT) - "+ s);
    }

    @Override
    public void onAdBreakEnd(AdBreakEndEvent adBreakEndEvent) {
        updateOutput(" " + "AdBreakEndEvent " + adBreakEndEvent.getAdPosition());
        print(" " + "AdBreakEndEvent " + adBreakEndEvent.getAdPosition());
    }

    @Override
    public void onAdBreakStart(AdBreakStartEvent adBreakStartEvent) {
        updateOutput(" " + "AdBreakStartEvent " + adBreakStartEvent.getAdPosition());
        print(" " + "AdBreakStartEvent " + adBreakStartEvent.getAdPosition());
    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        updateOutput(" " + "onAdSchedule " + adScheduleEvent.getTag());
        print(" " + "onAdSchedule " + adScheduleEvent.getClient());
        print(" " + "onAdSchedule " + adScheduleEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adScheduleEvent.getVmapAdBreaks().forEach(e->print("onAdSchedule-vmap ad break:" +e.toJson().toString()));
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        updateOutput(" " + "adErrorEvent: " + adErrorEvent.getMessage());
        print(" " + "adErrorEvent message: " + adErrorEvent.getMessage());
        print(" " + "adErrorEvent tag: " + adErrorEvent.getTag());
    }

    @Override
    public void onAdStarted(AdStartedEvent adStartedEvent) {

        updateOutput(" " + "adStartedEvent " + adStartedEvent.getCreativeType());
        print(" " + "adStartedEvent " + adStartedEvent.getTag());
    }

    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        updateOutput(" " + "onAdRequest " + adRequestEvent.getTag()+ "\r\n "
                + "onAdRequest client: " + adRequestEvent.getClient());
        print(" " + "onAdRequest tag: " + adRequestEvent.getTag());
        print(" " + "onAdRequest position: " + adRequestEvent.getAdPosition());
        print(" " + "onAdRequest client: " + adRequestEvent.getClient());
        print(" " + "onAdRequest offset: " + adRequestEvent.getOffset());
    }


    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        updateOutput(" " + "onAdClick(\"" + adClickEvent.getTag() + ")\r\n");
        print(" " + "onAdClick");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        updateOutput(" " + "onAdComplete(\"" + adCompleteEvent.getTag() + ")\r\n");
        print(" " + "onAdComplete");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        updateOutput(" " + "onAdSkipped(\"" + adSkippedEvent.getTag() + ")\r\n");
        print(" " + "onAdSkipped");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        updateOutput(" " + "onAdImpression: (\r\n" +
                " Tag" + adImpressionEvent.getTag() + "\r\n" +
                " Client: " + adImpressionEvent.getClient()+ "\r\n" +
                " CreativeType: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + ")\r\n");

        if(adImpressionEvent.getMediaFile()!=null ) print("onAdImpression - Media File: " + adImpressionEvent.getMediaFile().getFile()+ "\r\n");
        if(adImpressionEvent.getVmapInfo() !=null ) print("onAdImpression - VmapInfo: " + adImpressionEvent.getVmapInfo().toJson()+ "\r\n");

        print(" " + "onAdImpression: (\r\n" +
                " Tag: " + adImpressionEvent.getTag() + "\r\n" +
                " Universal Ad Id Value: " + adImpressionEvent.getUniversalAdIdValue()+ "\r\n" +
                " Universal Ad Id Registry: " + adImpressionEvent.getUniversalAdIdRegistry()+ "\r\n" +
                " ClickThrough URL: " + adImpressionEvent.getClickThroughUrl() + "\r\n" +
                " getMediaFileCompliance: " + Objects.requireNonNull(adImpressionEvent.getMediaFileCompliance()).toString() + "\r\n" +
                " getNonComplianceReasons: " + Arrays.toString(adImpressionEvent.getNonComplianceReasons()) + "\r\n" +
                " Vast Version: " + adImpressionEvent.getVastVersion()+ "\r\n" +
                " Client: " + adImpressionEvent.getClient()+ "\r\n" +
                " CreativeType: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Title: " + adImpressionEvent.getAdTitle() + "\r\n" +
                " Ad Categories: " + Arrays.toString(adImpressionEvent.getCategories()) + "\r\n" +
                " Ad System: " + adImpressionEvent.getAdSystem() + "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + "\r\n)\r\n");
    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        updateOutput(" " + "onAdPause(\"" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + "\")\n");
        print(" " + "onAdPause(\"" + adPauseEvent.getTag() + " " + adPauseEvent.getOldState());
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput(" " + "onAdPlay(" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")\r\n");
        print(" " + "onAdPlay(" + adPlayEvent.getTag() +
                "\r\nold state: " + adPlayEvent.getOldState() +
                "\r\ncreative type: " + adPlayEvent.getCreativeType() + ")\r\n");
    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        updateOutput(" " + "onAdCompanions tag:" + adCompanionsEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adCompanionsEvent
                    .getCompanions()
                    .forEach(e->{
                        print("onAdCompanions click - " + e.getClick()+ "\r\n" +
                                "onAdCompanions height - " + e.getHeight()+ "\r\n" +
                                "onAdCompanions width - " + e.getWidth()+ "\r\n" +
                                "onAdCompanions type - " + e.getType()+ "\r\n" +
                                "onAdCompanions resource - " + e.getResource()+ "\n");
                        printCreatives(e.getCreativeViews());
                    });
        }
    }

    private void printCreatives(List<String> creative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && creative.size() > 0) {
            creative.forEach(each -> print("onAdCompanions creative -" + each));
        }
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        updateOutput(" " + "onBeforeComplete()");
        print(" " + "onBeforeComplete(): " + beforeCompleteEvent);
    }

    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        updateOutput(" " + "onBeforePlay()");
        print(" " + "onBeforePlay(): " + beforePlayEvent);
    }

}
