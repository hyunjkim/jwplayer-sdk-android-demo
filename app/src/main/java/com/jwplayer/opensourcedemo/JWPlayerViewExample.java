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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.LogoConfig;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
     * Reference to the {@link CastManager}
     */
    private CastManager mCastManager;

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

        setupJWPlayerPlaylistItemExample();
        setupJWPlayerMediaSourceExample();

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        mEventHandler = new JWEventHandler(mPlayerView, outputTextView);

        // Get a reference to the CastManager
        mCastManager = CastManager.getInstance();
    }


    /*
    * @link - JW PLAYER MEDIA API: https://developer.jwplayer.com/jw-platform/docs/delivery-api-reference/#/Media
    * */
    private void setupJWPlayerPlaylistItemExample() {

        String mediaId = "jumBvHdL";
        String url = "https://cdn.jwplayer.com/v2/media/" + mediaId;

        Thread t = new Thread(() -> {

            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    byte[] response = Util.executePost(url);
                    String jsonResponse = new String(response, StandardCharsets.UTF_8);

                    // Get the Playlist
                    JSONArray responseObject = new JSONObject(jsonResponse) // set to pojo
                            .getJSONArray("playlist");  // Get the Playlist Object

                    List<PlaylistItem> videoList = PlaylistItem.listFromJson(responseObject);

                    PlayerConfig playerConfig = new PlayerConfig.Builder()
                            .playlist(videoList)
                            .allowCrossProtocolRedirects(true)
                            .mute(false)
                            .preload(true)
                            .autostart(true)
                            .build();

                    // Setup your player with the config
                    mPlayerView.setup(playerConfig);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Get Message: " + e.getMessage());
            }

        });
        t.start();

    }
    private void setupJWPlayerMediaSourceExample() {

        String mediaId = "jumBvHdL";
        String url = "https://cdn.jwplayer.com/v2/media/" + mediaId;

        // Create video
        PlaylistItem video = new PlaylistItem();

        // Create a list of media sources and add High Definition and Standard Definition variants of the stream
        List<MediaSource> mediaSources = new ArrayList<>();

        Thread t = new Thread(() -> {

            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    byte[] response = Util.executePost(url);
                    String jsonResponse = new String(response, StandardCharsets.UTF_8);

                    // Get the Playlist
                    Object responseObject = new JSONObject(jsonResponse) // set to pojo
                            .getJSONArray("playlist")  // Get the Playlist Object
                            .get(0);

                    // Get the Sources
                    JSONObject playlistObject = new JSONObject(responseObject.toString());
                    Object sources = playlistObject.get("sources");
                    String image = playlistObject.getString("image");
                    JSONArray jsonArray = new JSONArray(sources.toString());


                    for (int i = 0; i < jsonArray.length(); i++) {

                        print("\r\n\r\nJSONArray index: " + i + ") " + jsonArray.get(i) + "\r\n\r\n");

                        String parse = jsonArray.getString(i);

                        if (!jsonArray.optString(i).contains("apple.mpegurl")) {
                            MediaSource ms = MediaSource.parseJson(parse);
                            mediaSources.add(ms);
                        }
                    }
                    video.setImage(image);
                    video.setSources(mediaSources);

                    List<PlaylistItem> videoList = new ArrayList<>();
                    videoList.add(video);

                    PlayerConfig playerConfig = new PlayerConfig.Builder()
                            .playlist(videoList)
                            .allowCrossProtocolRedirects(true)
                            .mute(false)
                            .preload(true)
                            .autostart(true)
                            .build();

                    // Setup your player with the config
                    mPlayerView.setup(playerConfig);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
//                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Message: " + e.getMessage());
            }

        });
        t.start();

    }

    private void print(String s) {
        Log.d("JWPLAYEREVENTHANDLER", s);
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
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
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
