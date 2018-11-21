package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;


public class JWPlayerViewExample extends AppCompatActivity{

	/**
	 * Reference to the {@link JWPlayerView}
	 */
	private JWPlayerView anotherJWView;
	private MyFullScreenHandler myFullScreenHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_test);

		FrameLayout framelayoutContainer = findViewById(R.id.framelayout_container);
		VideoPlayerContainerView videoPlayerContainer = new VideoPlayerContainerView(this);
		framelayoutContainer.addView(videoPlayerContainer);
		anotherJWView = videoPlayerContainer.renderVideoContainer();

		PlayerConfig mPlayerConfig = new PlayerConfig.Builder()
				.file("http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
				.autostart(true)
				.repeat(true)
				.build();
		anotherJWView.setup(mPlayerConfig);

		myFullScreenHandler = new MyFullScreenHandler(anotherJWView,this);
		// Attach Full Screen Listener
//		anotherJWView.addOnFullscreenListener(myFullScreenHandler);
	}

	@Override
	protected void onResume() {
		// Let JW Player know that the app has returned from the background
		super.onResume();
		anotherJWView.onResume();
	}

	@Override
	protected void onPause() {
		// Let JW Player know that the app is going to the background
		anotherJWView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// Let JW Player know that the app is being destroyed
		anotherJWView.onDestroy();
//		anotherJWView.removeOnFullscreenListener(myFullScreenHandler);
		super.onDestroy();
	}

}
