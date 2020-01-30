package com.jwplayer.opensourcedemo;

import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.fullscreen.FullscreenHandler;


/*
* Fullscreen Vertically and Horizontally; NOT only horizontally
*
* */
public class MyFullscreenHandler implements FullscreenHandler {
    // Minimized View Size
    private ConstraintLayout.LayoutParams minimizedView;

    private JWPlayerView mPlayer;

    MyFullscreenHandler(JWPlayerView player){
        mPlayer = player;

        // Save the values Minimized layout param values
        minimizedView = (ConstraintLayout.LayoutParams) player.getLayoutParams();

    }

    @Override
    public void onFullscreenRequested() {
        // Set window fullscreen and remove title bar, and force landscape orientation
        ConstraintLayout.LayoutParams fullscreenParams =
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT);
        mPlayer.setLayoutParams(fullscreenParams);
    }

    @Override
    public void onFullscreenExitRequested() {
        // Minimize the screen
        mPlayer.setLayoutParams(minimizedView);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onAllowRotationChanged(boolean b) {
    }

    @Override
    public void updateLayoutParams(ViewGroup.LayoutParams layoutParams) {
    }

    @Override
    public void setUseFullscreenLayoutFlags(boolean b) {
    }
}
