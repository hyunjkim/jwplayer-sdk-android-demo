package com.jwplayer.opensourcedemo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class JWPlayerViewExample extends AppCompatActivity {
    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    private String skin = "";
    private String skinName = "";
    private EditText skinURL;
    private boolean useDefaultSkinConfig = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        // Enable WebView debugging
        // Open your Chrome browser > Remote devices > Dev Tools
        // This is helpful to test your CSS Skin
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mPlayerView = findViewById(R.id.jwplayer);
        skinURL = findViewById(R.id.skinurl);

        // Setup JWPlayer
        setupJWPlayer();

    }

    /**
     * Setup JW Player
     * SkinConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SkinConfig.Builder.html
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

    public void enter(View v) {

        // Get the Skin URL
        skin = skinURL.getText().toString();

        if (skin.length() > 0) {

            // Split the URL by "/"
            String[] urlSplit = skin.split("/");

            try {

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
            } catch (Exception e) {
                Toast.makeText(this, "invalid CSS", Toast.LENGTH_SHORT).show();
                Log.d("JWPLAYER_ERROR", "invalid CSS");
                setDefaultSkinConfig();
            }

        }


        // Setup JWPlayer
        setupJWPlayer();

        // clear the skin URL
        skinURL.setText("");
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
}
