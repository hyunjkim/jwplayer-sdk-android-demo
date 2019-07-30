package com.jwplayer.opensourcedemo.samples;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.AdvertisingBase;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.ads.ImaVMAPAdvertising;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;

import java.util.ArrayList;
import java.util.List;

public class SampleAds {
    /**
     * Vast Setup Example
     *
     * @see Advertising
     */

    public static Advertising getVastAd() {
        List<AdBreak> adbreaklist = new ArrayList<>();

        String ad = "";
        String vpaid = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinearvpaid2js&correlator=";

        AdBreak adbreak = new AdBreak("pre", AdSource.VAST, vpaid);
        adbreaklist.add(adbreak);

//		AdRules adRules = new AdRules.Builder()
//				.frequency(1)
//				.startOn(0)
//				.startOnSeek(AdRules.RULES_START_ON_SEEK_PRE)
//				.timeBetweenAds(2)
//				.build();

        Advertising vastad = new Advertising(AdSource.VAST, adbreaklist);
		vastad.setVpaidControls(true);
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

    /**
     * VAST VMAP Ad Example
     * {@link - https://developer.jwplayer.com/sdk/android/docs/developer-guide/advertising/vast/#vmap-advertising}
     *
     * @see VMAPAdvertising
     */
    public static VMAPAdvertising vastVMAP() {
        return new VMAPAdvertising(AdSource.VAST, "https://playertest.longtailvideo.com/adtags/vmap2.xml");
    }

    /**
     * IMA Ad Example
     * {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/ads/ImaAdvertising.html}
     *
     * @see ImaAdvertising
     */
    public static ImaAdvertising getImaAd() {

        List<AdBreak> adbreaklist = new ArrayList<>();

        String ad1 = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";
        String ad = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=";

        AdBreak adBreak = new AdBreak("pre", AdSource.IMA, ad);
        adbreaklist.add(adBreak);

        ImaSdkSettings imaSettings = ImaSdkFactory.getInstance().createImaSdkSettings();
//		imaSettings.setRestrictToCustomPlayer(true);
//		imaSettings.setPpid("");
//		imaSettings.setPlayerVersion("");
//		imaSettings.setPlayerType("");
//		imaSettings.setMaxRedirects(1);
//		imaSettings.setLanguage("");
        imaSettings.setEnableOmidExperimentally(true);
//		imaSettings.setDebugMode(true);
		imaSettings.setAutoPlayAdBreaks(true);

        return new ImaAdvertising(adbreaklist, imaSettings);
    }

    /**
     * IMA VMAP Ad Example
     * {@link - https://developer.jwplayer.com/sdk/android/docs/developer-guide/advertising/google-ima/#vmap-advertising}
     */
    public static ImaVMAPAdvertising imaVMAP() {
        String pre = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=";

        return new ImaVMAPAdvertising(pre);
    }

    public static VMAPAdvertising getVMAP(String client) {
        return client.equals("ima") ? imaVMAP() : vastVMAP();
    }

    public static AdvertisingBase getAd(String client) {
        return client.equals("ima") ? getImaAd() : getVastAd();
    }
}
