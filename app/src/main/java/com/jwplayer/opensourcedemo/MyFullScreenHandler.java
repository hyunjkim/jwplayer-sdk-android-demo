package com.jwplayer.opensourcedemo;


import android.content.res.Configuration;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class MyFullScreenHandler implements FullscreenHandler,
        VideoPlayerEvents.OnFullscreenListener{

    private JWPlayerView mPlayer;
    private int mConfig;
    private Window mWindow;

    public MyFullScreenHandler(JWPlayerView jwPlayerView, int config, Window window) {
        mPlayer = jwPlayerView;
        mConfig = config;
        mWindow = window;
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        // Make a toast or log that it was on fullscreen
        Log.i("HYUNJOO", " onFullScreen? " + fullscreenEvent.getFullscreen());
    }
    @Override
    public void onFullscreenRequested() {
        setUseFullscreenLayoutFlags(true);

        mWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onFullscreenExitRequested() {
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    @Override
    public void onResume() {
        if(mPlayer != null) {
            mPlayer.onResume();
            mPlayer.play();
        }
    }

    @Override
    public void onPause() {
        if(mPlayer != null) {
            mPlayer.pause();
            mPlayer.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.onDestroy();
        }

    }

    @Override
    public void onAllowRotationChanged(boolean b) {
        mPlayer.setFullscreen(mConfig == Configuration.ORIENTATION_LANDSCAPE,b);
    }

    @Override
    public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
    }

    /*
        Called when JWPlayerView.setUseFullscreenLayoutFlags(boolean) is called.
        Use this in your fullscreen implementation to determine whether to use the
        android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
        and android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION flags
        in calls to View.setSystemUiVisibility(int).
    **/
    @Override
    public void setUseFullscreenLayoutFlags(boolean b) {
        if(b){
           mPlayer.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
