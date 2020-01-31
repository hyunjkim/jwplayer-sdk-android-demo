package com.jwplayer.opensourcedemo;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;


public class JWPlayerViewExample extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        // Set orientation to Portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPlayerView = findViewById(R.id.jwplayer);

        // Display JWPlayer Alert Dialog
        findViewById(R.id.dialog_btn).setOnClickListener(view -> JWDialog());

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // My Custom Fullscreen Handler
        mPlayerView.setFullscreenHandler(new MyFullscreenHandler(mPlayerView));

        // Load PlaylistItem
        mPlayerView.load(getPlaylistItem());

        // Initialize event listeners
        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
    }

    private PlaylistItem getPlaylistItem() {
        // Load a media source
        return new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .title("BipBop")
                .description("A video player testing video.")
                .build();
    }

    /*
     * JW Player Dialog Example
     * */
    public void JWDialog() {

        if (mPlayerView != null) {

            // Pause the Player
            mPlayerView.pause();

            // Alert Dialog suggest to inflate the view or constructed
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.dialog_jwplayer, null);

            // Get a new JWPlayerView
            JWPlayerView dialogPlayer = layout.findViewById(R.id.dialog_jwplayer_view);

            // My Custom Fullscreen Handler
            dialogPlayer.setFullscreenHandler(new MyFullscreenHandler(dialogPlayer));

            // Create Alert Dialog
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(layout)
                    .setOnDismissListener(dialogInterface -> {

                        dialogPlayer.onDestroy(); // destroy the JWPlayerView

                        if (mPlayerView != null) { // Play the previous JWPlayerView
                            mPlayerView.play();
                        }
                    })
                    .setCancelable(true)
                    .create();

            // Load PlaylistItem
            dialogPlayer.load(getPlaylistItem());

            // Show the Dialog
            dialog.show();
        }
    }

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
