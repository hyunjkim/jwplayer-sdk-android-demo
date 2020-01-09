package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SamplePlaylist {

    public static List<PlaylistItem> mPlaylist = new ArrayList<>();
    public static List<PlaylistItem> getPlaylist() {
        return mPlaylist;
    }
    public static List<PlaylistItem> getDefaultPlaylist() {
        return Collections.singletonList(new PlaylistItem("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8"));
    }
}