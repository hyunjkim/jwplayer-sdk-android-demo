# [UNOFFICIAL] JW Player SDK for Android Open Source Demo

## ONLY JWPlayerView and Picture-in-Picture Demo

This application contains an example implementation of the JW Player SDK for Android.
Many credits to Google Example: https://github.com/googlearchive/android-PictureInPicture

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	Open the AndroidManifest.xml file and replace {YOUR_LICENSE_KEY} with your license key
- The demo application should now build and run. 

#### For more information on how to use our SDK refer to our developer guide:

[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

### Google's Demo:
- [Demo Github Project](https://github.com/googlearchive/android-PictureInPicture/blob/master/app/src/main/java/com/example/android/pictureinpicture/MediaSessionPlaybackActivity.java)
### Needs fix:
1) Add media session: [Best practice](https://developer.android.com/guide/topics/ui/picture-in-picture#best)
2) Handle playback resume: [onResume](https://developer.android.com/guide/topics/ui/picture-in-picture#continuing_playback)
3) Add Previous and Next button: [next-playlist-item](https://developer.android.com/guide/topics/ui/picture-in-picture#java)


![](https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/android/github/v310-picture-in-picture.gif)
