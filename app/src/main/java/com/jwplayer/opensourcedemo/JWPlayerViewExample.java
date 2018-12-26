package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

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

		// Instantiate the JW Player event handler class
		mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
	}

	private void setupJWPlayer() {
		List<PlaylistItem> playlistItemList = createPlaylist();

		List<AdBreak> adbreaklist = new ArrayList<>();
		String pre = "https://pubads.g.doubleclick.net/gampad/ads?correlator=http%3A%2F%2Fwww3.stream.co.jp&iu=/28331925/moviead_pre_test&env=vp&gdfp_req=1&output=vast&sz=640x360|640x480&description_url=https%3A%2F%2Fwww.stream.co.jp%2F&tfcd=0&npa=0&vpmute=0&vpa=0&vad_format=linear&url=https%3A%2F%2Fwww.stream.co.jp%2F&vpos=preroll&unviewed_position_start=1";
		String mid = "https://pubads.g.doubleclick.net/gampad/ads?correlator=http%3A%2F%2Fwww3.stream.co.jp&iu=/28331925/moviead_mid_test&env=vp&gdfp_req=1&output=vast&sz=640x360|640x480&description_url=https%3A%2F%2Fwww.stream.co.jp%2F&tfcd=0&npa=0&vpmute=0&vpa=0&vad_format=linear&url=https%3A%2F%2Fwww.stream.co.jp%2F&vpos=midroll&videoad_start_delay=0&unviewed_position_start=1";
		String post = "https://pubads.g.doubleclick.net/gampad/ads?correlator=http%3A%2F%2Fwww3.stream.co.jp&iu=/28331925/moviead_post_test&env=vp&gdfp_req=1&output=vast&sz=640x360|640x480&description_url=https%3A%2F%2Fwww.stream.co.jp%2F&tfcd=0&npa=0&vpmute=0&vpa=0&vad_format=linear&url=https%3A%2F%2Fwww.stream.co.jp%2F&vpos=postroll&unviewed_position_start=1";

		adbreaklist.add(new AdBreak("pre", AdSource.IMA, pre));
		adbreaklist.add(new AdBreak("50%", AdSource.IMA, mid));
		adbreaklist.add(new AdBreak("post", AdSource.IMA, post));

		ImaAdvertising advertise = new ImaAdvertising(adbreaklist);

		mPlayerView.setup(new PlayerConfig.Builder()
					.playlist(playlistItemList)
					.advertising(advertise)
					.autostart(true)
					.preload(true)
					.build()
				);
	}

	private List<PlaylistItem> createPlaylist() {
		List<PlaylistItem> playlistItemList = new ArrayList<>();

		String[] playlist = {
				"https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8",
				"http://content.jwplatform.com/videos/tkM1zvBq-cIp6U8lV.mp4",
//				"http://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
//				"http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
//				"http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
//				"http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
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
