package com.jwplayer.opensourcedemo.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.jwplayer.opensourcedemo.util.Util;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MediaIdAsyncTask extends AsyncTask<String, String, List<PlaylistItem>> {

    private MyThreadListener threadListener;

    public MediaIdAsyncTask(MyThreadListener threadListener) {
        super();
        this.threadListener = threadListener;
    }

    @Override
    protected List<PlaylistItem> doInBackground(String... strings) {

        print("PlaylistItem MediaID : " + strings[0]);

        String json = "https://cdn.jwplayer.com/v2/media/" + strings[0] + "?format=json";

        try {
            byte[] response = Util.executePost(json);
            String strResponse = new String(response);

            JSONArray jsonArray = new JSONObject(strResponse).getJSONArray("playlist");
            print(jsonArray.toString());
            SamplePlaylist.mPlaylist = PlaylistItem.listFromJson(jsonArray);
            return PlaylistItem.listFromJson(jsonArray);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
            print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
            print("ERROR CATCH Get Message: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        threadListener.beforeExecute();
    }

    @Override
    protected void onPostExecute(List<PlaylistItem> playlistItem) {
        super.onPostExecute(playlistItem);
        print("onPostExecute - setupJWPlayer");
        threadListener.clear();
        threadListener.setupJWPlayer();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        print("onProgressUpdate - countdown: " + values[0]);
        threadListener.countDown(values[0]);
    }

    @Override
    protected void onCancelled(List<PlaylistItem> playlistItem) {
        super.onCancelled(playlistItem);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private void print(String s) {
        Log.i("SAMPLEPLAYLIST", " - HYUNJOO - " + s + "\r\n");
    }
}
