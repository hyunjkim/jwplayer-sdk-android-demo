package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.jwplayer.opensourcedemo.fullscreenhandler.MyFullscreenHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.RelatedConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdRules;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


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
	private LinearLayout mLinearLayout;

	private MyFullscreenHandler fullscreenHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);
		mLinearLayout = findViewById(R.id.jw_linearlayout);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

		// Setup JWPlayer
		setupJWPlayer();

		// Handle hiding/showing of ActionBar
		mPlayerView.addOnFullscreenListener(this);

		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		// Instantiate the JW Player event handler class
		new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Instantiate the JW Player Ad event handler class
		new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();

		// My Fullscreen handler
		fullscreenHandler = new MyFullscreenHandler(this,mPlayerView);

		// JWPlayer fullscreen handler
		mPlayerView.setFullscreenHandler(fullscreenHandler);

		// No auto-fullscreen when I rotate
		fullscreenHandler.onAllowRotationChanged(true);
		fullscreenHandler.setUseFullscreenLayoutFlags(true);
	}


	private void setupJWPlayer() {

		List<PlaylistItem> playlistItemList = createPlaylist();

		PlayerConfig config = new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.autostart(true)
				.mute(true)
				.build();

		mPlayerView.setup(config);
	}

	/*
	 * Create a Playlist Example
	 * */
	private List<PlaylistItem> createPlaylist() {
		List<PlaylistItem> playlistItemList = new ArrayList<>();

		String[] playlist = {
				"https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
				"http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8",
				"http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
		};

		for(String each : playlist){
			playlistItemList.add(new PlaylistItem.Builder()
					.file(each)
					.build());
		}

		return playlistItemList;
	}

	/*
	 * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		int orientation = newConfig.orientation;

		String config = orientation == 1 ? "portrait": "landscape";

		print(" - onConfigurationChanged - " + config);


		fullscreenHandler.setMyFullscreen(mPlayerView.getFullscreen());
//		mPlayerView.setFullscreen(orientation == Configuration.ORIENTATION_LANDSCAPE,false);

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		// Let JW Player know that the app has returned from the background
		super.onResume();
		fullscreenHandler.onResume();
		mPlayerView.onResume();
	}

	@Override
	protected void onPause() {
		// Let JW Player know that the app is going to the background
		fullscreenHandler.onPause();
		mPlayerView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// Let JW Player know that the app is being destroyed
		fullscreenHandler.onDestroy();
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
		boolean isFullscreen = fullscreenEvent.getFullscreen();
		print("onFullscreen() - " + isFullscreen);

		if (actionBar != null) {
			if (isFullscreen) {
				actionBar.hide();
			} else {
				actionBar.show();
			}
		}

		// When going to Fullscreen we want to set fitsSystemWindows="false"
		mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
	}

	public void print(String s){
		Log.i("JWPLAYERVIEWEVENT",s);
	}

}
