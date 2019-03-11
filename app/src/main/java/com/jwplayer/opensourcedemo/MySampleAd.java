package com.jwplayer.opensourcedemo;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;

import java.util.ArrayList;
import java.util.List;

class MySampleAd {

    private static String adtag = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";

    /*
     * VAST Ad Setup Example
     * @link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/ads/Advertising.html
     * */

    static Advertising getVastAd() {
        List<AdBreak> adbreaklist = new ArrayList<>();

        AdBreak adbreak = new AdBreak("pre", AdSource.VAST, adtag);
        adbreaklist.add(adbreak);

        // For more info: https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/ads/AdRules.Builder.html
//		AdRules adRules = new AdRules.Builder()
//				.frequency(1)
//				.startOn(0)
//				.startOnSeek(AdRules.RULES_START_ON_SEEK_PRE)
//				.timeBetweenAds(2)
//				.build();

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
     * IMA Ad Setup Example
     * @link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/ads/ImaAdvertising.html
     * */
    static ImaAdvertising getImaAd() {
        List<AdBreak> adbreaklist = new ArrayList<>();

        AdBreak adBreak = new AdBreak("pre", AdSource.IMA, adtag);
        adbreaklist.add(adBreak);

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
}
