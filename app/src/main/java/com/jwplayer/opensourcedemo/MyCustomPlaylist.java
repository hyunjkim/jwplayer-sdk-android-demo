package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

class MyCustomPlaylist {

    static List<PlaylistItem> myCustomPlaylist(){

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
                    .sources(buildMediaSource(drmPlaylist[i], MediaType.MPD))
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

    /*
     * Create a Playlist Example
     * */
    private List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
        };

        for(String each : playlist){
            playlistItemList.add(new PlaylistItem(each));
        }

        return playlistItemList;
    }

    /**
     * MediaSource Playlist Example
     * */
    static List<PlaylistItem> createMediaSourcePlaylist() {
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


}
