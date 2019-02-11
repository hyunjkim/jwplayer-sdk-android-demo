package com.jwplayer.opensourcedemo.fullscreenhandler;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.jwplayer.opensourcedemo.R;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;


public class MyFullscreenHandler implements FullscreenHandler {

    private AppCompatActivity mActivity;
    private View mDecorView;
    private Window mWindow;
    private JWPlayerView mPlayer;
    private LinearLayout.LayoutParams minimize;
    private LinearLayout.LayoutParams toFullscreen;
    private View jwplayerview;
    private OrientationEventListener orientationEventListener;
    private String mOldState, currState;
    private View.OnSystemUiVisibilityChangeListener onSystemUiVisibilityChangeListener;
    private boolean mAllowRotation,mUseFullscreenLayoutFlags;

    public MyFullscreenHandler(AppCompatActivity activity, JWPlayerView jwPlayerView) {
        mActivity = activity;
        mPlayer = jwPlayerView;
        mWindow = mActivity.getWindow();
        mDecorView = mWindow.getDecorView();

        currState = getOrientation();

        // Get the parent view of jwplayerview from the XML
        LinearLayout layout = mActivity.findViewById(R.id.jw_linearlayout);

        onSystemUiVisibilityChangeListener = new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN  flags are set.

                if ((visibility & SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or other navigational controls.
                    print("The system bars are visible");
                    mWindow.clearFlags(SYSTEM_UI_FLAG_FULLSCREEN);
                    mWindow.addFlags(FLAG_FORCE_NOT_FULLSCREEN);
//                    mDecorView.setSystemUiVisibility(0); // Clear all flags
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                    print("The system bars are NOT visible");
                    mDecorView.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE); // Dim the status bar
                    mDecorView.setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION); // Double tap to enter & exit fullscreen
                    mDecorView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN); // Double tap to enter & exit fullscreen
                }
            }
        };

        mDecorView.setOnSystemUiVisibilityChangeListener(onSystemUiVisibilityChangeListener);

        orientationEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {

                mOldState = currState;

                if(50 <= orientation && orientation < 120 || 250 <= orientation && orientation <= 330){
                    print("onOrientationChanged() : landscape");
                    currState = "landscape";
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER); // Listen to user's orientation
                } else {
                    print("onOrientationChanged() : portrait");
                    currState = "portrait";
                }

            }
        };

        // Get jwplayerview from the parent
        jwplayerview = layout.getChildAt(0);

        // Set the desired minimize view and to fullscreen view
        minimize = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1);
        toFullscreen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void setMyFullscreen(boolean fullscreen) {

        boolean isLandscape = getOrientation().equals("landscape");

        if (!fullscreen || (!isLandscape && mOldState.equals("landscape") && fullscreen)){
            print("1) setMyFullscreen(FALSE): minimize");
            mPlayer.setFullscreen(false,true);
            setSize("minimize"); // Exit full screen
        } else {
            print("2) setMyFullscreen(TRUE): set to landscape mode");
            setSize("fullscreen"); // Enter full screen
            mPlayer.setFullscreen(true,true);
        }

    }

    private void setSize(String screen){
        switch (screen){
            case "fullscreen":
                print("setSize() - FULLSCREEN");
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Set the player to Landscape
                mWindow.addFlags(SYSTEM_UI_FLAG_LOW_PROFILE);        // Hide the Status bar
                mWindow.addFlags(SYSTEM_UI_FLAG_HIDE_NAVIGATION);    // Hide the Navigation bar
                mDecorView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN); // Double tap to enter & exit fullscreen
                jwplayerview.setLayoutParams(toFullscreen);         // set Jwplayer to fullscreen
                break;
            case "minimize":
                print("setSize() - MINIMIZE");
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER); // to user's orientation preference
                mDecorView.setSystemUiVisibility(0);
                jwplayerview.setLayoutParams(minimize); // set jwplayer exit fullscreen
                break;
        }

        currState = getOrientation();
    }

    @Override
    public void onFullscreenRequested() {
        setMyFullscreen(true);
    }

    @Override
    public void onFullscreenExitRequested() {
        setMyFullscreen(false);
    }

    @Override
    public void onResume() {
        print("onResume()");
    }

    @Override
    public void onPause() {
        print("onPause()");
    }

    @Override
    public void onDestroy() {
        print("onDestroy()");
    }

    @Override
    public void onAllowRotationChanged(boolean allowRotate) {
        print("onAllowRotationChanged() " + allowRotate);
        mAllowRotation= allowRotate;
        if(allowRotate) orientationEventListener.enable();
    }

    @Override
    public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
    }

    /*
     * @param useFlags - true if the FullscreenHandler should use the SYSTEM_UI_FLAG_FULLSCREEN and SYSTEM_UI_FLAG_HIDE_NAVIGATION flag.
     * */
    @Override
    public void setUseFullscreenLayoutFlags(boolean useFlags) {
        print("setUseFullscreenLayoutFlags " + useFlags);
        mUseFullscreenLayoutFlags = useFlags;
    }


    /*
     * The current orientation: https://stackoverflow.com/questions/3663665/how-can-i-get-the-current-screen-orientation
     * */
    private String getOrientation() {
        int orientation = mActivity.getResources().getConfiguration().orientation;
        return orientation == 1 ? "portrait": "landscape";
    }

    private void print(String s){
        Log.i("JWPLAYERVIEWEVENT","(FullscreenHandler) ... "+s);
    }

}
