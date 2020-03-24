package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.CustomButtonClickEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        String fficon = "https://s3.amazonaws.com/hyunjoo.success.jwplayer.com/icon/fast-forward-icon.png";
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

        // Remove fast forward button
        // Remove button does not work now, but will be fixed in v3.13 or later
        mPlayerView.addOnCompleteListener(new VideoPlayerEvents.OnCompleteListener() {
            @Override
            public void onComplete(CompleteEvent completeEvent) {
                mPlayerView.removeButton(button_id);
            }
        });

        // Load a media source
        PlaylistItem pi = new PlaylistItem.Builder()
                .file("https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4")
                .title("Big Buck Bunny")
                .description("A video player testing video.")
                .build();

        // Load a media source
        PlaylistItem pi2 = new PlaylistItem.Builder()
                .file("https://content.bitsontherun.com/videos/bkaovAYt-52qL9xLP.mp4")
                .title("Big Buck Bunny")
                .description("A video player testing video.")
                .build();

        mPlayerView.load(new ArrayList<PlaylistItem>(){{add(pi);add(pi2);}});


        // Initialize event listeners
        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
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
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE,
                true);
        super.onConfigurationChanged(newConfig);
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
