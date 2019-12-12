package com.example.jwplayersdk.fastforwardbuttondemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;


public class JWPlayerViewExample extends AppCompatActivity {

    private JWPlayerView mPlayerView;
    private CallbackScreen mCallbackScreen;
    private View.OnTouchListener touchlistener;
    private GestureDetector gestureDetector;
    private MyDoubleTapListener myDoubleTapListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Initialize my double tap listeners
        initializeListeners();

        // Set the on Touch listeners
        findViewById(R.id.rewind_button).setOnTouchListener(touchlistener);
        findViewById(R.id.fastforward_button).setOnTouchListener(touchlistener);
        mPlayerView.getChildAt(0).setOnTouchListener(touchlistener);


        // Event Logging
        mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);

        // Load PlaylistItem
        mPlayerView.load(new PlaylistItem("https://cdn.jwplayer.com/manifests/jumBvHdL.m3u8"));
    }

    /*
     * My Customer Double Tap Listener
     * */
    private void initializeListeners() {

        // Initialize the Double Tap Listener
        myDoubleTapListener = new MyDoubleTapListener();

        // double touch left or right (playback / fastforward)
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener());

        // Pass the Double Tap Listener
        gestureDetector.setOnDoubleTapListener(myDoubleTapListener);

        // Initialize on Touch Listener
        touchlistener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Pass the current focus view id
                myDoubleTapListener.setCurrentViewId(v.getId());

                return gestureDetector.onTouchEvent(event);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
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
     * My Customer Double Tap Listener
     */
    class MyDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        private int mCurrViewId;

        // I received the current view info
        void setCurrentViewId(int passedViewId) {
            mCurrViewId = passedViewId;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {

            // If the player is available
            // TODO: Check if it is livestream
            if (mPlayerView != null) {

                double currPosition = mPlayerView.getPosition();

                switch (mCurrViewId) {

                    // Rewind by 10 seconds
                    case R.id.rewind_button:
                        if (currPosition - 10 >= 0) {
                            currPosition -= 10;
                        } else {
                            currPosition = 0;  // If the position goes negative, set it to 0
                        }
                        break;

                    // Fast Forward by 10 seconds
                    case R.id.fastforward_button:
                        if (currPosition + 10 <= mPlayerView.getDuration()) {
                            currPosition += 10;
                        } else {
                            currPosition = mPlayerView.getDuration(); // If the position goes past the content duration length, set it to the end
                        }
                        break;
                }
                mPlayerView.seek(currPosition);
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }
    }

}
