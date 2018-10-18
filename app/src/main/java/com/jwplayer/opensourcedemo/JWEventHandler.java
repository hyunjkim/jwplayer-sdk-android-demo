package com.jwplayer.opensourcedemo;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.metadata.id3.BinaryFrame;
import com.google.android.exoplayer2.metadata.id3.GeobFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.AdClickEvent;
import com.longtailvideo.jwplayer.events.AdCompleteEvent;
import com.longtailvideo.jwplayer.events.AdImpressionEvent;
import com.longtailvideo.jwplayer.events.AdPauseEvent;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.AdSkippedEvent;
import com.longtailvideo.jwplayer.events.AdTimeEvent;
import com.longtailvideo.jwplayer.events.ControlsEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.RelatedCloseEvent;
import com.longtailvideo.jwplayer.events.RelatedOpenEvent;
import com.longtailvideo.jwplayer.events.RelatedPlayEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.adaptive.VisualQuality;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.meta.Metadata;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        VideoPlayerEvents.OnErrorListenerV2,
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
        VideoPlayerEvents.OnRelatedCloseListener,
        VideoPlayerEvents.OnControlsListener,
        VideoPlayerEvents.OnDisplayClickListener,
        VideoPlayerEvents.OnMuteListener,
        VideoPlayerEvents.OnRelatedOpenListener,
        VideoPlayerEvents.OnRelatedPlayListener,
        VideoPlayerEvents.OnSeekedListener,
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
    private final StringBuilder outputStringBuilder = new StringBuilder();


    public JWEventHandler(JWPlayerView jwPlayerView, TextView output, ScrollView scrollview) {
        mScroll = scrollview;
        mOutput = output;
        mOutput.setText(outputStringBuilder.append("Build version: ").append(jwPlayerView.getVersionCode()).append("\r\n"));

        // Subscribe to all JW Player events
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
    }

    private void updateOutput(String output) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n");
        mOutput.setText(outputStringBuilder.toString());
        mScroll.scrollTo(0, mOutput.getBottom());

    }

    private void print(String s){
        Log.i("JWPLAYER-LOG", s);
    }

    /**
     * Regular playback events below here
     */

    @Override
    public void onAudioTracks(List<AudioTrack> audioTracks) {
        updateOutput("onAudioTracks(List<AudioTrack>)");
        print("onAudioTracks(List<AudioTrack>)");
    }

    @Override
    public void onBeforeComplete() {
        updateOutput("onBeforeComplete()");
        print("onBeforeComplete()");
    }

    @Override
    public void onBeforePlay() {
        updateOutput("onBeforePlay()");
        print("onBeforePlay()");
    }

    @Override
    public void onBuffer(PlayerState oldState) {
        updateOutput("onBuffer(" + oldState + ")");
        print("onBuffer(" + oldState + ")");
    }

    @Override
    public void onCaptionsList(List<Caption> tracks) {
        updateOutput("onCaptionsList(List<Caption>)");
        print("onCaptionsList(List<Caption>)");
    }

    @Override
    public void onComplete() {
        updateOutput("onComplete()");
        print("onComplete()");
    }

    @Override
    public void onFullscreen(boolean fullscreen) {
        updateOutput("onFullscreen(" + fullscreen + ")");
        print("onFullscreen(" + fullscreen + ")");
    }

    @Override
    public void onIdle(PlayerState oldState) {
        updateOutput("onIdle(" + oldState + ")");
        print("onIdle(" + oldState + ")");
    }

    @Override
    public void onMeta(Metadata meta) {
        updateOutput("onMeta(Metadata)");
        print("onMeta(Metadata)");

        if(meta.getId3Metadata().size() > 0) {
            List<Id3Frame> id3 = meta.getId3Metadata();
            for(Id3Frame id3Obj : id3) {
                if(id3Obj instanceof TextInformationFrame) {
                    TextInformationFrame txxxFrame = (TextInformationFrame)id3Obj;
                } else if(id3Obj instanceof PrivFrame) {
                    PrivFrame privFrame = (PrivFrame)id3Obj;
                } else if(id3Obj instanceof GeobFrame) {
                    GeobFrame geobFrame = (GeobFrame)id3Obj;
                } else if(id3Obj instanceof BinaryFrame) {
                    BinaryFrame binaryFrame = (BinaryFrame)id3Obj;
                }
            }
        }
    }

    @Override
    public void onPause(PlayerState oldState) {
        updateOutput("onPause(" + oldState + ")");
        print("onPause(" + oldState + ")");
    }

    @Override
    public void onPlay(PlayerState oldState) {
        updateOutput("onPlay(" + oldState + ")");
        print("onPlay(" + oldState + ")");
    }

    @Override
    public void onPlaylistComplete() {
        updateOutput("onPlaylistComplete()");
        print("onPlaylistComplete()");
    }

    @Override
    public void onPlaylistItem(int index, PlaylistItem playlistItem) {
        updateOutput("onPlaylistItem(" + index + ", PlaylistItem)");
        print("onPlaylistItem(" + index + ", PlaylistItem)");
    }

    @Override
    public void onPlaylist(List<PlaylistItem> playlist) {
        updateOutput("onPlaylist(List<PlaylistItem>)");
        print("onPlaylist(List<PlaylistItem>)");
    }

    @Override
    public void onSeek(int position, int offset) {
        updateOutput("onSeek(" + position + ", " + offset + ")");
        print("onSeek(" + position + ", " + offset + ")");
    }

    @Override
    public void onSetupError(String message) {
        updateOutput("onSetupError(\"" + message + "\")");
        print("onSetupError(\"" + message + "\")");
    }

    @Override
    public void onTime(long position, long duration) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdError(String tag, String message) {
        updateOutput("onAdError(\"" + tag + "\", \"" + message + "\")");
        print("onAdError(\"" + tag + "\", \"" + message + "\")");
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        updateOutput("onError(\"" + errorEvent.getMessage() + "\")");
        print("onError(\"" + errorEvent.getMessage() + "\")");
        Log.i("JWEVENTHANDLER", "onError"+ errorEvent.getMessage());
    }

    @Override
    public void onLevelsChanged(int i) {
        updateOutput("onLevelsChange(" + i + ")");
        print("onLevelsChange(" + i + ")");
    }

    @Override
    public void onLevels(List<QualityLevel> list) {
        print("onLevels(List<QualityLevel>)");
        updateOutput("onLevels(List<QualityLevel>)");
    }

    @Override
    public void onAudioTrackChanged(int i) {
        updateOutput("onAudioTrackChanged(" + i + ")");
        print("onAudioTrackChanged(" + i + ")");
    }

    @Override
    public void onCaptionsChanged(int i, List<Caption> list) {
        updateOutput("onCaptionsChanged(" + i + ", List<Caption>)");
        print("onCaptionsChanged(" + i + ", List<Caption>)");
    }

    @Override
    public void onAdClick(AdClickEvent adClickEvent) {
        updateOutput("onAdClick(\"" + adClickEvent.getTag() + "\")");
        print("onAdClick(\"" + adClickEvent.getTag() + "\")");
    }

    @Override
    public void onAdComplete(AdCompleteEvent adCompleteEvent) {
        updateOutput("onAdComplete(\"" + adCompleteEvent.getTag() + "\")");
        print("onAdComplete(\"" + adCompleteEvent.getTag() + "\")");
    }

    @Override
    public void onAdSkipped(AdSkippedEvent adSkippedEvent) {
        updateOutput("onAdSkipped(\"" + adSkippedEvent.getTag() + "\")");
        print("onAdSkipped(\"" + adSkippedEvent.getTag() + "\")");
    }

    @Override
    public void onAdImpression(AdImpressionEvent adImpressionEvent) {
        updateOutput("onAdImpression(\"" + adImpressionEvent.getTag() + "\", \"" + adImpressionEvent.getCreativeType() + "\", \"" + adImpressionEvent.getAdPosition().name() + "\")");
        print("onAdImpression(\"" + adImpressionEvent.getTag() + "\", \"" + adImpressionEvent.getCreativeType() + "\", \"" + adImpressionEvent.getAdPosition().name() + "\")");

    }

    @Override
    public void onAdTime(AdTimeEvent adTimeEvent) {
        // Do nothing - this fires several times per second
    }

    @Override
    public void onAdPause(AdPauseEvent adPauseEvent) {
        updateOutput("onAdPause(\"" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + "\")");
        print("onAdPause(\"" + adPauseEvent.getTag() + "\", \"" + adPauseEvent.getOldState() + "\")");
    }

    @Override
    public void onAdPlay(AdPlayEvent adPlayEvent) {
        updateOutput("onAdPlay(\"" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + "\")");
        print("onAdPlay(\"" + adPlayEvent.getTag() + "\", \"" + adPlayEvent.getOldState() + "\")");
    }

    @Override
    public void onRelatedClose(RelatedCloseEvent relatedCloseEvent) {
        updateOutput("onRelatedClose(\"" + relatedCloseEvent.getMethod() + "\")");
        print("onRelatedClose(\"" + relatedCloseEvent.getMethod() + "\")");
    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {
        updateOutput("onControls(\"" + controlsEvent.getControls() + "\")");
        print("onControls(\"" + controlsEvent.getControls() + "\")");
    }

    @Override
    public void onDisplayClick() {
        print("onDisplayClick(\"" + "displayClick" + "\")");
        updateOutput("onDisplayClick(\"" + "displayClick" + "\")");
    }

    @Override
    public void onMute(boolean b) {
        updateOutput("onMute(\"" + b + "\")");
        print("onMute(\"" + b + "\")");
    }

    @Override
    public void onRelatedOpen(RelatedOpenEvent relatedOpenEvent) {
        updateOutput("onRelatedOpen(\"" + relatedOpenEvent.getMethod() + "\")");
        print("onRelatedOpen(\"" + relatedOpenEvent.getMethod() + "\")");
    }

    @Override
    public void onRelatedPlay(RelatedPlayEvent relatedPlayEvent) {
        updateOutput("onRelatedPlay(\"" + relatedPlayEvent.getAuto() + "\")");
        print("onRelatedPlay(\"" + relatedPlayEvent.getAuto() + "\")");
    }

    @Override
    public void onSeeked() {
        updateOutput("onSeeked(\"" + "seeked" + "\")");
        print("onSeeked(\"" + "seeked" + "\")");
    }

    @Override
    public void onVisualQuality(VisualQuality visualQuality) {
        updateOutput("onVisualQuality(\"" + visualQuality.getQualityLevel().getLabel() + "\")");
        print("onVisualQuality(\"" + visualQuality.getQualityLevel().getLabel() + "\")");
    }
}
