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
                "https://cdn.jwplayer.com/manifests/3HIGGMiI.m3u8",
                "https://cdn.jwplayer.com/manifests/DHfpVnJB.m3u8",
                "https://cdn.jwplayer.com/manifests/o89p07oM.m3u8"
        };
        String[] images = {
                "3HIGGMiI",
                "DHfpVnJB",
                "o89p07oM",
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

}
