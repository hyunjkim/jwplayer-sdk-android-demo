package com.jwplayer.opensourcedemo;

import android.util.Log;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AudioTrackChangedEvent;
import com.longtailvideo.jwplayer.events.AudioTracksEvent;
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
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;

import java.util.Arrays;

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
        VideoPlayerEvents.OnReadyListener{

    private JWPlayerViewExample mPlayerViewExample;
    private JWPlayerFragmentExample mPlayerFragmentExample;
    private JWPlayerView mPlayer;


    JWEventHandler(JWPlayerFragmentExample jwPlayerFragmentExample, JWPlayerView jwPlayerView) {
        mPlayerFragmentExample = jwPlayerFragmentExample;
        mPlayer = jwPlayerView;
        attachListeners();
    }

    JWEventHandler(JWPlayerViewExample jwPlayerViewExample, JWPlayerView jwPlayerView) {
        mPlayerViewExample = jwPlayerViewExample;
        mPlayer = jwPlayerView;
        attachListeners();
    }

    private void attachListeners() {
        // Subscribe to allEventHandler: Player events
        mPlayer.addOnSetupErrorListener(this);
        mPlayer.addOnPlaylistListener(this);
        mPlayer.addOnPlaylistItemListener(this);
        mPlayer.addOnPlayListener(this);
        mPlayer.addOnPauseListener(this);
        mPlayer.addOnBufferListener(this);
        mPlayer.addOnIdleListener(this);
        mPlayer.addOnErrorListener(this);
        mPlayer.addOnSeekListener(this);
        mPlayer.addOnTimeListener(this);
        mPlayer.addOnFullscreenListener(this);
        mPlayer.addOnLevelsChangedListener(this);
        mPlayer.addOnLevelsListener(this);
        mPlayer.addOnCaptionsListListener(this);
        mPlayer.addOnCaptionsChangedListener(this);
        mPlayer.addOnCompleteListener(this);
        mPlayer.addOnControlsListener(this);
        mPlayer.addOnDisplayClickListener(this);
        mPlayer.addOnMuteListener(this);
        mPlayer.addOnVisualQualityListener(this);
        mPlayer.addOnSeekedListener(this);
        mPlayer.addOnMetaListener(this);
        mPlayer.addOnPlaylistCompleteListener(this);
        mPlayer.addOnFirstFrameListener(this);
        mPlayer.addOnReadyListener(this);
    }

    private void print(String s){
        Log.i("JWEVENTHANDLER",s);
    }

    private void logToPlayer(String output){
        if(mPlayerViewExample==null){
            mPlayerFragmentExample.logout(output);
        } else mPlayerViewExample.logout(output);
    }
    /**
     * Regular playback events below here
     */

    @Override
    public void onAudioTracks(AudioTracksEvent audioTracksEvent) {
        for(AudioTrack audioTrack :  audioTracksEvent.getLevels()){
            logToPlayer(" " + "onAudioTracks "+ audioTrack.getName() +": " + audioTrack.toJson().toString());
            print(" " + "onAudioTracks "+ audioTrack.getName() +": " + audioTrack.toJson().toString());
        }
    }

    public void onBufferChange(BufferChangeEvent bufferChangeEvent) {
        String bufferChange = " " + "onBufferChangeEvent\r\n" +
                " position=" + bufferChangeEvent.getPosition() + "\r\n" +
                " duration=" + bufferChangeEvent.getDuration();
        logToPlayer(bufferChange);
        print(bufferChange);
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        logToPlayer("onError: " + errorEvent.getMessage());
        print("onError: " + errorEvent.getMessage());

        Exception exception = errorEvent.getException();
        if (exception != null) {
            print("\r\nException Message: " + exception.toString());
        }
    }


    @Override
    public void onAudioTrackChanged(AudioTrackChangedEvent audioTrackChangedEvent) {
        logToPlayer(" " + "audioTrackChangedEvent " + audioTrackChangedEvent.getCurrentTrack());
        print(" " + "audioTrackChangedEvent " + audioTrackChangedEvent.getCurrentTrack());
    }

    @Override
    public void onBuffer(BufferEvent bufferEvent) {
        logToPlayer(" " + "bufferEvent " + bufferEvent.getOldState());
        print(" " + "bufferEvent " + bufferEvent.getOldState());
    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent) {
        logToPlayer(" " + "captionsChangedEvent " + captionsChangedEvent.getCurrentTrack());
        print(" " + "captionsChangedEvent " + captionsChangedEvent.getCurrentTrack());
    }

    @Override
    public void onCaptionsList(CaptionsListEvent captionsListEvent) {
        for(Caption caption : captionsListEvent.getTracks()){
            logToPlayer(" " + "captionsListEvent " + caption.getLabel() + ": " + caption.toJson().toString());
            print(" " + "captionsListEvent " + caption.getLabel() + ": " + caption.toJson().toString());
        }
    }

    @Override
    public void onComplete(CompleteEvent completeEvent) {
        logToPlayer(" " + "completeEvent() ");
        print(" " + "completeEvent " + completeEvent);
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        logToPlayer(" " + "controlsEvent: " + controlsEvent.getControls());
        print(" " + "controlsEvent: " + controlsEvent.getControls());
    }

    @Override
    public void onDisplayClick(DisplayClickEvent displayClickEvent) {
        logToPlayer(" " + "displayClickEvent() ");
        print(" " + "displayClickEvent " + displayClickEvent);
    }

    @Override
    public void onFirstFrame(FirstFrameEvent firstFrameEvent) {
        logToPlayer(" " + "firstFrameEvent " + firstFrameEvent.getLoadTime());
        print(" " + "firstFrameEvent " + firstFrameEvent.getLoadTime());
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        logToPlayer(" " + "fullscreenEvent " + fullscreenEvent.getFullscreen());
        print(" " + "fullscreenEvent " + fullscreenEvent.getFullscreen());
    }

    @Override
    public void onIdle(IdleEvent idleEvent) {
        logToPlayer(" " + "idleEvent " + idleEvent.getOldState());
        print(" " + "idleEvent " + idleEvent.getOldState());
    }

    @Override
    public void onLevelsChanged(LevelsChangedEvent levelsChangedEvent) {
        logToPlayer(" " + "levelsChangedEvent " + levelsChangedEvent.getCurrentQuality());
        print(" " + "levelsChangedEvent " + levelsChangedEvent.getCurrentQuality());
    }

    @Override
    public void onLevels(LevelsEvent levelsEvent) {
        StringBuilder sb = new StringBuilder();

        logToPlayer("LevelsEvent: " );
        for(QualityLevel ql : levelsEvent.getLevels()){
            sb.append(ql.getLabel()).append("\r\n");
            print(" " + "LevelsEvent " + ql.getLabel() + ": " + ql.toJson());
        }
        if(sb.length() > 0) logToPlayer(sb.toString());
    }

    @Override
    public void onMeta(MetaEvent metaEvent) {
        logToPlayer(" " + "metaEvent " + metaEvent.getMetadata().toJson().toString());
        print(" " + "metaEvent " + metaEvent.getMetadata().toJson().toString());
    }

    @Override
    public void onMute(MuteEvent muteEvent) {
        logToPlayer(" " + "muteEvent " + muteEvent.getMute());
        print(" " + "muteEvent " + muteEvent.getMute());
    }

    @Override
    public void onPause(PauseEvent pauseEvent) {
        logToPlayer(" " + "pauseEvent " + pauseEvent.getOldState());
        print(" " + "pauseEvent " + pauseEvent.getOldState());
    }

    @Override
    public void onPlay(PlayEvent playEvent) {
        logToPlayer(" " + "playEvent " + playEvent.getOldState());
        print(" " + "playEvent " + playEvent.getOldState());
    }


    @Override
    public void onPlaylistComplete(PlaylistCompleteEvent playlistCompleteEvent) {
        logToPlayer(" " + "playlistCompleteEvent " + playlistCompleteEvent);
        print(" " + "playlistCompleteEvent ");
    }

    @Override
    public void onPlaylistItem(PlaylistItemEvent playlistItemEvent) {
        logToPlayer(" " + "playlistItemEvent " + playlistItemEvent.getPlaylistItem().getFile());
        print(" " + "playlistItemEvent file:" + playlistItemEvent.getPlaylistItem().getFile() + "\r\n"
                + "playlistItemEvent index:" + playlistItemEvent.getIndex());
    }
    @Override
    public void onPlaylist(PlaylistEvent playlistEvent) {
        logToPlayer(" " + "playlistEvent " + playlistEvent.getPlaylist().size());
        print(" " + "playlistEvent " + playlistEvent.getPlaylist().size());
    }

    @Override
    public void onSeek(SeekEvent seekEvent) {
        logToPlayer(" " + "seekEvent " + seekEvent.getOffset());
        print(" " + "seekEvent offset" + seekEvent.getOffset() + "\r\n"
                + "seekEvent position" + seekEvent.getPosition());
    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent) {
        logToPlayer(" " + "seekedEvent " + seekedEvent);
        print(" " + "seekedEvent ");
    }

    @Override
    public void onSetupError(SetupErrorEvent setupErrorEvent) {
        logToPlayer(" " + "setupErrorEvent " + setupErrorEvent.getMessage());
        print(" " + "setupErrorEvent " + setupErrorEvent.getMessage());
    }

    @Override
    public void onTime(TimeEvent timeEvent) {
//        logToPlayer();(" " + "timeEvent " + timeEvent);
//        print(" " + "timeEvent);
    }

    @Override
    public void onVisualQuality(VisualQualityEvent visualQualityEvent) {
        if( visualQualityEvent.getQualityLevel()!=null) {
            logToPlayer(" " + visualQualityEvent.getQualityLevel().toJson().toString());
            print(" " + visualQualityEvent.getQualityLevel().toJson().toString());
        }
        logToPlayer(" " + "visualQualityEvent mode: " + visualQualityEvent.getMode()+
                "\r\nreason: " +visualQualityEvent.getReason());
        print(" " + "visualQualityEvent mode: " + visualQualityEvent.getMode()+
                "\r\nreason: " +visualQualityEvent.getReason());
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        logToPlayer(" " + "onReady " + readyEvent.getSetupTime());
        print(" " + "onReady " + readyEvent.getSetupTime());
    }

}
