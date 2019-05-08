package com.jwplayer.opensourcedemo.handlers;

import android.os.Build;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.myutility.Logger;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AudioTrackChangedEvent;
import com.longtailvideo.jwplayer.events.AudioTracksEvent;
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
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        VideoPlayerEvents.OnReadyListener{

    private JWPlayerView mPlayer;
    private TextView mOutput;
    private ScrollView mScroll;
    private final StringBuilder outputStringBuilder = new StringBuilder();


    public JWEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;

        // Subscribe to allEventHandler: Player events
        jwPlayerView.addOnBufferListener(this);
        jwPlayerView.addOnCaptionsListListener(this);
        jwPlayerView.addOnCaptionsChangedListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnControlBarVisibilityListener(this);
        jwPlayerView.addOnControlsListener(this);
        jwPlayerView.addOnDisplayClickListener(this);
        jwPlayerView.addOnErrorListener(this);
        jwPlayerView.addOnFirstFrameListener(this);
        jwPlayerView.addOnFullscreenListener(this);
        jwPlayerView.addOnIdleListener(this);
        jwPlayerView.addOnLevelsChangedListener(this);
        jwPlayerView.addOnLevelsListener(this);
        jwPlayerView.addOnMetaListener(this);
        jwPlayerView.addOnMuteListener(this);
        jwPlayerView.addOnPauseListener(this);
        jwPlayerView.addOnPlayListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
        jwPlayerView.addOnPlaylistItemListener(this);
        jwPlayerView.addOnPlaylistListener(this);
        jwPlayerView.addOnReadyListener(this);
        jwPlayerView.addOnSeekListener(this);
        jwPlayerView.addOnSeekedListener(this);
        jwPlayerView.addOnSetupErrorListener(this);
        jwPlayerView.addOnTimeListener(this);
        jwPlayerView.addOnVisualQualityListener(this);

    }

    private void generateLogLine(String output) {
        mOutput.setText(Logger.log(output));
        mScroll.scrollTo(0, mOutput.getBottom());
    }


    /**
     * Regular playback events below here
     */

    @Override
    public void onAudioTracks(AudioTracksEvent audioTracksEvent) {
        generateLogLine("onAudioTracks size: " + audioTracksEvent.getLevels().size());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            audioTracksEvent.getLevels().forEach(e-> Logger.logEvent("onAudioTracks - " + e.getName() +": " +  e.toJson().toString()));
        }
    }

    public void onBufferChange(BufferChangeEvent bufferChangeEvent) {
        generateLogLine("onBufferChange: " +
               "\r\n\tposition=" + bufferChangeEvent.getPosition() +
                "\r\n\tduration=" + bufferChangeEvent.getDuration());
        Logger.logEvent("onBufferChange: " +
               "\r\n\tposition=" + bufferChangeEvent.getPosition() +
                "\r\n\tduration=" + bufferChangeEvent.getDuration());
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        generateLogLine("onError: " + errorEvent.getMessage());
        Exception exception = errorEvent.getException();
        Logger.logError("onError: " + errorEvent.getMessage(), exception);
    }

    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        generateLogLine("onAudioTrackChanged: " + audioTrackChangedEvent.getCurrentTrack());
        Logger.logEvent("onAudioTrackChanged: " + audioTrackChangedEvent.getCurrentTrack());
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        generateLogLine("onBuffer: " + bufferEvent.getOldState());
        Logger.logEvent("onBuffer: " + bufferEvent.getOldState());
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent) {
        generateLogLine("onCaptionsChanged: " + captionsChangedEvent.getCurrentTrack());
        Logger.logEvent("onCaptionsChanged: " + captionsChangedEvent.getCurrentTrack());
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        generateLogLine("onCaptionsList size: " + captionsListEvent.getTracks().size());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            captionsListEvent.getTracks().forEach(e-> Logger.logEvent("onCaptionsList - " + e.getLabel() +": " +  e.toJson().toString()));
        }
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        generateLogLine("onComplete()");
        Logger.logEvent("onComplete()");
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        generateLogLine("onControls: " + controlsEvent.getControls());
        Logger.logEvent("onControls: " + controlsEvent.getControls());
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        generateLogLine("onDisplayClick()");
        Logger.logEvent("onDisplayClick()");
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        generateLogLine("onFirstFrame: " + firstFrameEvent.getLoadTime());
        Logger.logEvent("onFirstFrame: " + firstFrameEvent.getLoadTime());
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        generateLogLine("onFullscreen: " + fullscreenEvent.getFullscreen());
        Logger.logEvent("onFullscreen: " + fullscreenEvent.getFullscreen());
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        generateLogLine("onIdle: " + idleEvent.getOldState());
        Logger.logEvent("onIdle: " + idleEvent.getOldState());
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        generateLogLine("onLevelsChanged: " + levelsChangedEvent.getCurrentQuality());
        Logger.logEvent("onLevelsChanged:" + levelsChangedEvent.getCurrentQuality());
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        generateLogLine("onlevelsEvent size: " + levelsEvent.getLevels().size());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            levelsEvent.getLevels().forEach(e-> Logger.logEvent("onlevelsEvent-" + e.getLabel()+":" + e.toJson().toString()));
        }
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
//        generateLogLine(metaEvent: " + metaEvent.getMetadata().toJson());
        Logger.logEvent("onMeta: " + metaEvent.getMetadata().toJson());
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        generateLogLine("onMute: " + muteEvent.getMute());
        Logger.logEvent("onMute: " + muteEvent.getMute());
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        generateLogLine("onPause: " + pauseEvent.getOldState());
        Logger.logEvent("onPause: " + pauseEvent.getOldState());
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        generateLogLine("onPlay: " + playEvent.getOldState());
        Logger.logEvent("onPlay: " + playEvent.getOldState());
    }


    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        generateLogLine("onPlaylistComplete()");
        Logger.logEvent("onPlaylistComplete() ");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        generateLogLine("onPlaylistItem index: " + playlistItemEvent.getIndex());
        Logger.logEvent("onPlaylistItem: " +
                "\r\n\tindex: " + playlistItemEvent.getIndex() +
                "\r\n\tfile: " + playlistItemEvent.getPlaylistItem().getFile());
    }
    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        generateLogLine("onPlaylist(): " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
        Logger.logEvent("onPlaylist(): " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        generateLogLine("onSeek: " + seekEvent.getPosition());
        Logger.logEvent("onSeek position: " + seekEvent.getPosition());
        Logger.logEvent("onSeek offset: " + seekEvent.getOffset());
    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        generateLogLine("onSeeked position: " + seekedEvent.getPosition());
        Logger.logEvent("onSeeked position: " + seekedEvent.getPosition());
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        generateLogLine("onSetupError: " + setupErrorEvent.getMessage());
        Logger.logEvent("onSetupError: " + setupErrorEvent.getMessage());
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        generateLogLine("onTime: " + timeEvent);
//        Logger.logEvent("onTime: " + timeEvent);
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQualityEvent) {
        if(visualQualityEvent.getQualityLevel() != null){
            generateLogLine("onVisualQuality: " + visualQualityEvent.getQualityLevel().toJson());
            Logger.logEvent("onVisualQuality: " + visualQualityEvent.getQualityLevel().toJson());
        }
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        generateLogLine("onReady: " + readyEvent.getSetupTime());
        Logger.logEvent("onReady: " + readyEvent.getSetupTime());
    }


    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        boolean isVisible = controlBarVisibilityEvent.isVisible();
        generateLogLine("onControlBarVisibilityChanged: " + isVisible);
        Logger.logEvent("onControlBarVisibilityChanged: " + isVisible);
    }
}
