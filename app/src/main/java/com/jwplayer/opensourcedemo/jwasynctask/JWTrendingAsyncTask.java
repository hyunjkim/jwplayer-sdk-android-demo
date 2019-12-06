package com.jwplayer.opensourcedemo.jwasynctask;

import android.os.AsyncTask;

import com.jwplayer.opensourcedemo.jwutil.Util;
import com.jwplayer.opensourcedemo.listeners.CallBackListener;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.io.IOException;
import java.util.List;

import static com.jwplayer.opensourcedemo.samples.SamplePlaylist.getDefaultPlaylist;

public class JWTrendingAsyncTask extends AsyncTask<String, String, List<PlaylistItem>> {

    private static final String DEFAULT_PLAYLIST_ID = "wHd7WY80";
    private static final String JW_HOST_API = "https://cdn.jwplayer.com/v2/playlists/";
    private CallBackListener mListener;

    public JWTrendingAsyncTask(CallBackListener listener) {
        super();
        mListener = listener;
    }

    @Override
    protected List<PlaylistItem> doInBackground(String... strings) {
        if (!isCancelled()) {

            String playlistid = strings[0];

            if (playlistid.length() <= 0) {
                playlistid = DEFAULT_PLAYLIST_ID;
            }

            String trending = JW_HOST_API + playlistid;

            byte[] response = new byte[0];

            try {

                response = Util.executePost(trending);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.length <= 0) {
                return getDefaultPlaylist();
            }
            String strResponse = new String(response);
            return PlaylistItem.listFromJson(strResponse);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        mListener.onShowProgressBar("Trend");
    }

    @Override
    protected void onPostExecute(List<PlaylistItem> playlistItems) {
        super.onPostExecute(playlistItems);
        SamplePlaylist.setPlaylist("Trend", playlistItems);
        mListener.onHideProgressBar("Trend");
    }

}

