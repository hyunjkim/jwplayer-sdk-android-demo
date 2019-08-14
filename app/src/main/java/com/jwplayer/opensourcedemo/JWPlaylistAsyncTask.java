package com.jwplayer.opensourcedemo;

import android.os.AsyncTask;
import android.util.Log;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JWPlaylistAsyncTask extends AsyncTask<String, String, List<PlaylistItem>> {

    private MyThreadListener mThreadListener;

    JWPlaylistAsyncTask(MyThreadListener passThreadListener) {
        super();
        this.mThreadListener = passThreadListener;
    }

    @Override
    protected List<PlaylistItem> doInBackground(String... strings) {
        if (!isCancelled()) {

            print("PlaylistItem Playlist ID : " + strings[0]);

            String json = "https://cdn.jwplayer.com/v2/playlists/" + strings[0] + "?format=json";

            try {
                byte[] response = new byte[0];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    response = Util.executePost(json);
                }
                String strResponse = new String(response);

                JSONArray jsonArray = new JSONObject(strResponse).getJSONArray("playlist");

                return PlaylistItem.listFromJson(jsonArray);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
                print("ERROR CATCH Get Message: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<PlaylistItem> playlistItems) {
        super.onPostExecute(playlistItems);
        mThreadListener.playlistUpdated(playlistItems);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        mThreadListener.showProgressBar(values[0]);
    }

    @Override
    protected void onCancelled(List<PlaylistItem> playlistItems) {
        super.onCancelled(playlistItems);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mThreadListener = null;
        print("onCancelled - threadlistener = null");
    }

    private void print(String output) {
        Log.i("HYUNJOO", " - ASYNCTASK - " + output + "\r\n");
    }
}
