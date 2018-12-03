package com.jwplayer.opensourcedemo;

import android.util.Log;
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

    private JWPlayerView jwPlayerView;

    JWEventHandler(JWPlayerView jwPlayerView) {
        this.jwPlayerView = jwPlayerView;
        setupListeners();
        print("Build Version: " + jwPlayerView.getVersionCode() + "\n");
    }

    private void setupListeners() {
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

    private void print(String s){
        Log.i("JWEVENTHANDLER"," "+ s);
    }

    /**
     * Regular playback events below here
     */

    @Override
    public void onAudioTracks(AudioTracksEvent audioTracksEvent) {
        print("onAudioTracks " + audioTracksEvent);
    }

    public void onBufferChange(BufferChangeEvent bufferChangeEvent) {
        print("onBufferChangeEvent\r\n" +
                " position=" + bufferChangeEvent.getPosition() + "\r\n" +
                " duration=" + bufferChangeEvent.getDuration());
    }
    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        print("onAdClick");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        print("onAdComplete");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        print("onAdSkipped");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        print("onAdImpression(\"" + adImpressionEvent.getTag() + "\r\n" +
                " Video Type: " + adImpressionEvent.getCreativeType()+ "\r\n" +
                " Ad Position: " + adImpressionEvent.getAdPosition().name() + ")\r\n");
    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        print("onAdPause(\"" + adPauseEvent.getTag() + " " + adPauseEvent.getOldState());
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        print("onAdPlay(\"" + adPlayEvent.getTag() + " " + adPlayEvent.getOldState());
    }

    @Override
    public void onAdCompanions(AdCompanionsEvent adCompanionsEvent) {
        print("onAdCompanions  tag:" + adCompanionsEvent);
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        print("onAdError " + adErrorEvent);
    }

    @Override
    public void onAdStarted(AdStartedEvent adStartedEvent) {
        print("onAdStarted " + adStartedEvent);
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        print("onBeforeComplete ");
    }

    @Override
    public void onAdRequest(AdRequestEvent adRequestEvent) {
        print("onAdRequest ");
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        Exception exception = errorEvent.getException();
        Log.i("JWPLAYER-LOG", "onError: " + errorEvent.getMessage(), exception);
    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        print("onAdSchedule ");
    }

    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        print("onBeforePlay ");
    }

    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        print("onAudioTrackChanged ");
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        print("onBuffer ");
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent) {
        print("onCaptionsChanged ");
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        print("onCaptionsList ");
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        print("onComplete ");
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        print("onControls ");
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        print("onDisplayClick ");
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        print("onFirstFrame ");
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        print("onFullscreen ");
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        print("onIdle ");
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        print("onLevelsChanged ");
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        print("onLevels ");
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
        print("onMeta ");
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        print("onMute ");
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        print("onPause ");
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        print("onPlay ");
    }

    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        print("onPlaylistComplete ");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        print("onPlaylistItem ");
    }

    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        print("onPlaylist ");
    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        print("onSeek ");
    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        print("onSeeked ");
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        print("onSetupError ");
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        print("timeEvent);
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQualityEvent) {
        print("onVisualQuality ");
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        print("onReady Setup Time" + readyEvent.getSetupTime());
    }

    @Override
    public void onAdBreakEnd(AdBreakEndEvent adBreakEndEvent) {
        print("onAdBreakEnd Ad Position: " + adBreakEndEvent.getAdPosition());
    }

    @Override
    public void onAdBreakStart(AdBreakStartEvent adBreakStartEvent) {
        print("onAdBreakStart Ad Position: " + adBreakStartEvent.getAdPosition());
    }
}
