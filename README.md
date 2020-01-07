# [UNOFFICIAL] JW Player SDK for Android Open Source Demo

This application contains an example implementation of the JW Player SDK for Android.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key

The demo application should now build and run. 

For more information on how to use our SDK refer to our developer guide:

[How to Enable FreeWheel Ad Manager with JWPlayerView](https://developer.jwplayer.com/jwplayer/docs/android-enable-freewheel-ad-manager#section-add-a-pre-roll-ad-to-a-playlist)

```    private void setupFreeWheel(){
        int networkId = 42015;
        String serverId = "http://7cee0.v.fwmrm.net/";
        String profileId = "fw_tutorial_android";
        String sectionId = "fw_tutorial_android";
        String mediaId = "fw_simple_tutorial_asset";
        FwSettings settings = new FwSettings(networkId, serverId, profileId, sectionId, mediaId);

        List<AdBreak> adSchedule = new ArrayList<>();

        AdBreak adBreak = new AdBreak.Builder()
                .tag("fw_preroll")
                .source(AdSource.FW)
                .build();

        adSchedule.add(adBreak);

        FwAdvertising advertising = new FwAdvertising(settings, adSchedule);

        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8")
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(playlistItem);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(advertising)
                .build();

        mPlayerView.setup(config);
    }
    
