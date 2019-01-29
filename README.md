#JW Player SDK for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android using SharingPlugin Feature.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
- `git checkout v3_sharingConfig`
-	Open the `AndroidManifest.xml` file and replace ```{YOUR_LICENSE_KEY}``` with your license key

The demo application should now build and run. 

For more information on how to use our SDK refer to our developer guide:
- [https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

	```
      private void setupSharingConfig() {

        List<PlaylistItem> playlistItemList = createPlaylist();

        SharingConfig sharingConfig = new SharingConfig.Builder()
            .heading("Youtube")
            .link("https://www.youtube.com/watch?v=BGtROJeMPeE")
            .build();

        mPlayerView.setup(new PlayerConfig.Builder()
            .playlist(playlistItemList)
            .sharingConfig(sharingConfig)
            .preload(true)
            .autostart(true)
            .build()
        );

      }
```

![text](https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/android/jira/v342/sharingpluginevents/v342-sharing1.png)
![text](https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/android/jira/v342/sharingpluginevents/v342sharing2.png)
