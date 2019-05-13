package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class SamplePlaylist {

    /*
     * Create a Playlist Example
     * */
    public static List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    /**
     * MediaSource Playlist Example
     * <p>
     * Adaptive Streaming for more info: https://support.jwplayer.com/articles/adaptive-streaming-reference
     */
    public static List<PlaylistItem> createMediaSourcePlaylist() {
        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String hls = "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8";
        String mp4 = "https://cdn.jwplayer.com/videos/jumBvHdL-Zq6530MP.mp4";
        String mp4_1 = "https://cdn.jwplayer.com/videos/jumBvHdL-TNpruJId.mp4";
        String mp4_2 = "https://cdn.jwplayer.com/videos/jumBvHdL-FctPAkow.mp4";

        MediaSource mshls = new MediaSource.Builder()
                .file(hls)
                .label("HLS")
                .type(MediaType.HLS)
                .build();
        MediaSource ms = new MediaSource.Builder()
                .file(mp4)
                .label("180px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms1 = new MediaSource.Builder()
                .file(mp4_1)
                .label("480px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms2 = new MediaSource.Builder()
                .file(mp4_2)
                .label("1280px")
                .type(MediaType.MP4)
                .build();

        mediaSourceList.add(ms);
        mediaSourceList.add(ms1);
        mediaSourceList.add(ms2);
        mediaSourceList.add(mshls);

        PlaylistItem item = new PlaylistItem.Builder()
                .sources(mediaSourceList)
                .build();

        playlistItemList.add(item);

        return playlistItemList;
    }

}
