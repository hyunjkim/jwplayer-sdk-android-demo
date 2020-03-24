# [UNOFFICIAL] JW Player SDK for Android Open Source Demo
This application contains an example implementation of the JW Player SDK for Android.

## Add button
- [VideoPlayerEvents.onCustomButtonClick](https://sdk.jwplayer.com/android/v3/reference/com/longtailvideo/jwplayer/events/listeners/VideoPlayerEvents.OnCustomButtonClickListener.html#onCustomButtonClick-com.longtailvideo.jwplayer.events.CustomButtonClickEvent-)

- [playerInstance.addButton()](https://sdk.jwplayer.com/android/v3/reference/com/longtailvideo/jwplayer/JWPlayerView.html#addButton-java.lang.String-java.lang.String-com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents.OnCustomButtonClickListener-java.lang.String-java.lang.String-)
```
        String fficon = "https://www.host.com/fast-forward-icon.png";
        String button_tooltip = "Fastforward";
        VideoPlayerEvents.OnCustomButtonClickListener buttonClickListener =  new VideoPlayerEvents.OnCustomButtonClickListener() {
            @Override
            public void onCustomButtonClick(CustomButtonClickEvent customButtonClickEvent) {
                mPlayerView.seek(5);
            }
        };
        String button_id = "ff-btn-id";
        String button_class = "ff-btn-class";

        // Fast forward button
        mPlayerView.addButton(
                fficon,
                button_tooltip,
                buttonClickListener,
                button_id,
                button_class
        );
```

## Remove button
[playerInstance.removeButton](https://sdk.jwplayer.com/android/v3/reference/com/longtailvideo/jwplayer/JWPlayerView.html#removeButton-java.lang.String-)

Remove button currently does not work, but it will be released in the future v3.13.0 or later


#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	Open the AndroidManifest.xml file and replace {YOUR_LICENSE_KEY} with your license key
- The demo application should now build and run. 

#### For more information on how to use our SDK refer to our developer guide:

[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)
