package com.jwplayer.opensourcedemo;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class MyJWPlayerView extends JWPlayerView implements
        JWPlayerView.OnDragListener {

    private GestureDetectorCompat mGestureDetector;

    public MyJWPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyJWPlayerView(Context context, PlayerConfig playerConfig) {
        super(context, playerConfig);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    /*
     * Copied and credits to: https://developer.android.com/training/gestures/detector.html
     * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onInterceptTouchEvent(event);

    }

    public void addOnDragListener(OnDragListener dragListener) {
        this.setOnDragListener(dragListener);
    }

    public void addOnGestureListener(Context context, MyGestureListener myGestureListener) {
        mGestureDetector = new GestureDetectorCompat(context, myGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(myGestureListener);
    }

    public void removeOnGestureListener(){
        mGestureDetector = null;
    }

}
