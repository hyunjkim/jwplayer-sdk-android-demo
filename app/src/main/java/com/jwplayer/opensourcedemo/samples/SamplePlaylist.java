package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SamplePlaylist {

    private static List<PlaylistItem> mTrendingPlaylist = new ArrayList<>();
    private static List<PlaylistItem> mManualPlaylist = new ArrayList<>();

    private static boolean toast = false;

    /*
     * Default Playlist
     * When this is called, Manual / Trend list  did not return
     * */
    public static List<PlaylistItem> getDefaultPlaylist() {

        // Show a toast that their player ID is invalid
        toast = true;

        // My default video
        PlaylistItem item = new PlaylistItem.Builder()
                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .image("https://cdn.jwplayer.com/v2/media/jumBvHdL/poster.jpg")
                .title("Default Video")
                .build();

        return Collections.singletonList(item);
    }

    /*
     * Get the Playlist
     */
    public static List<PlaylistItem> getPlaylistItem(String name) {
        return name.equals("Manual") ? mManualPlaylist : mTrendingPlaylist;
    }

    /*
     * Set the Playlist
     */
    public static void setPlaylist(String name, List<PlaylistItem> list) {
        if (name.equals("Manual")) {
            mManualPlaylist = list;
        } else {
            mTrendingPlaylist = list;
        }
    }

    /*
     * Show Toast
     * */
    public static boolean makeToast() {
        return toast;
    }

    /*
     * Don't Toast
     * */
    public static void resetToast() {
        toast = false;
    }
}
