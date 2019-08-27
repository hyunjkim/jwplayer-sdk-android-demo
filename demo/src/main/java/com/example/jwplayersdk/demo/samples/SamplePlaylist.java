package com.example.jwplayersdk.demo.samples;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class SamplePlaylist {
    private static List<PlaylistItem> playlistItemList = new ArrayList<>();
    /*
     * Create a Playlist Example
     * */
    public static List<PlaylistItem> createPlaylist() {

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };
        String[] titles = {
                "Alaskan Dogs",
                "Surfer Dudes",
                "Beach Waves",
                "Cyclist",
                "Happiness",
                "Climbing a mountain",
                "Beep Beep Bip-Bop"
        };

        int index = 0;

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            item.setTitle(titles[index]);
            index += 1;
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    public static String getPlaylistMediaFile(int position) {
        return playlistItemList.get(position).getFile();
    }
}
