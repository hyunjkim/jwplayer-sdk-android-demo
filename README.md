# JW Player Android SDK (version 3+) - VAST & IMA Example

This application contains an example implementation of the JW Player SDK for Android.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	git checkout v3_ads
- Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key

The demo application should now build and run. 

For more information on how to use our SDK refer to our developer guide:
[JW Android Support Documentation](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

#### Setup JWPlayer Example:
```
	private void setupJWPlayer() {

		List<PlaylistItem> playlistItemList = createPlaylist();

		SkinConfig skin = new SkinConfig.Builder()
				.url("https://www.imhostinthis.com/css/mycustom.css")
				.name("mycustom")
				.build();

		Advertising ad = getVastAd();
// 		ImaAdvertising ad = getImaAd();

		PlayerConfig playerConfigBuilder = new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.autostart(true)
				.advertising(ad)  // Add your Ad here 
				.build();

		mPlayerView.setup(playerConfigBuilder);
	}
```

#### Vast Setup Example:
```
	private Advertising getVastAd(){
		List<AdBreak> adbreaklist = new ArrayList<>();

		String ad = "";

		AdBreak adbreak = new AdBreak("pre", AdSource.VAST, ad);
		adbreaklist.add(adbreak);

		AdRules adRules = new AdRules.Builder()
				.frequency(1)
				.startOn(0)
				.startOnSeek(AdRules.RULES_START_ON_SEEK_PRE)
				.timeBetweenAds(2)
				.build();

		Advertising vastad = new Advertising(AdSource.VAST, adbreaklist);
		vastad.setVpaidControls(true);
		vastad.setAdRules(adRules);
		vastad.setClient(AdSource.VAST);
		vastad.setRequestTimeout(2);
		vastad.setSkipOffset(1);
		vastad.setAdMessage("");
		vastad.setCueText("");
		vastad.setSkipMessage("");
		vastad.setSkipText("");

		return vastad;
	}
```

#### IMA Setup Example:
```
	private ImaAdvertising getImaAd(){
		List<AdBreak> adbreaklist = new ArrayList<>();

		String ad = "";

		AdBreak adBreak = new AdBreak("pre", AdSource.IMA, ad);

		adbreaklist.add(adBreak);

		ImaSdkSettings imaSettings = ImaSdkFactory.getInstance().createImaSdkSettings();
		imaSettings.setRestrictToCustomPlayer(true);
		imaSettings.setPpid("");
		imaSettings.setPlayerVersion("");
		imaSettings.setPlayerType("");
		imaSettings.setMaxRedirects(1);
		imaSettings.setLanguage("");
		imaSettings.setEnableOmidExperimentally(true);
		imaSettings.setDebugMode(true);
		imaSettings.setAutoPlayAdBreaks(true);

		return new ImaAdvertising(adbreaklist,imaSettings);
	}
```
