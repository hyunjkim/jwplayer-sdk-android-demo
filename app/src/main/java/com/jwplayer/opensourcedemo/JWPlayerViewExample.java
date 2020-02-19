package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwAdvertising;
import com.longtailvideo.jwplayer.freewheel.media.ads.FwSettings;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;

    private CallbackScreen mCallbackScreen;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

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

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
    }

    /**
     * Enable FreeWheel
     * For more info:
     * {@link - https://developer.jwplayer.com/jwplayer/docs/android-enable-freewheel-ad-manager#section-add-a-pre-roll-ad-to-a-playlist}
     */
    private void setupFreeWheel() {
        int networkId = 42015;
        String serverId = "http://7cee0.v.fwmrm.net/";
        String profileId = "fw_tutorial_android";
        String sectionId = "fw_tutorial_android";
        String mediaId = "fw_simple_tutorial_asset";

        // FreeWheel Settings
        FwSettings fwSettings = new FwSettings(networkId, serverId, profileId, sectionId, mediaId);

        // Ad Schedule
        List<AdBreak> adSchedule = new ArrayList<>();

        String[] tags = {
                "fw_preroll",
                "fw-midroll",
                "fw-postroll"};

        int offset = 0;

        String[] offsets = {"pre","mid","post"};

        // Ad breaks
        for (int each = 0; each < tags.length; each++){

            AdBreak adBreak = new AdBreak.Builder()
                    .tag(tags[each])
                    .offset(offsets[each])
                    .source(AdSource.FW)
                    .build();
            adSchedule.add(adBreak);
        }

        // Fw Advertising setup
        FwAdvertising fwadvertising = new FwAdvertising(fwSettings, adSchedule);

        // PlaylistItem + FreeWheel Ads
        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .freewheelSettings(fwSettings)
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(playlistItem);

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(fwadvertising)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton on the JW Player SDK
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        return true;
    }
}
