package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.LogoConfig;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;


public class JWPlayerViewExample extends AppCompatActivity
        implements VideoPlayerEvents.OnFullscreenListener {

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * An instance of our event handling class
     */
    private JWEventHandler mEventHandler;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Stored instance of CoordinatorLayout
     * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
     */
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
        Button logoToggle = findViewById(R.id.logotogglebtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        LogoConfig logoConfig = new LogoConfig.Builder()
                .file("https://cdn.jwplayer.com/v2/media/jumBvHdL/poster.jpg")
                .build();

        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(SamplePlaylist.createPlaylist())
                .logoConfig(logoConfig)
                .stretching(PlayerConfig.STRETCHING_EXACT_FIT)
                .build();

        mPlayerView.setup(config);

        logoToggle.setOnClickListener(v -> {
            LogoConfig logo = mPlayerView.getConfig().getLogoConfig();
            if (logoToggle.getText().equals("HIDE LOGO")) {
                if (logo != null) {
                    logo.setHide(true);
                    Log.i("JWEVENT", "---------------------- hide logo - " + logo.getHide());
                }
                logoToggle.setText("SHOW LOGO");
            } else {
                if (logo != null) {
                    logo.setHide(false);
                    Log.i("JWEVENT", "++++++++++++++++++++++ show logo - " + logo.getHide());
                }
                logoToggle.setText("HIDE LOGO");
            }
            runOnUiThread(() -> {
                PlayerConfig mConfig = mPlayerView.getConfig();
                mConfig.setLogoConfig(logo);
                Log.i("JWEVENT", "Logo Config: " + logo.getHide());
                double position = mPlayerView.getPosition();;
                boolean resume = false;
                int index = mPlayerView.getPlaylistIndex();
                if (!mPlayerView.getState().equals(PlayerState.COMPLETE)) {
                    resume = true;
                }
                mPlayerView.setup(mConfig);
                mPlayerView.playlistItem(index);
                mPlayerView.seek(position);

                if(resume) mPlayerView.play();
            });
        });

        // Keep the screen on
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);
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

        // Register the MediaRouterButton
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

