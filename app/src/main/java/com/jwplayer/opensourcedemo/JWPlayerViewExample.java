package com.jwplayer.opensourcedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwAdvertising;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwSettings;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity{

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

        // Setup FreeWheel Ad
        setupFreeWheel();
    }

    /** Enable FreeWheel
     * For more info:
     * {@link - https://developer.jwplayer.com/jwplayer/docs/android-enable-freewheel-ad-manager#section-add-a-pre-roll-ad-to-a-playlist}
     * */
    private void setupFreeWheel(){
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
