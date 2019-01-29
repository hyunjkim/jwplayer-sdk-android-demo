#JW Player SDK for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android using the RelatedPlugin Feature (Recommendations).

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
- `Git checkout v3_relatedConfig`
-	Open the `AndroidManifest.xml` file and replace {YOUR_LICENSE_KEY} with your license key

The demo application should now build and run. For more information on how to use our SDK refer to our developer guide:

[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)


	private void setupOverlay() {
		List<PlaylistItem> playlistItemList = createPlaylist();

		RelatedConfig relatedConfig = new RelatedConfig.Builder()
				.file("https://myownfeed//feed.rss")
				.displayMode(RELATED_DISPLAY_MODE_OVERLAY)
				.build();

		mPlayerView.setup(new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.relatedConfig(relatedConfig)
				.allowCrossProtocolRedirects(true)
				.preload(true)
				.autostart(true)
				.build()
		);
	}
  ![alt text](https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/android/jira/v342/sharingpluginevents/v342-relatedplugin.png)
