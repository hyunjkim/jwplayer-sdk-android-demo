# [UNOFFICIAL] Additional Samples: Using Fragments, AsyncTask, RecyclerView, JW Player SDK for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key

The demo application should now build and run. 
For more information on how to use our SDK refer to our developer guide:

[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

## Single Activity that can add to the back of the Fragment stack of JWPlayer views whenever a user clicks on a video from the  video list

## Credit to: https://medium.com/@harivigneshjayapalan/android-recyclerview-implementing-single-item-click-and-long-press-part-ii-b43ef8cb6ad8

## [MyRecyclerItemTouchListener](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/java/com/jwplayer/opensourcedemo/MyRecyclerItemTouchListener.java)
- Includes an MyClickListener Interface https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/java/com/jwplayer/opensourcedemo/MyRecyclerItemTouchListener.java#L71

## [MyRecyclerAdapter](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/java/com/jwplayer/opensourcedemo/MyRecyclerAdapter.java)

## [VideoDetailFragment](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/java/com/jwplayer/opensourcedemo/VideoDetailFragment.java#L94)
- This is how the VideoDetailFragment is getting the video file whenever a video is clicked on the list
- This is how I pass (https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/java/com/jwplayer/opensourcedemo/MainActivity.java#L138)

## XML Layout
1) [fragment_videodetailfragment.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/res/layout/fragment_videodetailfragment.xml)
- This contains the JWPlayerView and EventListener Text Output
2) [layout_grids.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/res/layout/layout_grids.xml)
- This is for the recycler view grids
3) [layout_mainactivity.xml](https://github.com/hyunjkim/jwplayer-sdk-android-demo/blob/v3_fragments/app/src/main/res/layout/layout_mainactivity.xml)
- This layout contains Fragment Container and RecyclerView

![](https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/android/github/v3_fragment_rv.png)
