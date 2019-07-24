package com.jwplayer.opensourcedemo.samples;

import android.util.Log;

import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.util.Util;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SamplePlaylist {

    private static List<PlaylistItem> mPlaylist;
    private static Thread thread;
    private MyThreadListener mListener;

    public SamplePlaylist(MyThreadListener listener) {
        mListener = listener;
    }

    public static List<PlaylistItem> getPlaylist() {
        return mPlaylist;
    }

    /*
     * Create a Playlist Example
     * */
    public static List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
                "https://contenthread.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
                "http://contenthread.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "https://contenthread.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
                "https://contenthread.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
                "https://contenthread.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
                "https://playertesthread.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
        };

        for (String each : playlist) {
            PlaylistItem item = new PlaylistItem(each);
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    /**
     * MediaSource Playlist Example
     */
    public static List<PlaylistItem> createMediaSourcePlaylist() {
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

    public static void stopThreads() {
        if (thread != null) thread = null;
    }

    public void getJSONPlaylist(String playlistid) {

        thread = new Thread(() -> {
            String json = "https://cdn.jwplayer.com/v2/playlists/" + playlistid + "?format=json";

            try {
                byte[] response = Util.executePost(json);
                String strResponse = new String(response);

                JSONArray jsonArray = new JSONObject(strResponse).getJSONArray("playlist");
                print(jsonArray.toString());
                mPlaylist = PlaylistItem.listFromJson(jsonArray);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
                print("ERROR CATCH Get Message: " + e.getMessage());
            }
        });

        thread.start();

        while (thread.isAlive()) {
            try {
                print("Sample playlist - Thread join() ");
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        print("Sample playlist - setupjwplayer start() ");
        mListener.setupJWPlayer();
    }

    public void getJSONPlaylistItem(String mediaId) {

        thread = new Thread(() -> {
            String json = "https://cdn.jwplayer.com/v2/media/" + mediaId + "?format=json";

            try {
                byte[] response = Util.executePost(json);
                String strResponse = new String(response);

                JSONArray jsonArray = new JSONObject(strResponse).getJSONArray("playlist");
                print(jsonArray.toString());
                mPlaylist = PlaylistItem.listFromJson(jsonArray);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
                print("ERROR CATCH Get Message: " + e.getMessage());
            }
        });

        thread.start();
        while (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mListener.setupJWPlayer();
    }

    private void print(String s) {
        Log.i("SAMPLEPLAYLIST", "JSON OBJECT RESPONSE: " + s + "\r\n");
    }
}