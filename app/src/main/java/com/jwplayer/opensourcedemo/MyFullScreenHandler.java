package com.jwplayer.opensourcedemo;

import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;

class MyFullScreenHandler implements
        VideoPlayerEvents.OnFullscreenListener,
        FullscreenHandler {

    private JWPlayerViewExample mRoot;
    private JWPlayerView anotherJWView;
    private LinearLayout mCoordinatorLayout;

    MyFullScreenHandler(JWPlayerView anotherJWView, JWPlayerViewExample jwPlayerView){
        mRoot = jwPlayerView;
        this.anotherJWView=anotherJWView;
    }


    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = mRoot.getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

//         When going to Fullscreen we want to set fitsSystemWindows="false"
//        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    @Override
    public void onFullscreenRequested() {
        print("onFullscreenRequested ");
//        anotherJWView.setFullscreen(true,true);
    }

    @Override
    public void onFullscreenExitRequested() {
        print("onFullscreenExitRequested ");
        anotherJWView.setFullscreen(false,false);
    }

    @Override
    public void onResume() {
        anotherJWView.onResume();
    }

    @Override
    public void onPause() {
        anotherJWView.onPause();
    }

    @Override
    public void onDestroy() {
        anotherJWView.onDestroy();
    }

    @Override
    public void onAllowRotationChanged(boolean b) {
        print("onAllowRotationChanged: " + b);
    }

    @Override
    public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        print("params w: "+ layoutParams.width);
        print("params h: "+ layoutParams.height);
    }

    @Override
    public void setUseFullscreenLayoutFlags(boolean b) {
        print("SetUseFullScreenLayoutFlags: " + b);
    }

    private void print(String s){
        Log.i("JWFULLSCREENHANDLER", " " + s);
    }
}

