package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class JWPlayerViewExample extends AppCompatActivity{

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
		ScrollView scrollView = findViewById(R.id.scroll);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);


		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		// Instantiate the JW Player event handler class
		mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Setup JWPlayer
		setupJWPlayer();

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
	}


	private void setupJWPlayer() {
		List<PlaylistItem> playlistItemList = createPlaylist();

		SkinConfig skins = new SkinConfig.Builder()
				.name("hiding-fullscreen-button")
				.url("https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/css/hiding-fullscreen-button.css")
				.build();

		mPlayerView.setup(new PlayerConfig.Builder()
					.playlist(playlistItemList)
					.preload(true)
					.skinConfig(skins)
					.build()
				);
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
		mPlayerView.setFullscreen(false, false);
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
