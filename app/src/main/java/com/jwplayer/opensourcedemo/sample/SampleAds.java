package com.jwplayer.opensourcedemo.sample;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.longtailvideo.jwplayer.media.ads.Ad;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.ads.ImaVMAPAdvertising;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;

import java.util.ArrayList;
import java.util.List;

public class SampleAds {
    /*
     * Vast Setup Example
     * */

    public static Advertising getVastAd() {
        List<AdBreak> adbreaklist = new ArrayList<>();

        String vpaid = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinearvpaid2js&correlator=";

        String vast = "https://playertest.longtailvideo.com/vast-30s-ad.xml";

        Ad ad = new Ad(AdSource.VAST,vpaid);
        AdBreak adbreak = new AdBreak("pre", ad);
        adbreaklist.add(adbreak);
        Advertising vastad = new Advertising(AdSource.VAST, adbreaklist);
//		vastad.setVpaidControls(true);
//		vastad.setAdRules(adRules);
//		vastad.setClient(AdSource.VAST);
//		vastad.setRequestTimeout(2);
//		vastad.setSkipOffset(1);
//		vastad.setAdMessage("");
//		vastad.setCueText("");
//		vastad.setSkipMessage("");
//		vastad.setSkipText("");

        return vastad;
    }

    /*
     * VAST VMAP Ad Example
     * {For more info: https://developer.jwplayer.com/sdk/android/docs/developer-guide/advertising/vast/#vmap-advertising}
     * */
    public static VMAPAdvertising vastVMAP() {
        return new VMAPAdvertising(AdSource.VAST, "https://playertest.longtailvideo.com/adtags/vmap2.xml");
    }

    /*
     * IMA Ad Example
     * */
    public static ImaAdvertising getImaAd() {
        List<AdBreak> adbreaklist = new ArrayList<>();

        String ima = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";
        Ad ad = new Ad(AdSource.IMA,ima);
        AdBreak adbreak = new AdBreak("pre", ad);
        adbreaklist.add(adbreak);

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

        return new ImaAdvertising(adbreaklist, imaSettings);
    }

    /*
     * IMA VMAP Ad Example
     * {For more info: https://developer.jwplayer.com/sdk/android/docs/developer-guide/advertising/google-ima/#vmap-advertising}
     * */
    public static ImaVMAPAdvertising imaVMAP() {
        String pre = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=";

        return new ImaVMAPAdvertising(pre);
    }

    public static VMAPAdvertising getVMAP(String client) {
        return client.equals("ima") ? imaVMAP() : vastVMAP();
    }
}
