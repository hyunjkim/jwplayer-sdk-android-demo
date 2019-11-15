package com.jwplayer.opensourcedemo.jwrecyclerview;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jwplayer.opensourcedemo.R;
import com.jwplayer.opensourcedemo.jwrecyclerview.RecyclerViewFragment.ViewHolderLifeCycleCallback;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

/**
 * Provide a reference to the type of views that you are using (custom JWPlayerViewHolder)
 */
public class JWPlayerViewHolder extends RecyclerView.ViewHolder implements ViewHolderLifeCycleCallback {

    private static final String TAG = "HYUNJOO-ViewHolder";

    private final TextView textView;
    private JWPlayerView mPlayerView;
    private ImageView mImageView;
    private FrameLayout mFrameLayout;
    private ProgressBar mProgressBar;

    public JWPlayerViewHolder(View v) {
        super(v);

        mImageView = v.findViewById(R.id.imageview);
        mFrameLayout = v.findViewById(R.id.temp_framelayout);
        mProgressBar = v.findViewById(R.id.temp_progressbar);
        mPlayerView = v.findViewById(R.id.jwplayerview);
        textView = v.findViewById(R.id.textView);

        mPlayerView.getConfig().setPreload(true);
        mPlayerView.setControls(false);

        hide(true, getCurrPlayerStateIdleOrBuffer());

        mPlayerView.addOnIdleListener(idleEvent -> hide(true,true));
        mPlayerView.addOnBufferListener(bufferEvent -> hide(true,true));
        mPlayerView.addOnFirstFrameListener(firstFrameEvent -> hide(false, false));
        mPlayerView.addOnCompleteListener(completeEvent -> hide(true,false));
    }

    public boolean getCurrPlayerStateIdleOrBuffer(){
        return mPlayerView.getState().equals(PlayerState.IDLE) || mPlayerView.getState().equals(PlayerState.BUFFERING);
    }

    public void hide(boolean hidePlayer, boolean showProgressBar) {

        if(showProgressBar) {
            mPlayerView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.bringToFront();
            mImageView.setVisibility(View.VISIBLE);
        } else if (hidePlayer){
            mPlayerView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
        }
    }

    public void hidePlayer(String image) {
        if (mPlayerView != null) {
            Log.d(TAG, "hidePlayer() STOPPPPPPPP ");
            mPlayerView.pause();
            mPlayerView.onStop();
        }

        Glide.with(mPlayerView)
                .load(image)
                .into(mImageView);

        hide(true, false);
    }

    public void showPlayer(PlaylistItem item) {
        Log.d(TAG, "showPlayer()");
        mPlayerView.load(item);
        mPlayerView.play();
        hide(false, getCurrPlayerStateIdleOrBuffer());
    }

    public JWPlayerView getPlayer() {
        return mPlayerView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void onStart() {
        if (mPlayerView != null) {
            mPlayerView.onStart();
        }
    }

    public void onResume() {
        if (mPlayerView != null) {
            mPlayerView.onResume();
        }
    }

    public void onPause() {
        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
    }

    public void onStop() {
        if (mPlayerView != null) {
            mPlayerView.onStop();
        }
    }

    public void onDestroy() {
        if (mPlayerView != null) {
            mPlayerView.onDestroy();
        }
    }
}
