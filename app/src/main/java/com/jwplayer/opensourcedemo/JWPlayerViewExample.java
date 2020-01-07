package com.jwplayer.opensourcedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.CaptionsConfig;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;

    private CallbackScreen mCallbackScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Event Logging
        mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);

        // Setup WebVTT
        setupWebVTT();
    }

    /**
     * Setup WebVTT Example with JWPlayerView
     * <p>
     * For more info: {@link - https://developer.jwplayer.com/jwplayer/docs/android-add-captions}
     */

    public void setupWebVTT() {

        //  customize the font, font color, window color, background color, and edge style of the captions in your app
        CaptionsConfig captionsConfig = new CaptionsConfig.Builder()
                .fontFamily("zapfino")
                .fontSize(12)
                .fontOpacity(100)
                .color("#FFFFFF")
                .edgeStyle(CaptionsConfig.CAPTION_EDGE_STYLE_RAISED)
                .windowColor("#000000")
                .windowOpacity(50)
                .backgroundColor("#000000")
                .backgroundOpacity(100)
                .build();


        // To add videos or streams with embedded captions to your app
        List<Caption> captionTracks = new ArrayList<>();

        Caption captionEn = new Caption.Builder()
                .file("https://cdn.jwplayer.com/manifests/{MEDIA_ID}.vtt")
                .label("English")
                .isdefault(true)
                .build();
        captionTracks.add(captionEn);

        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file("https://cdn.jwplayer.com/manifests/{MEDIA_ID}.m3u8")
                .title("WebVTT Example")
                .tracks(captionTracks)
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(playlistItem);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlist)
                .captionsConfig(captionsConfig)
                .build();
        mPlayerView.setup(config);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
}
