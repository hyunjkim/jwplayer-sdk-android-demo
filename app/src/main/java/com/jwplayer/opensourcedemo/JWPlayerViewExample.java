package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.listeners.JWCastHandler;
import com.jwplayer.opensourcedemo.listeners.JWEventHandler;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.captions.Caption;
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
	private MediaRouteButton mChromecastbtn;
	private JWCastHandler castHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);
		mChromecastbtn = findViewById(R.id.chromecast_btn);
		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);


		// Handle hiding/showing of ActionBar
		mPlayerView.addOnFullscreenListener(this);

		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		// Instantiate the JW Player event handler class
		mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);


		// Setup JWPlayer
		setupJWPlayerPlaylistItem();
//		setupJWPlayerPlayConfigWithEmptyCaptions();

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
		mCastManager.addMediaRouterButton(mChromecastbtn);
		castHandler = new JWCastHandler(mChromecastbtn);
		mCastManager.addDeviceListener(castHandler);
		mCastManager.addConnectionListener(castHandler);
		mPlayerView.addOnControlBarVisibilityListener(castHandler);

	}


	private void setupJWPlayerPlaylistItem() {
		String captionVideo = "http://cdnbakmi.kaltura.com/p/243342/sp/24334200/playManifest/entryId/0_uka1msg4/flavorIds/1_vqhfu6uy,1_80sohj7p/format/applehttp/protocol/http/a.m3u8";

		PlaylistItem video = new PlaylistItem.Builder()
				.file(captionVideo)
				.build();

		mPlayerView.load(video);
	}

	private void setupJWPlayerPlayConfigWithEmptyCaptions() {
		String captionVideo = "http://cdnbakmi.kaltura.com/p/243342/sp/24334200/playManifest/entryId/0_uka1msg4/flavorIds/1_vqhfu6uy,1_80sohj7p/format/applehttp/protocol/http/a.m3u8";

		PlaylistItem video = new PlaylistItem.Builder()
				.file(captionVideo)
				.build();

		List<Caption> captionTracks = new ArrayList<>();
		Caption emptyCaps = new Caption("file_en.srt");
		captionTracks.add(emptyCaps);
		video.setCaptions(captionTracks);

		List<PlaylistItem> item = new ArrayList<>();
		item.add(video);
		mPlayerView.setup(new PlayerConfig.Builder()
		.playlist(item)
		.build());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Set fullscreen when the device is rotated to landscape
		mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
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
		mCastManager.removeDeviceListener(castHandler);
		mCastManager.removeConnectionListener(castHandler);
		mPlayerView.removeOnControlBarVisibilityListener(castHandler);
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
		super.onCreateOptionsMenu(menu);
		// Inflate the menu
		getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
//		mCastManager.addMediaRouterButton(menu,R.id.media_route_menu_item);
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
