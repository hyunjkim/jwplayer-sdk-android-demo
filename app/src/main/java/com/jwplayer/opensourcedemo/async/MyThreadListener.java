package com.jwplayer.opensourcedemo.async;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public interface MyThreadListener {
    void playlistUpdated(List<PlaylistItem> playlistItems);
    void showProgressBar(String value);
}
