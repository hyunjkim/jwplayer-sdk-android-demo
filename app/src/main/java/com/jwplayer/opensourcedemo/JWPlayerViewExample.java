package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jwplayer.opensourcedemo.handlers.JWAdEventHandler;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.pojo.JWPlayerResponse;
import com.jwplayer.opensourcedemo.pojo.Schedule;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdRules;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.AdvertisingBase;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

	private String client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jwplayerview);

		mPlayerView = findViewById(R.id.jwplayer);
		TextView outputTextView = findViewById(R.id.output);
		ScrollView scrollView = findViewById(R.id.scroll);
		mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

		print("onCreate() getJSONAdvertising");

		getJSONAdvertising();

		Handler handler = new Handler(getMainLooper());

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				print("onCreate() - setupJWPlayer");
				// Setup JWPlayer
				setupJWPlayer();
			}
		};
		handler.postDelayed(runnable,2000);

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
	}


	/*
	* Setup JWPlayer
	* */
	private void setupJWPlayer() {

//		List<PlaylistItem> playlistItemList = createMediaSourcePlaylist();
		List<PlaylistItem> playlistItemList = createPlaylist();

		print("client 2) "+ client);

		AdvertisingBase advertising = client.contains("ima")? getImaAd(): getVastAd();

		SkinConfig skin = new SkinConfig.Builder()
				.url("https://www.imhostinthis.com/css/mycustom.css")
				.name("mycustom")
				.build();

		PlayerConfig playerConfigBuilder = new PlayerConfig.Builder()
				.playlist(playlistItemList)
				.autostart(true)
				.advertising(advertising) // Ima or Vast Ad Example
				.build();

		mPlayerView.setup(playerConfigBuilder);
	}

	/*
	* Credits to: https://stackoverflow.com/questions/15282181/how-do-i-convert-a-jsonobject-to-class-object
	* */
	private void getJSONAdvertising(){

		Thread t = new Thread(() -> {

			String json = "https://cdn.jwplayer.com/v2/advertising/schedules/L0JeALpT.json";

			try {
				byte[] response = Util.executePost(json);
				String strResponse = new String(response);

				JsonParser parser = new JsonParser();
				JsonElement mJson =  parser.parse(strResponse);

				Gson gson = new Gson();
				JWPlayerResponse jwPlayerResponse = gson.fromJson(mJson, JWPlayerResponse.class);

				client = jwPlayerResponse.getClient();

				print("client 1) "+ client);

				if(client != null){
					if(client.equals("ima")){
						getImaAd();
					} else {
						getVastAd();
					}
				}
				print("Schedule size: " + jwPlayerResponse.getSchedule().size());

				for (Schedule s : jwPlayerResponse.getSchedule()){
					print("Schedule: " + s.getTag());
				}

			} catch (IOException e) {
				e.printStackTrace();
				print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
				print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
				print("ERROR CATCH Get Message: " + e.getMessage());
			}
		});
		t.start();
	}

	/*
	 * Vast Setup Example
	 * */

	private Advertising getVastAd(){
		List<AdBreak> adbreaklist = new ArrayList<>();

		String ad = "";
		String vpaid = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinearvpaid2js&correlator=";

		AdBreak adbreak = new AdBreak("pre", AdSource.VAST, ad);
		adbreaklist.add(adbreak);

//		AdRules adRules = new AdRules.Builder()
//				.frequency(1)
//				.startOn(0)
//				.startOnSeek(AdRules.RULES_START_ON_SEEK_PRE)
//				.timeBetweenAds(2)
//				.build();

		Advertising vastad = new Advertising(AdSource.VAST, adbreaklist);
		vastad.setVpaidControls(true);
//		vastad.setAdRules(adRules);
//		vastad.setClient(AdSource.VAST);
//		vastad.setRequestTimeout(2);
//		vastad.setSkipOffset(1);
//		vastad.setAdMessage("");
//		vastad.setCueText("");
//		vastad.setSkipMessage("");
//		vastad.setSkipText("");

		return vastad;
	}

	/*
	* IMA Ad Example
	* */
	private ImaAdvertising getImaAd(){
		List<AdBreak> adbreaklist = new ArrayList<>();

		String ad = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/152134561/dugout/test&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&url=__page-url__&description_url=__domain__&correlator=123&cust_params=videoplayer%3Djwplayer&vpod=1&max_ad_duration=181000&min_ad_duration=0&sarid=1087561&sf=2&sfu=vid&kfa=0&tfcd=0";

		AdBreak adBreak = new AdBreak("pre", AdSource.IMA, ad);

		adbreaklist.add(adBreak);

		ImaSdkSettings imaSettings = ImaSdkFactory.getInstance().createImaSdkSettings();
//		imaSettings.setRestrictToCustomPlayer(true);
//		imaSettings.setPpid("");
//		imaSettings.setPlayerVersion("");
//		imaSettings.setPlayerType("");
//		imaSettings.setMaxRedirects(1);
//		imaSettings.setLanguage("");
//		imaSettings.setEnableOmidExperimentally(true);
//		imaSettings.setDebugMode(true);
//		imaSettings.setAutoPlayAdBreaks(true);

		AdRules adRules = new AdRules.Builder()
				.frequency(1)
				.startOn(0)
				.startOnSeek(AdRules.RULES_START_ON_SEEK_PRE)
				.timeBetweenAds(2)
				.build();


		ImaAdvertising imaAdvertising = new ImaAdvertising(adbreaklist,imaSettings);
		imaAdvertising.setClient(AdSource.IMA);
		imaAdvertising.setRequestTimeout(1);
		imaAdvertising.setVpaidControls(true);

		return imaAdvertising;
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
				"http://content.jwplatform.com/videos/i3q4gcBi-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/iLwfYW2S-cIp6U8lV.mp4",
				"http://content.jwplatform.com/videos/8TbJTFy5-cIp6U8lV.mp4",
		};

		for(String each : playlist){
			playlistItemList.add(new PlaylistItem(each));
		}

		return playlistItemList;
	}

	/**
	 * MediaSource Playlist Example
	 * */
	private List<PlaylistItem> createMediaSourcePlaylist() {
		List<MediaSource> mediaSourceList = new ArrayList<>();
		List<PlaylistItem> playlistItemList = new ArrayList<>();

		String hls = "https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8";

		MediaSource ms = new MediaSource.Builder()
				.file(hls)
				.type(MediaType.HLS)
				.build();
		mediaSourceList.add(ms);

		PlaylistItem item = new PlaylistItem.Builder()
				.sources(mediaSourceList)
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


	/*
	* Upper right corner, drop down menu XML
	* */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);
		// Register the MediaRouterButton on the JW Player SDK
		mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
		return true;
	}

	/*
	* The items to add to the list on the drop down menu
	* */
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



	private void print(String s){
		Log.i("HYUNJOO", "JSON OBJECT RESPONSE: " + s);
	}
}