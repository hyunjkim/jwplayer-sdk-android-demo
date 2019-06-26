package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class SamplePlaylist {

    /*
     * Create a Playlist Example
     * */
    static List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "https://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "https://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8"
        };

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }
    /*
     * Create a Single PlaylistItem Example
     * */

    static List<PlaylistItem> createSingleItem() {


// Create a list of media sources and add High Definition and Standard Definition variants of the stream
        List<MediaSource> mediaSources = new ArrayList<>();
        mediaSources.add(new MediaSource.Builder()
                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .type(MediaType.HLS)
                .isdefault(false)
                .build());

// Assign the sources to the PlaylistItem
        PlaylistItem item = new PlaylistItem();
        item.setSources(mediaSources);
        List<PlaylistItem> pli = new ArrayList<>();
        pli.add(item);

        return pli;
    }
}