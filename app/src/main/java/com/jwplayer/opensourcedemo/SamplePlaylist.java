package com.jwplayer.opensourcedemo;

import android.util.Log;

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
                "https://cdn.jwplayer.com/manifests/RDn7eg0o.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "https://cdn.jwplayer.com/manifests/8TbJTFy5.m3u8",
                "https://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4"
        };
        String[] images = {
                "RDn7eg0o",
                "tkM1zvBq",
                "8TbJTFy5",
                "i3q4gcBi",
                "iLwfYW2S"
        };

        int index = 0;

        for (String each : playlist) {

            String[] array;

            if (each.endsWith(".m3u8")) {
                array = each.split("/manifests/");
            } else {
                array = each.split("/videos/");
            }

            PlaylistItem item = new PlaylistItem.Builder()
                    .file(each)
                    .image("https://cdn.jwplayer.com/v2/media/"+images[index]+"/poster.jpg")
                    .title(array[1])
                    .build();

            index+=1;

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

        String hls = "https://cdn.jwplayer.com/manifests/RDn7eg0o.m3u8";
        String mp4 = "https://cdn.jwplayer.com/videos/RDn7eg0o-Zq6530MP.mp4";
        String mp4_1 = "https://cdn.jwplayer.com/videos/RDn7eg0o-TNpruJId.mp4";
        String mp4_2 = "https://cdn.jwplayer.com/videos/RDn7eg0o-FctPAkow.mp4";

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
