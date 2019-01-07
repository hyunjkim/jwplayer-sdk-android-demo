package com.jwplayer.opensourcedemo;

import android.os.Build;
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
import com.longtailvideo.jwplayer.events.AudioTrackChangedEvent;
import com.longtailvideo.jwplayer.events.AudioTracksEvent;
import com.longtailvideo.jwplayer.events.BeforeCompleteEvent;
import com.longtailvideo.jwplayer.events.BeforePlayEvent;
import com.longtailvideo.jwplayer.events.BufferChangeEvent;
import com.longtailvideo.jwplayer.events.BufferEvent;
import com.longtailvideo.jwplayer.events.CaptionsChangedEvent;
import com.longtailvideo.jwplayer.events.CaptionsListEvent;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.ControlsEvent;
import com.longtailvideo.jwplayer.events.DisplayClickEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.FirstFrameEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.IdleEvent;
import com.longtailvideo.jwplayer.events.LevelsChangedEvent;
import com.longtailvideo.jwplayer.events.LevelsEvent;
import com.longtailvideo.jwplayer.events.MetaEvent;
import com.longtailvideo.jwplayer.events.MuteEvent;
import com.longtailvideo.jwplayer.events.PauseEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.PlaylistCompleteEvent;
import com.longtailvideo.jwplayer.events.PlaylistEvent;
import com.longtailvideo.jwplayer.events.PlaylistItemEvent;
import com.longtailvideo.jwplayer.events.ReadyEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.SeekedEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.TimeEvent;
import com.longtailvideo.jwplayer.events.VisualQualityEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements
        VideoPlayerEvents.OnSetupErrorListener,
        VideoPlayerEvents.OnPlaylistListener,
        VideoPlayerEvents.OnPlaylistItemListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnBufferListener,
        VideoPlayerEvents.OnIdleListener,
        VideoPlayerEvents.OnErrorListener,
        VideoPlayerEvents.OnSeekListener,
        VideoPlayerEvents.OnTimeListener,
        VideoPlayerEvents.OnFullscreenListener,
        VideoPlayerEvents.OnAudioTracksListener,
        VideoPlayerEvents.OnAudioTrackChangedListener,
        VideoPlayerEvents.OnCaptionsListListener,
        VideoPlayerEvents.OnMetaListener,
        VideoPlayerEvents.OnPlaylistCompleteListener,
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnLevelsChangedListener,
        VideoPlayerEvents.OnLevelsListener,
        VideoPlayerEvents.OnCaptionsChangedListener,
        VideoPlayerEvents.OnControlsListener,
        VideoPlayerEvents.OnControlBarVisibilityListener,
        VideoPlayerEvents.OnDisplayClickListener,
        VideoPlayerEvents.OnMuteListener,
        VideoPlayerEvents.OnSeekedListener,
        VideoPlayerEvents.OnVisualQualityListener,
        VideoPlayerEvents.OnFirstFrameListener,
        VideoPlayerEvents.OnBufferChangeListener,
        VideoPlayerEvents.OnReadyListener,

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

    private JWPlayerView mPlayer;
    private TextView mOutput;
    private ScrollView mScroll;
    private final StringBuilder outputStringBuilder = new StringBuilder();


    JWEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(outputStringBuilder.append("Build version: ").append(jwPlayerView.getVersionCode()).append("\r\n"));

        // Subscribe to allEventHandler: Player events
        jwPlayerView.addOnSetupErrorListener(this);
        jwPlayerView.addOnPlaylistListener(this);
        jwPlayerView.addOnPlaylistItemListener(this);
        jwPlayerView.addOnPlayListener(this);
        jwPlayerView.addOnPauseListener(this);
        jwPlayerView.addOnBufferListener(this);
        jwPlayerView.addOnIdleListener(this);
        jwPlayerView.addOnErrorListener(this);
        jwPlayerView.addOnSeekListener(this);
        jwPlayerView.addOnTimeListener(this);
        jwPlayerView.addOnFullscreenListener(this);
        jwPlayerView.addOnLevelsChangedListener(this);
        jwPlayerView.addOnLevelsListener(this);
        jwPlayerView.addOnCaptionsListListener(this);
        jwPlayerView.addOnCaptionsChangedListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnControlBarVisibilityListener(this);
        jwPlayerView.addOnControlsListener(this);
        jwPlayerView.addOnDisplayClickListener(this);
        jwPlayerView.addOnMuteListener(this);
        jwPlayerView.addOnVisualQualityListener(this);
        jwPlayerView.addOnSeekedListener(this);
        jwPlayerView.addOnMetaListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
        jwPlayerView.addOnFirstFrameListener(this);
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
        jwPlayerView.addOnReadyListener(this);
    }

    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s){
        Log.i("JWEVENTHANDLER",s);
    }


    /**
     * Regular playback events below here
     */

    @Override
    public void onAudioTracks(AudioTracksEvent audioTracksEvent) {
        updateOutput(" " + "onAudioTracks " + audioTracksEvent.getLevels());
        print(" " + "onAudioTracks " + audioTracksEvent);
    }

    public void onBufferChange(BufferChangeEvent bufferChangeEvent) {
        updateOutput(" " + "onBufferChangeEvent\r\n" +
                " position=" + bufferChangeEvent.getPosition() + "\r\n" +
                " duration=" + bufferChangeEvent.getDuration());
        print(" " + "onBufferChangeEvent\r\n" +
                " position=" + bufferChangeEvent.getPosition() + "\r\n" +
                " duration=" + bufferChangeEvent.getDuration());
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
        updateOutput(" " + "onAdImpression(\"" + adImpressionEvent.getTag() + "\r\n" +
                " Video Type: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + ")\r\n");
        print(" " + "onAdImpression(\"" + adImpressionEvent.getTag() + "\r\n" +
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
        print(" " + "onAdPause(\"" + adPauseEvent.getTag() + " " + adPauseEvent.getOldState());
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput(" " + "onAdPlay(\"" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + ")\r\n");
        print(" " + "onAdPlay(\"" + adPlayEvent.getTag() + " " + adPlayEvent.getOldState());
    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        updateOutput(" " + "onAdCompanions  tag:" + adCompanionsEvent.getTag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adCompanionsEvent
                    .getCompanions()
                    .forEach(e->{
                        print("onAdCompanions-" + e.getResource()+ "\n");
                        printCreatives(e.getCreativeViews());
                    });
        }
    }

    private void printCreatives(List<String> creative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && creative.size()>0) {
            creative.forEach(each -> print("onAdCompanions-Creative: " + each));
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
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        updateOutput(" " + "onBeforeComplete()");
        print(" " + "onBeforeComplete()");
    }


    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        updateOutput(" " + "onAdRequest " + adRequestEvent.getTag());
        print(" " + "onAdRequest tag: " + adRequestEvent.getTag());
        print(" " + "onAdRequest position: " + adRequestEvent.getAdPosition());
        print(" " + "onAdRequest client: " + adRequestEvent.getClient());
        print(" " + "onAdRequest offset: " + adRequestEvent.getOffset());
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        updateOutput("onError: " + errorEvent.getMessage());
        Exception exception = errorEvent.getException();
        Log.i("JWPLAYER-LOG", "onError: " + errorEvent.getMessage(), exception);
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
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        updateOutput(" " + "beforePlayEvent()");
        print(" " + "beforePlayEvent()");
    }

    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        updateOutput(" " + "audioTrackChangedEvent " + audioTrackChangedEvent.getCurrentTrack());
        print(" " + "audioTrackChangedEvent " + audioTrackChangedEvent.getCurrentTrack());
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        updateOutput(" " + "bufferEvent " + bufferEvent.getOldState());
        print(" " + "bufferEvent " + bufferEvent.getOldState());
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent) {
        updateOutput(" " + "captionsChangedEvent " + captionsChangedEvent.getCurrentTrack());
        print(" " + "captionsChangedEvent " + captionsChangedEvent.getCurrentTrack());
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        updateOutput(" " + "captionsListEvent()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            captionsListEvent.getTracks().forEach(e->print("onCaptionsList-"+e.getLabel() +": "+ e.toJson().toString()));
        }
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        updateOutput(" " + "completeEvent()");
        print(" " + "completeEvent()");
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        updateOutput(" " + "controlsEvent " + controlsEvent.getControls());
        print(" " + "controlsEvent " + controlsEvent.getControls());
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        updateOutput(" " + "displayClickEvent()");
        print(" " + "displayClickEvent()");
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        updateOutput(" " + "firstFrameEvent " + firstFrameEvent.getLoadTime());
        print(" " + "firstFrameEvent " + firstFrameEvent.getLoadTime());
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        updateOutput(" " + "fullscreenEvent " + fullscreenEvent.getFullscreen());
        print(" " + "fullscreenEvent " + fullscreenEvent.getFullscreen());
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        updateOutput(" " + "idleEvent " + idleEvent.getOldState());
        print(" " + "idleEvent " + idleEvent.getOldState());
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        updateOutput(" " + "levelsChangedEvent " + levelsChangedEvent.getCurrentQuality());
        print(" " + "levelsChangedEvent " + levelsChangedEvent.getCurrentQuality());
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        updateOutput(" " + "levelsEvent " + levelsEvent.getLevels().size());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            levelsEvent.getLevels().forEach(e-> print("levelsEvent-"+e.getLabel()+":" + e.toJson().toString()));
        }
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
        updateOutput(" " + "metaEvent " + metaEvent.getMetadata().toJson());
        print(" " + "metaEvent " + metaEvent.getMetadata().toJson());
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        updateOutput(" " + "muteEvent " + muteEvent.getMute());
        print(" " + "muteEvent " + muteEvent.getMute());
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        updateOutput(" " + "pauseEvent " + pauseEvent.getOldState());
        print(" " + "pauseEvent " + pauseEvent.getOldState());
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        updateOutput(" " + "playEvent " + playEvent.getOldState());
        print(" " + "playEvent " + playEvent.getOldState());
    }


    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        updateOutput(" " + "playlistCompleteEvent() ");
        print(" " + "playlistCompleteEvent() ");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        updateOutput(" " + "playlistItemEvent index: " + playlistItemEvent.getIndex());
        print(" " + "playlistItemEvent index: " + playlistItemEvent.getIndex());
        print(" " + "playlistItemEvent file: " + playlistItemEvent.getPlaylistItem().getFile());
    }
    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        updateOutput(" " + "playlistEvent() " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
        print(" " + "playlistEvent() " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        updateOutput(" " + "seekEvent()"+seekEvent.getPosition());
        print(" " + "seekEvent position: " + seekEvent.getPosition());
        print(" " + "seekEvent offset: " + seekEvent.getOffset());
    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        updateOutput(" " + "onSeeked() "+ seekedEvent.getPosition());
        print(" " + "onSeeked() "+ seekedEvent.getPosition());
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        updateOutput(" " + "setupErrorEvent " + setupErrorEvent.getMessage());
        print(" " + "setupErrorEvent "+setupErrorEvent.getMessage());
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        updateOutput(" " + "timeEvent " + timeEvent);
//        print(" " + "timeEvent);
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQualityEvent) {
        updateOutput(" " + "visualQualityEvent " + visualQualityEvent.getQualityLevel().toJson());
        print(" " + "visualQualityEvent " + visualQualityEvent.getQualityLevel().toJson());
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        updateOutput(" " + "onReady " + readyEvent.getSetupTime());
        print(" " + "onReady " + readyEvent.getSetupTime());
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
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        boolean isVisible = controlBarVisibilityEvent.isVisible();
        updateOutput("onControlBarVisibilityChanged(): " + isVisible);
        print("onControlBarVisibilityChanged(): " + isVisible);
    }
}
