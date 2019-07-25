package com.jwplayer.opensourcedemo.sample;

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
                "https://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    /**
     * Sample 1 - MediaSource Playlist Example
     */
    public static List<PlaylistItem> createMediaSourcePlaylist() {
        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String hls = "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8";

        MediaSource ms = new MediaSource.Builder()
                .file(hls)
                .type(MediaType.HLS)
                .build();
        mediaSourceList.add(ms);

        PlaylistItem item = new PlaylistItem.Builder()
                .sources(mediaSourceList)
                .build();

        playlistItemList.add(item);

        return playlistItemList;
    }


    /**
     * Sample 2 - MediaSource Playlist Example
     */
    public static List<PlaylistItem> getMediaSourceExample() {
        final String[] mp4 = {
                "Zq6530MP",
                "TNpruJId",
                "cIp6U8lV"};

        final List<MediaSource> mediaSourceList = new ArrayList<MediaSource>() {{
            add(new MediaSource.Builder().file(String.format("https://cdn.jwplayer.com/videos/jumBvHdL-%s.mp4", mp4[0])).label("320px").build());
            add(new MediaSource.Builder().file(String.format("https://cdn.jwplayer.com/videos/jumBvHdL-%s.mp4", mp4[1])).label("4820px").build());
            add(new MediaSource.Builder().file(String.format("https://cdn.jwplayer.com/videos/jumBvHdL-%s.mp4", mp4[2])).label("720px").build());
        }};
        return new ArrayList<PlaylistItem>() {{
            add(new PlaylistItem.Builder()
                    .sources(mediaSourceList)
                    .build());

        }};
    }

}