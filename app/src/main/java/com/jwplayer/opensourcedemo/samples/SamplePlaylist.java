package com.jwplayer.opensourcedemo.samples;

import com.jwplayer.opensourcedemo.drm.WidevineMediaDrmCallback;
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
                "https://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };

        // DRM Content Added First
        PlaylistItem drm = new PlaylistItem.Builder()
                .file("https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd")
                .mediaDrmCallback(new WidevineMediaDrmCallback())
                .build();

        playlistItemList.add(drm);

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

}
