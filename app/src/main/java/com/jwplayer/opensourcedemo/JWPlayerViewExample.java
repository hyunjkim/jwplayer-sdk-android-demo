package com.jwplayer.opensourcedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerViewExample extends AppCompatActivity implements
		VideoPlayerEvents.OnFullscreenListener {

	/**
	 * Reference to the {@link JWPlayerView}
	 */
	private JWPlayerView mPlayerView;

	/**
	 * Reference to the {@link CastManager}
	 */
	private CastManager mCastManager;

	/**
	 * Stored instance of CoordinatorLayout
	 * http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html
	 */
	private CoordinatorLayout mCoordinatorLayout;

	private MediaRouteButton mCastButton;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
		mCastButton = findViewById(R.id.custom_castBtn);

		// Handle hiding/showing of ActionBar
		mPlayerView.addOnFullscreenListener(this);

		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		// Instantiate the JW Player event handler class
		//new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Setup JWPlayer
		setupJWPlayer();

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
		mCastManager.addMediaRouterButton(mCastButton);

		CastListener castListener = new CastListener(mPlayerView, mCastManager, mCastButton);
		mCastManager.addConnectionListener(castListener);
	}

	private void setupJWPlayer() {
		List<PlaylistItem> playlistItemList = createPlaylist();

		PlayerConfig config = new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.autostart(true)
				.preload(true)
				.build();

		mPlayerView.setup(config);
	}

	private List<PlaylistItem> createPlaylist() {
		List<PlaylistItem> playlistItemList = new ArrayList<>();

		String[] playlist = {
				"https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
				"http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
				"http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8"
				};

		for(String each : playlist){
			playlistItemList.add(new PlaylistItem(each));
		}

		return playlistItemList;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Set fullscreen when the device is rotated to landscape
		boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
		mPlayerView.setFullscreen(isLandscape, true);
		super.onConfigurationChanged(newConfig);
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
				LinearLayout.LayoutParams toFullscreen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
				mPlayerView.setLayoutParams(toFullscreen);
			} else {
				actionBar.show();
				LinearLayout.LayoutParams toMinimize = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
				mPlayerView.setLayoutParams(toMinimize);
			}
		}
		// When going to Fullscreen we want to set fitsSystemWindows="false"
		mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
		// Register the MediaRouterButton on the JW Player SDK
//		mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
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
