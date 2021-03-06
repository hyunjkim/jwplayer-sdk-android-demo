package com.jwplayer.opensourcedemo.listeners;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.jwutil.Logger;
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
import com.longtailvideo.jwplayer.events.RelatedCloseEvent;
import com.longtailvideo.jwplayer.events.RelatedOpenEvent;
import com.longtailvideo.jwplayer.events.RelatedPlayEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.SeekedEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.TimeEvent;
import com.longtailvideo.jwplayer.events.VisualQualityEvent;
import com.longtailvideo.jwplayer.events.WarningEvent;
import com.longtailvideo.jwplayer.events.listeners.RelatedPluginEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

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
        VideoPlayerEvents.OnWarningListener,
        RelatedPluginEvents.OnRelatedCloseListener,
        RelatedPluginEvents.OnRelatedOpenListener,
        RelatedPluginEvents.OnRelatedPlayListener {

    private JWPlayerView mPlayer;
    private TextView mOutput;
    private ScrollView mScroll;

    public JWEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mPlayer = jwPlayerView;
        mScroll = scrollview;
        mOutput = output;

        // Subscribe to allEventHandler: Player events
        mPlayer.addOnBufferListener(this);
        mPlayer.addOnCaptionsListListener(this);
        mPlayer.addOnCaptionsChangedListener(this);
        mPlayer.addOnCompleteListener(this);
        mPlayer.addOnControlBarVisibilityListener(this);
        mPlayer.addOnControlsListener(this);
        mPlayer.addOnDisplayClickListener(this);
        mPlayer.addOnErrorListener(this);
        mPlayer.addOnFirstFrameListener(this);
        mPlayer.addOnFullscreenListener(this);
        mPlayer.addOnIdleListener(this);
        mPlayer.addOnLevelsChangedListener(this);
        mPlayer.addOnLevelsListener(this);
        mPlayer.addOnMetaListener(this);
        mPlayer.addOnMuteListener(this);
        mPlayer.addOnPauseListener(this);
        mPlayer.addOnPlayListener(this);
        mPlayer.addOnPlaylistCompleteListener(this);
        mPlayer.addOnPlaylistItemListener(this);
        mPlayer.addOnPlaylistListener(this);
        mPlayer.addOnReadyListener(this);
        mPlayer.addOnRelatedCloseListener(this);
        mPlayer.addOnRelatedOpenListener(this);
        mPlayer.addOnRelatedPlayListener(this);
        mPlayer.addOnSeekListener(this);
        mPlayer.addOnSeekedListener(this);
        mPlayer.addOnSetupErrorListener(this);
        mPlayer.addOnTimeListener(this);
        mPlayer.addOnVisualQualityListener(this);
        mPlayer.addOnWarningListener(this);

    }

    private void updateOutput(String output) {
        mOutput.setText(Logger.generateLogLine(output));
        mScroll.scrollTo(0, mOutput.getBottom());
    }

    private void print(String s) {
        Log.i("JWEVENTHANDLER", s);
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
    public void onError(ErrorEvent errorEvent) {
        updateOutput(" onError: " + errorEvent.getMessage());
        Exception exception = errorEvent.getException();
        Log.i("JWPLAYER-LOG", "onError: " + errorEvent.getMessage(), exception);
    }

    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        updateOutput(" " + "onAudioTrackChanged: " + audioTrackChangedEvent.getCurrentTrack());
        print(" " + "onAudioTrackChanged: " + audioTrackChangedEvent.getCurrentTrack());
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        updateOutput(" " + "onBuffer() " + bufferEvent.getOldState());
        print(" " + "onBuffer() " + bufferEvent.getOldState());
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent) {
        updateOutput(" " + "onCaptionsChanged(): " + captionsChangedEvent.getCurrentTrack());
        print(" " + "onCaptionsChanged(): " + captionsChangedEvent.getCurrentTrack());
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        updateOutput(" " + "onCaptionsList()");

        for (Caption each : captionsListEvent.getTracks()) {
            print("  onCaptionsList()- " + each.getLabel() + ": " + each.toJson().toString());
        }
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        updateOutput(" " + "onComplete()");
        print(" " + "onComplete(): " + completeEvent.toString());
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        updateOutput(" " + "onControls(): " + controlsEvent.getControls());
        print(" " + "onControls(): " + controlsEvent.getControls());
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        updateOutput(" " + "onDisplayClick()");
        print(" " + "onDisplayClick()");
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        updateOutput(" " + "onFirstFrame: " + firstFrameEvent.getLoadTime());
        print(" " + "onFirstFrame: " + firstFrameEvent.getLoadTime());
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        updateOutput(" " + "onFullscreen: " + fullscreenEvent.getFullscreen());
        print(" " + "onFullscreen: " + fullscreenEvent.getFullscreen());
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        updateOutput(" " + "onIdle: " + idleEvent.getOldState());
        print(" " + "onIdle: " + idleEvent.getOldState());
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        updateOutput(" " + "onLevelsChanged: " + levelsChangedEvent.getCurrentQuality());
        print(" " + "onLevelsChanged:" + levelsChangedEvent.getCurrentQuality());
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        updateOutput(" " + "onLevelsEvent size: " + levelsEvent.getLevels().size());
        print(" " + "onLevelsEvent size: " + levelsEvent.getLevels().size());

        for (QualityLevel each : levelsEvent.getLevels()) {
            print(" onLevelsEvent - " + each.toJson().toString());
        }
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
//        updateOutput(" " + "metaEvent " + metaEvent.getMetadata().toJson());
        print(" " + "onMeta " + metaEvent.getMetadata().toJson());
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        updateOutput(" " + "onMute " + muteEvent.getMute());
        print(" " + "onMute " + muteEvent.getMute());
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        updateOutput(" " + "onPause " + pauseEvent.getOldState());
        print(" " + "onPause " + pauseEvent.getOldState());
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        updateOutput(" " + "onPlay " + playEvent.getOldState());
        print(" " + "onPlay " + playEvent.getOldState());
    }


    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        updateOutput(" " + "onPlaylistComplete() ");
        print(" " + "onPlaylistComplete() ");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        updateOutput(" " + "onPlaylistItem index: " + playlistItemEvent.getIndex());
        print(" " + "onPlaylistItem index: " + playlistItemEvent.getIndex());
        print(" " + "onPlaylistItem file: " + playlistItemEvent.getPlaylistItem().getFile());
    }

    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        updateOutput(" " + "onPlaylist() " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
        print(" " + "onPlaylist() " + playlistEvent.getPlaylist().get(mPlayer.getPlaylistIndex()).getFile());
    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        updateOutput(" " + "onSeek()" + seekEvent.getPosition());
        print(" " + "onSeek position: " + seekEvent.getPosition());
        print(" " + "onSeek offset: " + seekEvent.getOffset());
    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        updateOutput(" " + "onSeeked() ");
        print(" " + "onSeeked() " + seekedEvent.toString());
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        updateOutput(" " + "onSetupError " + setupErrorEvent.getMessage());
        print(" " + "onSetupError " + setupErrorEvent.getMessage());
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        updateOutput(" " + "onTime " + timeEvent);
//        print(" " + "onTime");
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQualityEvent) {
        if (visualQualityEvent.getQualityLevel() != null) {
            updateOutput(" " + "onVisualQuality: " + visualQualityEvent.getQualityLevel().toJson());
            print(" " + "onVisualQuality: " + visualQualityEvent.getQualityLevel().toJson());
        }
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        updateOutput(" " + "onReady " + readyEvent.getSetupTime());
        print(" " + "onReady " + readyEvent.getSetupTime());
    }


    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        boolean isVisible = controlBarVisibilityEvent.isVisible();
        updateOutput(" onControlBarVisibilityChanged(): " + isVisible + "\r\n");
        print(" onControlBarVisibilityChanged(): " + isVisible + "\r\n");
    }


    @Override
    public void onRelatedClose(RelatedCloseEvent relatedCloseEvent) {
        updateOutput(" onRelatedClose(): " + relatedCloseEvent.getMethod());
        print(" onRelatedClose(): " + relatedCloseEvent.getMethod());
    }

    @Override
    public void onRelatedOpen(RelatedOpenEvent relatedOpenEvent) {
        updateOutput(" onRelatedOpen()" +
                "method: " + relatedOpenEvent.getMethod() +
                "onRelatedOpen url: " + relatedOpenEvent.getUrl());
        print(" onRelatedOpen() - " + "\r\nmethod: " + relatedOpenEvent.getMethod() + "\r\nurl: " + relatedOpenEvent.getUrl());

        int item = 0;
        if (item < relatedOpenEvent.getItems().size()) {
            for (PlaylistItem each : relatedOpenEvent.getItems()) {
                print(" onRelatedOpen() " + item + ") " + each.toJson().toString());
                item += 1;
            }
        }
    }

    @Override
    public void onRelatedPlay(RelatedPlayEvent relatedPlayEvent) {
        updateOutput(" onRelatedPlay(): " + relatedPlayEvent.getItem().getFile());
        print(" onRelatedPlay(): " +
                "\r\nAuto" + relatedPlayEvent.getAuto() +
                "\r\nFile:" + relatedPlayEvent.getItem().getFile() +
                "\r\nPosition: " + relatedPlayEvent.getPosition());

    }

    @Override
    public void onWarning(WarningEvent warningEvent) {
        updateOutput(" onWarning() " + warningEvent.getMessage());
        print(" onWarning() " + warningEvent.getMessage() + "\r\nonWarning() - Exception: \r\n" + warningEvent.getException());
    }
}
