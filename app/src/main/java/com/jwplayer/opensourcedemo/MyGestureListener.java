package com.jwplayer.opensourcedemo;

import android.content.ClipData;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.longtailvideo.jwplayer.JWPlayerView;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

public class MyGestureListener implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        MyJWPlayerView.OnDragListener {


    private final String TAG = "HYUNJOO";
    private LinearLayout mParentLinearLayout;
    private RelativeLayout mRelativeLayout;
    private boolean isMinimized = false, isNormalView = true;
    private JWPlayerView mPlayerView;

    MyGestureListener(JWPlayerView jwPlayerView, RelativeLayout mRelativeLayout, LinearLayout mParentLinearLayout) {
        mPlayerView = jwPlayerView;
        this.mParentLinearLayout = mParentLinearLayout;
        this.mRelativeLayout = mRelativeLayout;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(TAG, "onSingleTapConfirmed " + getEvent(e.getAction()));
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i(TAG, "onDoubleTap " + getEvent(e.getAction()));
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i(TAG, "onDoubleTapEvent " + getEvent(e.getAction()));
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown " + getEvent(e.getAction()));
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress " + getEvent(e.getAction()));
        if(!isNormalView) normalSizePlayer();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp " + getEvent(e.getAction()));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "onScroll " + getEvent(e1.getAction()) + " | "+ getEvent(e2.getAction()));
//        if (e1.getAction() == MotionEvent.ACTION_DOWN) {
////             Focus on JWPlayerView and controls
//            Log.i("HYUNJOO", "start drag");
//            ClipData dragData = ClipData.newPlainText("", "");
//            View.DragShadowBuilder myShadow = new View.DragShadowBuilder();
//            mPlayerView.startDrag(dragData, myShadow, mPlayerView, 0);
//            mPlayerView.setVisibility(View.VISIBLE);
////            return true;
//        }
        if(distanceY < 0) minimize();
        else normalSizePlayer();
        Log.i(TAG, "onScroll x: " + distanceX + " | y: "+ distanceY );
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress " + getEvent(e.getAction()));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling " + getEvent(e1.getAction()) + " | " + getEvent(e2.getAction()));
        Log.i(TAG, "onFling x: " + velocityX + " | : "+ velocityY );
        return false;
    }

    /*
     * Credits to Tutorials Point: https://www.tutorialspoint.com/android/android_drag_and_drop.htm
     * TODO: Work on transitions
     * */
    @Override
    public boolean onDrag(View v, DragEvent event) {

        int x_cord;
        int y_cord;

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED:
                Log.i("HYUNJOO", "ACTION_DRAG_ENDED ");
                if (!isMinimized) minimize();
                else normalSizePlayer();
                v.invalidate();
                break;
//            case DragEvent.ACTION_DRAG_EXITED:
////                Log.i("HYUNJOO", "ACTION_DRAG_EXITED ");
//////                normalSizePlayer();
////                v.invalidate();
//                break;
//            case DragEvent.ACTION_DRAG_LOCATION:
////                x_cord = (int) event.getX();
////                y_cord = (int) event.getY();
////                int total = x_cord + y_cord;
////                Log.i("HYUNJOO", "ACTION_DRAG_LOCATION -  location \r\nx: " + x_cord + "\r\ny: " + y_cord + "\r\nTotal: " + total);
////                break;
//            case DragEvent.ACTION_DROP:
////                x_cord = (int) event.getX();
////                y_cord = (int) event.getY();
//                Log.i("HYUNJOO", "ACTION_DROP - location x: " + x_cord + "    y: " + y_cord);
//                break;
//            case DragEvent.ACTION_DRAG_STARTED:
////                x_cord = (int) event.getX();
////                y_cord = (int) event.getY();
//                Log.i("HYUNJOO", "ACTION_DRAG_STARTED - location x: " + x_cord + " y: " + y_cord);
////                v.invalidate();
//                break;
//            case DragEvent.ACTION_DRAG_ENTERED:
////                x_cord = (int) event.getX();
////                y_cord = (int) event.getY();
////                v.invalidate();
//                Log.i("HYUNJOO", "ACTION_DRAG_ENTERED - location x: " + x_cord + " y: " + y_cord);
//                break;
//        }
        }
        return true;
    }

    /**
     * Minimize JW Player View
     */
    private void minimize() {
        if (mPlayerView == null) return;
        if (!isMinimized) {
            Log.i("HYUNJOO", "minimize");
            // Get the JWPlayerView from the Parent Linear Layout, which should be at index 0
            View player = mParentLinearLayout.getChildAt(0);

            // Remove JWPlayerView from the LinearLayout
            mParentLinearLayout.removeViewAt(0);

            // Setting the parameters of minimized view to the bottom right
            RelativeLayout.LayoutParams minimizedParams = new RelativeLayout.LayoutParams(
                    (mPlayerView.getWidth() / 2), (mPlayerView.getHeight() / 2));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                minimizedParams.addRule(ALIGN_PARENT_END);
            }
            minimizedParams.addRule(ALIGN_PARENT_RIGHT);
            minimizedParams.addRule(ALIGN_PARENT_BOTTOM);

            // Pass the parameters to JWPlayerView
            player.setLayoutParams(minimizedParams);

            // Adding the minimized jwplayerview to the parent view
            mRelativeLayout.addView(player);

            // Minimize = true
            isMinimized = !isMinimized;
            isNormalView = !isMinimized;
        }
    }

    /**
     * Return JW Player View to LinearLayout
     */
    private void normalSizePlayer() {
        if (mPlayerView == null) return;

        if (isMinimized) {
            Log.i("HYUNJOO", "normal");

            // Remove the specific location of the player from the Relative Layout
            View player = mRelativeLayout.getChildAt(2);

            // Remove the minimized view from Relative Layout
            mRelativeLayout.removeViewAt(2);

            // Setting the attributes to fit JWPlayerView back into the parent linear layout
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);

            // Pass the parameters here
            player.setLayoutParams(linearLayoutParams);

            // Added back to the parent linear layout
            mParentLinearLayout.addView(player, 0);

            // TODO: I don't understand why this code below didn't work, but the code above worked
//            mParentLinearLayout.addView(player, 0, linearLayoutParams);

            // Minimize = false
            isMinimized = !isMinimized;
        }
        isNormalView = !isMinimized;
    }

    String getEvent(int action){
        switch(action){
            case MotionEvent.ACTION_UP:
                return " " +action + " MotionEvent.ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return " " +action + " MotionEvent.ACTION_CANCEL";
            case MotionEvent.ACTION_DOWN:
                return " " +action + " MotionEvent.ACTION_DOWN";
            case MotionEvent.ACTION_SCROLL:
                return " " +action + " MotionEvent.ACTION_SCROLL";
            case MotionEvent.ACTION_POINTER_UP:
                return " " +action + " MotionEvent.ACTION_POINTER_UP";
            case MotionEvent.ACTION_POINTER_DOWN:
                return " " +action + " MotionEvent.ACTION_POINTER_DOWN";
            case MotionEvent.ACTION_MOVE:
                return " " +action + " MotionEvent.ACTION_MOVE";
            case MotionEvent.ACTION_MASK:
                return " " +action + " MotionEvent.ACTION_MASK";
        }
        return "";
    }
}
