package com.jwplayer.opensourcedemo.jwrecyclerview;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jwplayer.opensourcedemo.R;
import com.jwplayer.opensourcedemo.jwrecyclerview.RecyclerViewFragment.ViewHolderLifeCycleCallback;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

/**
 * Provide a reference to the type of views that you are using (custom JWPlayerViewHolder)
 */
public class JWPlayerViewHolder extends RecyclerView.ViewHolder implements ViewHolderLifeCycleCallback {

    private static final String TAG = "HYUNJOO-ViewHolder";

    private final TextView textView;
    private JWPlayerView mPlayerView;
    private ImageView mImageView;

    public JWPlayerViewHolder(View v) {
        super(v);

        mImageView = v.findViewById(R.id.imageview);
        mPlayerView = v.findViewById(R.id.jwplayerview);
        textView = v.findViewById(R.id.textView);

        hide(true);

        mPlayerView.getConfig().setPreload(true);
        mPlayerView.setControls(false);

        mPlayerView.addOnFirstFrameListener(firstFrameEvent -> hide(false));
    }

    public void hide(boolean hidePlayer) {

        if (hidePlayer) {
            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
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

        hide(true);

    }

    public void showPlayer(PlaylistItem item) {
        mPlayerView.load(item);
        mPlayerView.play();
        hide(false);
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
