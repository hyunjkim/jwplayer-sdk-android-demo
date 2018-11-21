package com.jwplayer.opensourcedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.longtailvideo.jwplayer.JWPlayerView;

public class VideoPlayerContainerView extends FrameLayout {

    public VideoPlayerContainerView(@NonNull Context context) {
        super(context);
    }

    public VideoPlayerContainerView(@NonNull Context context,
                                    @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JWPlayerView renderVideoContainer(){

        JWPlayerView playerView = (JWPlayerView) LayoutInflater
                .from(getContext())
                .inflate(R.layout.layout_jwplayerview, null);

        JWPlayerView.LayoutParams params  = new JWPlayerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        playerView.setLayoutParams(params);
        addView(playerView);

        return playerView;
    }

}
