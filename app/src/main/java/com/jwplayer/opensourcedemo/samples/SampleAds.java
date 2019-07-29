package com.jwplayer.opensourcedemo.samples;

import android.util.Log;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.util.Util;
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

public class SampleAds {

    public static String client;
    public static JSONArray schedule;
    public static JSONObject adRules;
    public static List<AdBreak> adbreaklist;

    /*
     * Vast Ad Setup Example
     * */

    public Advertising getVastAd() {
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
    public ImaAdvertising getImaAd() {

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


    private void print(String s) {
        Log.i("SAMPLEADS", "JSON OBJECT RESPONSE: " + s + "\r\n");
    }

    public String getClient() {
        return client;
    }
}
