package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class JWPlayerViewExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener,
        View.OnClickListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;
    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;
    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;
    private String skin = "";
    private String skinName = "";
    private EditText skinURL;
    private boolean useDefaultSkinConfig = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        mPlayerView = findViewById(R.id.jwplayer);
        skinURL = findViewById(R.id.skinurl);
        findViewById(R.id.enterbtn).setOnClickListener(this);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);

        // Enable WebView debugging
        // Open your Chrome browser > Remote devices > Dev Tools
        // This is helpful to test your CSS Skin
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);


        // Setup JWPlayer
        setupJWPlayer();


        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

    }

    /**
     * Setup JW Player
     * <p>
     * 1 - PlayerConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlayerConfig.Builder.html
     * 2 - LogoConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/LogoConfig.html
     * 3 - PlaybackRateConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlaybackRateConfig.html
     * 4 - CaptionsConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/CaptionsConfig.html
     * 5 - RelatedConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/RelatedConfig.html
     * 6 - SharingConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SharingConfig.html
     * 7 - SkinConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SkinConfig.Builder.html
     * <p>
     * More info about our Player Configuration and other available Configurations:
     * {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/package-summary.html}
     */
    private void setupJWPlayer() {

        List<PlaylistItem> playlistItemList = SamplePlaylist.createPlaylist();

        // Even if I have CSS URL, but I also checked if it's an actualy CSS file by checking if there is a name
        if (skin.length() < 1 || useDefaultSkinConfig) {
            setDefaultSkinConfig();
        }

        SkinConfig skinConfig = new SkinConfig.Builder()
                .url(skin)
                .name(skinName)
                .build();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .skinConfig(skinConfig)
                .build();

        mPlayerView.setup(config);

        // Reset the Skin details to empty
        resetSkinConfig();
    }

    /*
     * Reset the Skin Config
     * */
    private void resetSkinConfig() {
        useDefaultSkinConfig = false;
        skin = "";
        skinName = "";
    }

    /*
     * Default Skin Config
     * */
    private void setDefaultSkinConfig() {
        skin = "https://ssl.p.jwpcdn.com/player/v/7.2.3/skins/bekle.css";
        skinName = "bekle";
    }

    @Override
    public void onClick(View v) {

        // Get the Skin URL
        skin = skinURL.getText().toString();

        if (skin.length() > 0) {

            // Split the URL by "/"
            String[] urlSplit = skin.split("/");

            if (urlSplit.length > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // Java8, filter for word that contains ".css"

                    skinName = Arrays.stream(urlSplit)
                            .filter(eachWord -> eachWord.endsWith(".css"))
                            .collect(Collectors.joining());

                    skinName = skinName.substring(0, skinName.length() - 4); // remove the ".css"

                } else { // Java7, check from the end of the array for a word with ".css"
                    int arrayEnd = urlSplit.length;
                    while (arrayEnd >= 0) {
                        if (urlSplit[arrayEnd].endsWith(".css")) {

                            String name = urlSplit[arrayEnd];
                            skinName = name.substring(0, name.length() - 4); // remove the ".css"
                            arrayEnd = 0;
                        }
                        arrayEnd -= 1;
                    }
                }

                if (skinName.length() < 1) {
                    // If I can't get the CSS Skin name, then I assume, this is not a Skin URL and I will use my default skin
                    useDefaultSkinConfig = true;
                }

            }
        }


        // Setup JWPlayer
        setupJWPlayer();

        // clear the skin URL
        skinURL.setText("");
    }


    /*
//     * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mPlayerView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        mPlayerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreenEvent true if the player is fullscreen
     */
    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton on the JW Player SDK
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_fragment:
                Intent i = new Intent(this, JWPlayerFragmentExample.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
