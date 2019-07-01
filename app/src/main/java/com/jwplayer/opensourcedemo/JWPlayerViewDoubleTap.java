package com.jwplayer.opensourcedemo;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.core.PlayerState;

public class JWPlayerViewDoubleTap extends JWPlayerView implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnGenericMotionListener {

    private final String TAG = "JWEVENT (DOUBLETAP)";
    boolean isDoubleTapped = false;
    private long startTimer = -1;
    private int tapped = 0;
    private boolean isControlbarVisible = false;
    private int currState = 1;

    public JWPlayerViewDoubleTap(Context context, PlayerConfig playerConfig) {
        super(context, playerConfig);
    }

    public JWPlayerViewDoubleTap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addGestureListener(Context context) {
        GestureDetectorCompat mDetector = new GestureDetectorCompat(context, this);
        mDetector.setOnDoubleTapListener(this);
        setOnGenericMotionListener(this);
        addOnControlBarVisibilityListener(controlBarVisibilityEvent -> {
            isControlbarVisible = controlBarVisibilityEvent.isVisible();
            Log.i(TAG, "CONTROLBAR SHOW? : " + isControlbarVisible);

            // Reset the timer
            if (!isControlbarVisible) resetTimer();
        });
    }

    public void resetTimer() {
        currState = -1;
        startTimer = System.currentTimeMillis();
        tapped = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // Player size
        int tappingArea = (getHeight() / 4) * 3;

        // Player movement by y-axis
        int movement = (int) ev.getY();

        // Tap counts
        tapped++;

        // Did user double tap?
        isDoubleTapped = onDoubleTap(ev);

        // Player is on Idle / Buffer / Playing?
        boolean playerState = (this.getState() != PlayerState.PAUSED || this.getState() != PlayerState.COMPLETE);

        Log.i("JWDOUBLETAP", "========  FINGER MOVEMENT: " + movement + " | Taps: " + tapped + " | ControlBarArea ? " + (movement > tappingArea) + " | Controlbarvisible? " + isControlbarVisible + " | Player State? " + this.getState() + " | DOUBLE TAP? " + isDoubleTapped);


        // Player is playing / idled / buffering & User double tapped above control bars & current state is still here
        // - OR -
        // Player is playing / idled / buffering & User touched above the controls bars & current state is still above control bar
        if (playerState && isDoubleTapped && (movement < tappingArea && currState == -1)
                || playerState && (movement < tappingArea) && currState == -1) {
            Log.i(TAG, " ------------------------------------------------------ NO FULL SCREEEN ");
            resetTimer(); // current state is -1
            return true;
        }
        // User tapped on the control bar - OR - Player paused / complete & the controlbar is visible
        else if (movement > tappingArea || !playerState && isControlbarVisible) {
            Log.i(TAG, "------------------------------------------------------ Controlbar in Control:  " + movement);
            currState = 1;  // current state is 1
            return false;
        }

        return isDoubleTapped;
    }

    /**
     * If the user is tapping more than twice in less than 30ms, it's a double tap
     * Default doubletap timer in Android System is 300ms if you call
     * {@link ViewConfiguration}
     * {@see -  ViewConfiguration.getDoubleTapTimeout()}
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i(TAG, "onDoubleTap Tapped: " + tapped);
        Log.i(TAG, "onDoubleTap Current Timeout: " + (System.currentTimeMillis() - startTimer));

        return (tapped == 2 && (System.currentTimeMillis() - startTimer) <= 30);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        Log.i(TAG, "onDoubleTapEvent : " + e.getAction());
        return onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onGenericMotion(View v, MotionEvent event) {
        Log.i(TAG, "onGenericMotion : " + event.getAction());
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

