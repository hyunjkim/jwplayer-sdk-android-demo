package com.jwplayer.opensourcedemo.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SampleAds;
import com.jwplayer.opensourcedemo.util.Util;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class AdvertisingAsyncTask extends AsyncTask<String, String, Advertising> {

    private MyThreadListener threadListener;

    public AdvertisingAsyncTask(MyThreadListener threadListener) {
        super();
        this.threadListener = threadListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        threadListener.beforeExecute();
    }

    @Override
    protected void onPostExecute(Advertising advertising) {
        super.onPostExecute(advertising);
        print("onPostExecute - setupJWPlayer");
        threadListener.clear();
        threadListener.setupJWPlayer();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        print("onProgressUpdate - countdown: " + values[0]);
        threadListener.countDown(values[0]);
    }

    @Override
    protected void onCancelled(Advertising advertising) {
        super.onCancelled(advertising);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Advertising doInBackground(String... strings) {

        print("Advertusubg Advertise ID : " + strings[0]);

        String json = "https://cdn.jwplayer.com/v2/advertising/schedules/" + strings[0] + ".json";

        try {
            byte[] response = Util.executePost(json);
            String strResponse = new String(response);

            JSONObject jsonObject = new JSONObject(strResponse);
            print(jsonObject.toString());

            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {

                String key = keys.next();

                print("Each key: " + key);

                switch (key) {
                    case "schedule":
                        SampleAds.schedule = new JSONArray(jsonObject.getJSONArray("schedule").toString());
                        break;
                    case "rules":
                        SampleAds.adRules = new JSONObject(jsonObject.getJSONObject("rules").toString());
                        break;
                    case "client":
                        SampleAds.client = jsonObject.getString("client");
                        break;
                    case "adscheduleid":
                        print("Each key: " + strings[0]);
                        break;
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
            print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
            print("ERROR CATCH Get Message: " + e.getMessage());
        }
        return null;
    }

    private void print(String s) {
        Log.i("SAMPLEADVERTISING", " - HYUNJOO - " + s + "\r\n");
    }
}
