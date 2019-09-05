package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

class SamplePlaylist {

    /*
     * Create a Playlist Example
     * */
    static List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "https://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8"
        };
        String[] ids = {
                "alaska",
                "big buck bunny",
                "surf",
                "waves",
                "bicycle",
                "car",
                "hiking",
                "bipbop"

        };
        int id = 0;
        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            item.setMediaId(ids[id]);
            playlistItemList.add(item);
            id++;
        }

        return playlistItemList;
    }

}
