package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class SamplePlaylist {


    /**
     * MediaSource Playlist Example
     * <p>
     * Adaptive Streaming for more info: https://support.jwplayer.com/articles/adaptive-streaming-reference
     */
    public static List<PlaylistItem> createMediaSourcePlaylist() {

        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        MediaSource ms1 = new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/videos/iLwfYW2S-Zq6530MP.mp4")
                .label("180px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms2 = new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/videos/iLwfYW2S-TNpruJId.mp4")
                .label("270px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms3 = new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/videos/iLwfYW2S-cIp6U8lV.mp4")
                .label("406px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms4 = new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/videos/iLwfYW2S-FctPAkow.mp4")
                .label("720px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms5 = new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/videos/iLwfYW2S-8yQ1cYbs.mp4")
                .label("1080px")
                .type(MediaType.MP4)
                .build();

        mediaSourceList.add(ms1);
        mediaSourceList.add(ms2);
        mediaSourceList.add(ms3);
        mediaSourceList.add(ms4);
        mediaSourceList.add(ms5);

        PlaylistItem mp4 = new PlaylistItem.Builder()
                .title("People Laughing in a Car")
                .sources(mediaSourceList)
                .build();

        PlaylistItem hls = new PlaylistItem.Builder()
                .title("Alaska Video")
                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .build();

        playlistItemList.add(hls);
        playlistItemList.add(mp4);

        return playlistItemList;
    }

}
