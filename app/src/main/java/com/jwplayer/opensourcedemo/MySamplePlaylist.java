package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

class MySamplePlaylist {

    /**
     * MediaSource Playlist Example
     */
    static List<PlaylistItem> createMediaSourcePlaylist() {
        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/3yknMpYB.m3u8",
                "https://content.jwplatform.com/videos/3yknMpYB-XpQlVcLm.mp4",
                "https://content.jwplatform.com/videos/3yknMpYB-oxD7pWs8.mp4"
        };

        for(String each : playlist){

            MediaType type = MediaType.MP4;

            if(each.contains("m3u8")){
                type = MediaType.HLS;
            }

            MediaSource mediaSource = new MediaSource.Builder()
                    .file(each)
                    .label(each.substring(each.length()-12))
                    .type(type)
                    .build();

            mediaSourceList.add(mediaSource);

        }

        PlaylistItem item = new PlaylistItem.Builder()
                .sources(mediaSourceList)
                .build();

        playlistItemList.add(item);

        return playlistItemList;
    }

}
