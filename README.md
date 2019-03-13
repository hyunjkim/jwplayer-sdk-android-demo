# JW Player SDK for Android Open Source Demo


[![Join the chat at https://gitter.im/jwplayer/jwplayer-sdk-android-demo](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jwplayer/jwplayer-sdk-android-demo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This application contains an example implementation of the JW Player SDK for Android.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	Open the AndroidManifest.xml file and replace {YOUR_LICENSE_KEY} with your license key

The demo application should now build and run. For more information on how to use our SDK refer to our developer guide:

[https://developer.jwplayer.com/sdk/android/docs/developer-guide/](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)


## [PlaylistItem](https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/playlists/PlaylistItem.html)

There are two parsejson method:

- [parseJson(org.json.JSONObject json)](https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/playlists/PlaylistItem.html#parseJson-org.json.JSONObject-)
- [parseJson(java.lang.String json)](https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/playlists/PlaylistItem.html#parseJson-java.lang.String-)


## [List<PlaylistItem>]()

There are two listFromJson method:

- [listFromJson(org.json.JSONArray json)](https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/playlists/PlaylistItem.html#listFromJson-org.json.JSONArray-)
- [listFromJson(java.lang.String json)](https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/media/playlists/PlaylistItem.html#listFromJson-java.lang.String-)

#### Code Example, I use listFromJson(org.json.JSONArray json) API Method: 
```                   
                    String mediaId = "jumBvHdL";
                    String url = "https://cdn.jwplayer.com/v2/media/" + mediaId;
                    
                    byte[] response = Util.executePost(url);
                    String jsonResponse = new String(response, StandardCharsets.UTF_8);

                    // Get the Playlist
                    JSONArray responseObject = new JSONObject(jsonResponse) // Set to pojo
                            .getJSONArray("playlist");  // Get the Playlist Object

                    List<PlaylistItem> videoList = PlaylistItem.listFromJson(responseObject);

                    PlayerConfig playerConfig = new PlayerConfig.Builder()
                            .playlist(videoList)
                            .autostart(true)
                            .build();

                    // Setup your player with the config
                    mPlayerView.setup(playerConfig);```
