package com.jwplayer.opensourcedemo;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.AdClickEvent;
import com.longtailvideo.jwplayer.events.AdCompleteEvent;
import com.longtailvideo.jwplayer.events.AdErrorEvent;
import com.longtailvideo.jwplayer.events.AdImpressionEvent;
import com.longtailvideo.jwplayer.events.AdPauseEvent;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdScheduleEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.AudioTrackChangedEvent;
import com.longtailvideo.jwplayer.events.AudioTracksEvent;
import com.longtailvideo.jwplayer.events.BeforeCompleteEvent;
import com.longtailvideo.jwplayer.events.BeforePlayEvent;
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
import com.longtailvideo.jwplayer.events.RelatedCloseEvent;
import com.longtailvideo.jwplayer.events.RelatedOpenEvent;
import com.longtailvideo.jwplayer.events.RelatedPlayEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.SeekedEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.TimeEvent;
import com.longtailvideo.jwplayer.events.VisualQualityEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.RelatedPluginEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements VideoPlayerEvents.OnSetupErrorListener,
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
        AdvertisingEvents.OnAdClickListener,
        AdvertisingEvents.OnAdCompleteListener,
        AdvertisingEvents.OnAdSkippedListener,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdImpressionListener,
        AdvertisingEvents.OnAdTimeListener,
        AdvertisingEvents.OnAdPauseListener,
        AdvertisingEvents.OnAdPlayListener,
        AdvertisingEvents.OnAdScheduleListener,
        AdvertisingEvents.OnBeforePlayListener,
        AdvertisingEvents.OnBeforeCompleteListener,

        RelatedPluginEvents.OnRelatedCloseListener,
        RelatedPluginEvents.OnRelatedOpenListener,
        RelatedPluginEvents.OnRelatedPlayListener {

    boolean mDisplayClicked = false;
    private String TAG = JWEventHandler.class.getName();
    private TextView mOutput;
    private JWPlayerView mPlayerView;

    JWEventHandler(JWPlayerView jwPlayerView, TextView output) {
        mOutput = output;
        mPlayerView = jwPlayerView;

        // Subscribe to all JW Player events
        jwPlayerView.addOnFirstFrameListener(this);
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
        jwPlayerView.addOnControlBarVisibilityListener(this);
        jwPlayerView.addOnCaptionsChangedListener(this);
        jwPlayerView.addOnRelatedCloseListener(this);
        jwPlayerView.addOnRelatedOpenListener(this);
        jwPlayerView.addOnRelatedPlayListener(this);
        jwPlayerView.addOnControlsListener(this);
        jwPlayerView.addOnDisplayClickListener(this);
        jwPlayerView.addOnMuteListener(this);
        jwPlayerView.addOnVisualQualityListener(this);
        jwPlayerView.addOnSeekedListener(this);
        jwPlayerView.addOnAdClickListener(this);
        jwPlayerView.addOnAdCompleteListener(this);
        jwPlayerView.addOnAdSkippedListener(this);
        jwPlayerView.addOnAdErrorListener(this);
        jwPlayerView.addOnAdImpressionListener(this);
        jwPlayerView.addOnAdTimeListener(this);
        jwPlayerView.addOnAdPauseListener(this);
        jwPlayerView.addOnAdPlayListener(this);
        jwPlayerView.addOnMetaListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnBeforePlayListener(this);
        jwPlayerView.addOnBeforeCompleteListener(this);
        jwPlayerView.addOnAdScheduleListener(this);
    }

    private void updateOutput(String output) {
        mOutput.setText(output);
    }

    /**
     * Regular playback events below here
     */


    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        print("onBuffer " + bufferEvent.getOldState());
        updateOutput("onBuffer()");
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        print("firstFrameEvent " + mPlayerView.getState());
        updateOutput("onFirstFrame()");
    }

    @Override
    public void onAudioTracks(AudioTracksEvent audioTracksEvent) {
        print("onAudioTracks");
        updateOutput("onAudioTracks(List<AudioTrack>)");
    }

    @Override
    public void onBeforeComplete(BeforeCompleteEvent beforeCompleteEvent) {
        print("onBeforeComplete");
        updateOutput("onBeforeComplete()");
    }

    @Override
    public void onBeforePlay(BeforePlayEvent beforePlayEvent) {
        print("onBeforePlay");
        updateOutput("onBeforePlay()");
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        print("onCaptionsList");
        updateOutput("onCaptionsList(List<Caption>)");
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        print("onComplete");
        updateOutput("onComplete()");
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreen) {
        Log.d(TAG, "onFullscreen");
        updateOutput("onFullscreen(" + fullscreen.getFullscreen() + ")");
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        print("onIdle");
        updateOutput("onIdle()");
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
        print("onMeta");
        updateOutput("onMeta()");
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {

        print("onPause: did it hit onDisplayClicked? " + mDisplayClicked);

        if (!mDisplayClicked) {
            print("onPause() - Settings was touched!");
        }
        mDisplayClicked = false;
//        print("onPause()");
        updateOutput("onPause()");
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        print("onPlay() set displayclick false");
        updateOutput("onPlay()");
    }

    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        print("onPlaylistComplete");
        updateOutput("onPlaylistComplete()");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        print("onPlaylistItem");
        updateOutput("onPlaylistItem()");
    }

    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        print("onPlaylist");
        updateOutput("onPlaylist()");
    }


    @Override
    public void onSeek(SeekEvent seekEvent) {
        print("onSeek(" + seekEvent.getPosition() + ", " + seekEvent.getOffset() + ")");
        updateOutput("onSeek(" + seekEvent.getPosition() + ", " + seekEvent.getOffset() + ")");
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        Log.d("onSetupError: ", setupErrorEvent.getMessage());
        updateOutput("onSetupError(\"" + setupErrorEvent.getMessage() + "\")");
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        Log.d(TAG, timeEvent.getDuration() + "  ***  ");
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        updateOutput("onAdError(\"" + "\", \"" + adErrorEvent.getMessage() + "\")");
        Log.d(TAG, "onAdError tag : " + adErrorEvent.getTag());
        Log.d(TAG, "onAdError msg : " + adErrorEvent.getMessage());
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        print("onError : " + errorEvent);
        Log.d(TAG, "onError : " + errorEvent.getMessage());
        updateOutput("onError(\"" + errorEvent.getMessage() + "\")");
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        print("onLevelsChange(" + levelsChangedEvent.getCurrentQuality() + ")");
        updateOutput("onLevelsChange(" + levelsChangedEvent.getCurrentQuality() + ")");
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        print("onLevels");
        updateOutput("onLevels(List<QualityLevel>) size: " + levelsEvent.getLevels().size());
    }

    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        print("onAudioTrackChanged");
        updateOutput("onAudioTrackChanged(" + audioTrackChangedEvent.getCurrentTrack() + ")");
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent list) {
        Log.d(TAG, "onCaptionsChanged");
        updateOutput("onCaptionsChanged(" + list.getCurrentTrack() + ", List<Caption>)");
    }

    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        print("onAdClick");
        updateOutput("onAdClick(\"" + adClickEvent.getTag() + "\")");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        print("onAdComplete");
        updateOutput("onAdComplete(\"" + adCompleteEvent.getTag() + "\")");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        print("onAdSkipped");
        updateOutput("onAdSkipped(\"" + adSkippedEvent.getTag() + "\")");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        print("onAdImpression");
        updateOutput("onAdImpression(\"" + adImpressionEvent.getTag() + "\", \"" + adImpressionEvent.getCreativeType() + "\", \"" + adImpressionEvent.getAdPosition().name() + "\")");

    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        print("onAdTime");
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        print("onAdPause");
        updateOutput("onAdPause(\"" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + "\")");
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        print("onAdPlay");
        updateOutput("onAdPlay(\"" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + "\")");
    }

    public void onSeeked(SeekedEvent seekedEvent) {
        print("onSeeked current position: " + seekedEvent.getPosition());
        updateOutput("onSeeked(\"" + "seeked" + "\")");
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        print("onControls");
        updateOutput("onControls(\"" + controlsEvent.getControls() + "\")");
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        mDisplayClicked = true;

        print("onDisplayClick()");
        updateOutput("onDisplayClick()");
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQuality) {
        Log.d(TAG, "onVisualQuality");
        updateOutput("onVisualQuality(\"" + "\")");
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        print("onMute");
        updateOutput("onMute()");

    }

    @Override
    public void onAdSchedule(AdScheduleEvent adScheduleEvent) {
        print("onAdSchedule");
        updateOutput("onAdSchedule()");
    }

    @Override
    public void onRelatedClose(RelatedCloseEvent relatedCloseEvent) {
        updateOutput("onRelatedClose(): " + relatedCloseEvent.getMethod());
        print("onRelatedClose(): " + relatedCloseEvent.getMethod());
    }

    @Override
    public void onRelatedOpen(RelatedOpenEvent relatedOpenEvent) {
        updateOutput("onRelatedOpen()" +
                "method: " + relatedOpenEvent.getMethod() +
                "onRelatedOpen url: " + relatedOpenEvent.getUrl());
        print("onRelatedOpen()" + "\r\nmethod: " + relatedOpenEvent.getMethod() + "\r\nurl: " + relatedOpenEvent.getUrl());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            print("onRelatedOpen getitems(): ");
            relatedOpenEvent.getItems().forEach(e -> print(" getitems - " + e + "\r\n"));
        }
    }

    @Override
    public void onRelatedPlay(RelatedPlayEvent relatedPlayEvent) {
        updateOutput("onRelatedPlay(): " + relatedPlayEvent.getItem().getFile());
        print("onRelatedPlay(): " +
                "\r\nAuto" + relatedPlayEvent.getAuto() +
                "\r\nFile:" + relatedPlayEvent.getItem().getFile() +
                "\r\nPosition: " + relatedPlayEvent.getPosition());

    }

    private void print(String s) {
        Log.d("JWPLAYEREVENTHANDLER", s);
    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {

        print("onControlBarVisibilityChanged()");
        updateOutput("onControlBarVisibilityChanged()");
    }

}
