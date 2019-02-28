package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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


	private static StringBuilder outputStringBuilder;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwview);

		outputStringBuilder = new StringBuilder();

		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

		// Setup JWPlayer
		setupJWPlayer();

		// Handle hiding/showing of ActionBar
		mPlayerView.addOnFullscreenListener(this);

		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		outputTextView.setText(outputStringBuilder.append("Build version: ").append(mPlayerView.getVersionCode()).append("\r\n"));

		// Instantiate the JW Player event handler class
		new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Instantiate the JW Player Ad event handler class
		new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
	}

	public static String getOutput(String output){
		DateFormat dateFormat = new SimpleDateFormat("KK:mm:ss.SSS", Locale.US);
		return outputStringBuilder.append("").append(dateFormat.format(new Date())).append(" ").append(output).append("\r\n").toString();
	}

	private void setupJWPlayer() {

		List<PlaylistItem> playlistItemList = createMediaSourcePlaylist();

		PlayerConfig config = new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.autostart(true)
				.preload(true)
				.allowCrossProtocolRedirects(true)
				.build();

		mPlayerView.setup(config);
	}

	/*
	 * MediaSource Playlist Example
	 * */
	private List<PlaylistItem> createMediaSourcePlaylist() {
		List<MediaSource> mediaSourceList = new ArrayList<>();
		List<PlaylistItem> playlistItemList = new ArrayList<>();

		String mp3 = "http://content.bitsontherun.com/videos/3XnJSIm4-zWfluNSa.mp3";

		MediaSource ms = new MediaSource.Builder()
				.file(mp3)
				.type(MediaType.MP3)
				.build();
		mediaSourceList.add(ms);

		PlaylistItem item = new PlaylistItem.Builder()
				.sources(mediaSourceList)
				.image("https://cdn.jwplayer.com/v2/media/jumBvHdL/poster.jpg")
				.build();

		playlistItemList.add(item);

		return playlistItemList;
	}

	/*
	 * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Set fullscreen when the device is rotated to landscape
//		mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false);
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
