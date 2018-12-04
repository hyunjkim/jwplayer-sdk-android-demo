package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class MyCustomPlaylist {

    public static List<PlaylistItem> myCustomPlaylist(){

        List<PlaylistItem> playlistItemList = new ArrayList<>();
        WidevineMediaDrmCallback widevineMediaDrmCallback = new WidevineMediaDrmCallback();

        String[] drmPlaylist = {
                "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd",
                "https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd",
                "https://d2jl6e4h8300i8.cloudfront.net/drm/dash/netflix_meridian/mbr/stream.mpd",
        };

        String[] nonDRM = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
        };

        for(int i = 0; i < drmPlaylist.length; i++){
            if(i==0) {
                widevineMediaDrmCallback = new WidevineMediaDrmCallback("d286538032258a1c", "widevine_test");
            } else widevineMediaDrmCallback = new WidevineMediaDrmCallback();
            playlistItemList.add(new PlaylistItem.Builder()
                    .sources(buildMediaSource(drmPlaylist[i],MediaType.MPD))
                    .mediaDrmCallback(widevineMediaDrmCallback)
                    .build());
            if (i!=drmPlaylist.length-1) playlistItemList.add(new PlaylistItem(nonDRM[i])); // Add this for every other DRM content, to test if the content still works when it is mixed
        }
        return playlistItemList;
    }

    private static List<MediaSource> buildMediaSource(String file, MediaType type) {

        List<MediaSource> mediaSourceList = new ArrayList<>();

        MediaSource ms = new MediaSource.Builder()
                .file(file)
                .type(type)
                .build();

        mediaSourceList.add(ms);

        return mediaSourceList;
    }

}
