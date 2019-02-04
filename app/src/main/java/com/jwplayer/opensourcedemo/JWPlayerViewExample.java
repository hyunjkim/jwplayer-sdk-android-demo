package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.handler.JWEventHandler;
import com.jwplayer.opensourcedemo.handler.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.handler.MyCastHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.mediarouter.app.MediaRouteButton;

public class JWPlayerViewExample extends AppCompatActivity{

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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

		hideActionBar();

		// Setup JWPlayer
		setupJWPlayerPlaylistItem();
//		setupJWPlayerMediaSourceFile();

		// Keep the screen on during playback
		new KeepScreenOnHandler(mPlayerView, getWindow());

		// Instantiate the JW Player event handler class
		new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();

		addChromeCastButton();
	}

	private void addChromeCastButton() {
		MediaRouteButton chromecastBtn = findViewById(R.id.chromecast_btn);
		mCastManager.addMediaRouterButton(chromecastBtn);
		MyCastHandler mCastHandler = new MyCastHandler(chromecastBtn,mPlayerView);
		mCastManager.addConnectionListener(mCastHandler);
		mCastManager.addDeviceListener(mCastHandler);
		mPlayerView.addOnControlBarVisibilityListener(mCastHandler);
	}

	private void hideActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
		mCoordinatorLayout.setFitsSystemWindows(false);
	}

	// Sample of a video with inline captions
	private void setupJWPlayerPlaylistItem() {

		String captionVideo = "http://cdnbakmi.kaltura.com/p/243342/sp/24334200/playManifest/entryId/0_uka1msg4/flavorIds/1_vqhfu6uy,1_80sohj7p/format/applehttp/protocol/http/a.m3u8";

		PlaylistItem video = new PlaylistItem.Builder()
				.file(captionVideo)
				.build();

		mPlayerView.load(video);
	}

	private void setupJWPlayerMediaSourceFile() {
		List<MediaSource> mediaSourceList = new ArrayList<>();

		String hls = "";

		MediaSource ms = new MediaSource.Builder()
				.file(hls)
				.type(MediaType.HLS)
				.build();
		mediaSourceList.add(ms);

		PlaylistItem item = new PlaylistItem.Builder()
				.sources(mediaSourceList)
				.build();

		mPlayerView.load(item);
	}

	private void setupJWPlayerPlayConfigWithCaptions() {
		String captionVideo = "videofile.mp4";

		// Build a playlistitem
		PlaylistItem video = new PlaylistItem.Builder()
				.file(captionVideo)
				.build();

		// Add captions here
		List<Caption> captionTracks = new ArrayList<>();
		Caption caption1 = new Caption("file_en.srt");
		captionTracks.add(caption1);

		// Set the caption to the video
		video.setCaptions(captionTracks);

		// Add the playlist item with the caption to a Playlist
		List<PlaylistItem> item = new ArrayList<>();
		item.add(video);

		// Pass the playlist to the player config
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
