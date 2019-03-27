package com.jwplayer.opensourcedemo;

import android.util.Log;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdRules;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class SampleAds {

    static String client;
    private static boolean isFinished = true;
    private static JSONArray schedule;
    private static JSONObject adRules;
    private static List<AdBreak> adbreaklist;
    private static MyThreadListener mListener;

    SampleAds(MyThreadListener listener) {
        mListener = listener;
    }

    static void getJSONAdvertising(String adscheduleid) {

        Thread setupjwplayer = new Thread(() -> {
            mListener.setupJWPlayer();
        });

        Thread t = new Thread(() -> {
            String json = "https://cdn.jwplayer.com/v2/advertising/schedules/" + adscheduleid + ".json";

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
                            schedule = new JSONArray(jsonObject.getJSONArray("schedule").toString());
                            break;
                        case "rules":
                            adRules = new JSONObject(jsonObject.getJSONObject("rules").toString());
                            break;
                        case "client":
                            client = jsonObject.getString("client");
                            break;
                        case "adscheduleid":
                            print("Each key: " + adscheduleid);
                            break;
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
                print("ERROR CATCH Get Message: " + e.getMessage());
            }
        });

        t.start();

        while (t.isAlive()) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setupjwplayer.start();
        }
        try {
            setupjwplayer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Vast Ad Setup Example
     * */

    static Advertising getVastAd() {
        adbreaklist = new ArrayList<>();

        for (int i = 0; i < schedule.length(); i++) {
            AdBreak adBreak = null;
            try {
                adBreak = AdBreak.parseJson(schedule.getJSONObject(i));
                print("AdBreak: " + adBreak.toJson().toString());
                adbreaklist.add(adBreak);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Advertising vastad = new Advertising(AdSource.VAST, adbreaklist);
        vastad.setVpaidControls(true);
        AdRules mAdRules = null;

        try {
            mAdRules = AdRules.parseJson(adRules);
            vastad.setAdRules(mAdRules);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vastad;
    }

    /*
     * IMA Ad Setup Example
     * */
    static ImaAdvertising getImaAd() {

        adbreaklist = new ArrayList<>();

        for (int i = 0; i < schedule.length(); i++) {
            AdBreak adBreak = null;
            try {
                adBreak = AdBreak.parseJson(schedule.getJSONObject(i));
                print("AdBreak: " + adBreak.toJson().toString());
                adbreaklist.add(adBreak);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ImaSdkSettings imaSettings = ImaSdkFactory.getInstance().createImaSdkSettings();
//		imaSettings.setRestrictToCustomPlayer(true);
//		imaSettings.setPpid("");
//		imaSettings.setPlayerVersion("");
//		imaSettings.setPlayerType("");
//		imaSettings.setMaxRedirects(1);
//		imaSettings.setLanguage("");
//		imaSettings.setEnableOmidExperimentally(true);
//		imaSettings.setDebugMode(true);
//		imaSettings.setAutoPlayAdBreaks(true);

        ImaAdvertising imaAdvertising = new ImaAdvertising(adbreaklist, imaSettings);
        imaAdvertising.setClient(AdSource.IMA);

        AdRules mAdRules = null;

        try {
            mAdRules = AdRules.parseJson(adRules);
            imaAdvertising.setAdRules(mAdRules);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imaAdvertising;
    }


    private static void print(String s) {
        Log.i("SAMPLEADS", "JSON OBJECT RESPONSE: " + s);
    }
}
