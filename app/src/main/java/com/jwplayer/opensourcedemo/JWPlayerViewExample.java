package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.CuePoint;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.ads.interactivemedia.v3.api.StreamDisplayContainer;
import com.google.ads.interactivemedia.v3.api.StreamManager;
import com.google.ads.interactivemedia.v3.api.StreamRequest;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.google.ads.interactivemedia.v3.api.player.VideoStreamPlayer;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.cast.CastManager;
import com.longtailvideo.jwplayer.events.MetaEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity implements
		AdsLoader.AdsLoadedListener,
		AdEvent.AdEventListener,
		AdErrorEvent.AdErrorListener{

	private StreamDisplayContainer mDisplayContainer;

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

		// Setup JWPlayer

		setupJWPlayer();

		mEventHandler = new JWEventHandler(mPlayerView, outputTextView, scrollView);

		// Get a reference to the CastManager
		mCastManager = CastManager.getInstance();
	}
	// Live stream asset key.
	private static final String TEST_ASSET_KEY = "sN_IYUG8STe1ZzhIIE_ksA";

	// VOD content source and video IDs.
	private static final String TEST_CONTENT_SOURCE_ID = "19463";
	private static final String TEST_VIDEO_ID = "googleio-highlights";
	private ImaSdkFactory imaSdkFactory;
	private StreamManager mStreamManager;

	private void setupJWPlayer(){
		mPlayerCallbacks = new ArrayList<>();

		imaSdkFactory = ImaSdkFactory.getInstance();
		mDisplayContainer = imaSdkFactory.createStreamDisplayContainer();

		ImaSdkSettings settings = imaSdkFactory.createImaSdkSettings();
		settings.setEnableOmidExperimentally(true);

		AdsLoader mAdsLoader = imaSdkFactory.createAdsLoader(this);
		mAdsLoader.addAdErrorListener(this);
		mAdsLoader.addAdsLoadedListener(this);
		mAdsLoader.requestStream(buildStreamRequest());

	}


	private StreamRequest buildStreamRequest() {
		VideoStreamPlayer videoStreamPlayer = createVideoStreamPlayer();
		mPlayerView.addOnMetaListener(new VideoPlayerEvents.OnMetaListener() {
										  @Override
										  public void onMeta(MetaEvent metaEvent) {
											  for (VideoStreamPlayer.VideoStreamPlayerCallback callback : mPlayerCallbacks) {
											  		Log.i("DAI GOOGLE",metaEvent.getMetadata().getId3Metadata().toString());
												  callback.onUserTextReceived(metaEvent.getMetadata().getId3Metadata().toString());
											  }
										  }
									  });
		mPlayerView.addOnSeekListener(new VideoPlayerEvents.OnSeekListener() {
			@Override
			public void onSeek(SeekEvent seekEvent) {
				long newSeekPositionMs = (long) seekEvent.getPosition();
				if (mStreamManager != null) {
					CuePoint prevCuePoint = mStreamManager.getPreviousCuePointForStreamTime(mPlayerView.getPosition());
					if (prevCuePoint != null && !prevCuePoint.isPlayed()) {
						Log.i("DAI GOOGLE","previous Cue Point: "+ prevCuePoint);
						Log.i("DAI GOOGLE","old seek position: " +  newSeekPositionMs);
						newSeekPositionMs = (long) (prevCuePoint.getStartTime() * 1000);
						Log.i("DAI GOOGLE","new seek position: " +  newSeekPositionMs);
					}
				}

				Log.i("DAI GOOGLE","let's seek: " +  newSeekPositionMs);
				mPlayerView.seek(newSeekPositionMs);
			}
		});
		mDisplayContainer.setVideoStreamPlayer(videoStreamPlayer);
		mDisplayContainer.setAdContainer(mPlayerView);
		// Live stream request.
		StreamRequest request = imaSdkFactory.createLiveStreamRequest(
				TEST_ASSET_KEY, null, mDisplayContainer);

		// VOD request. Comment the createLiveStreamRequest() line above and uncomment this
		// createVodStreamRequest() below to switch from a live stream to a VOD stream.
		//StreamRequest request = mSdkFactory.createVodStreamRequest(TEST_CONTENT_SOURCE_ID,
		//        TEST_VIDEO_ID, null, mDisplayContainer);
		return request;
	}

	private List<VideoStreamPlayer.VideoStreamPlayerCallback> mPlayerCallbacks;

	private VideoStreamPlayer createVideoStreamPlayer() {
		VideoStreamPlayer player = new VideoStreamPlayer() {
			@Override
			public void loadUrl(String url, List<HashMap<String, String>> subtitles) {
				mPlayerView.load(new PlaylistItem(url));
				mPlayerView.play();
			}

			@Override
			public int getVolume() {
				return 0;
			}

			@Override
			public void addCallback(
					VideoStreamPlayerCallback videoStreamPlayerCallback) {
				mPlayerCallbacks.add(videoStreamPlayerCallback);
			}

			@Override
			public void removeCallback(
					VideoStreamPlayerCallback videoStreamPlayerCallback) {
				mPlayerCallbacks.remove(videoStreamPlayerCallback);
			}

			@Override
			public void onAdBreakStarted() {
				// Disable player controls.
				mPlayerView.setControls(false);
			}

			@Override
			public void onAdBreakEnded() {
				// Re-enable player controls.
				mPlayerView.setControls(true);
			}

			@Override
			public VideoProgressUpdate getContentProgress() {
				return new VideoProgressUpdate((long)mPlayerView.getPosition(),
						(long)mPlayerView.getDuration());
			}
		};
		return player;
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
	public void onAdsManagerLoaded(AdsManagerLoadedEvent event) {
		mStreamManager = event.getStreamManager();
		mStreamManager.addAdErrorListener(this);
		mStreamManager.addAdEventListener(this);
		mStreamManager.init();
	}

	@Override
	public void onAdError(AdErrorEvent adErrorEvent) {
		mPlayerView.setControls(true);
		mPlayerView.next();
		mPlayerView.play();
	}

	@Override
	public void onAdEvent(AdEvent adEvent) {
		switch (adEvent.getType()) {
			case AD_PROGRESS:
				// Do nothing or else log will be filled by these messages.
				break;
			default:
				Log.i("DAI GOOGLE",String.format("Event: %s\n", adEvent.getType()));
				break;
		}
	}

}
