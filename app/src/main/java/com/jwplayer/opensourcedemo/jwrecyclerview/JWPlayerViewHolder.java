package com.jwplayer.opensourcedemo.jwrecyclerview;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jwplayer.opensourcedemo.R;
import com.jwplayer.opensourcedemo.jwrecyclerview.RecyclerViewFragment.ViewHolderLifeCycleCallback;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Provide a reference to the type of views that you are using (custom JWPlayerViewHolder)
 */
public class JWPlayerViewHolder extends RecyclerView.ViewHolder implements ViewHolderLifeCycleCallback {

    private static final String TAG = "HYUNJOO-ViewHolder";

    private final TextView textView, playerSizeTv;
    private JWPlayerView mPlayerView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private LinearLayout mParentLayout;

    public JWPlayerViewHolder(View v) {
        super(v);

        mParentLayout = v.findViewById(R.id.parent_vh_layout);
        mImageView = v.findViewById(R.id.imageview);
        mProgressBar = v.findViewById(R.id.temp_progressbar);
        mPlayerView = v.findViewById(R.id.jwplayerview);
        textView = v.findViewById(R.id.textView);
        playerSizeTv = v.findViewById(R.id.player_size_tv);

        mPlayerView.getConfig().setPreload(true);
        mPlayerView.setControls(false);

        hide(true, getCurrPlayerStateIdleOrBuffer());

        mPlayerView.addOnIdleListener(idleEvent -> hide(true, true));
        mPlayerView.addOnBufferListener(bufferEvent -> hide(true, true));
        mPlayerView.addOnFirstFrameListener(firstFrameEvent -> hide(false, false));
        mPlayerView.addOnCompleteListener(completeEvent -> hide(true, false));
    }

    public boolean getCurrPlayerStateIdleOrBuffer() {
        return mPlayerView.getState().equals(PlayerState.IDLE) || mPlayerView.getState().equals(PlayerState.BUFFERING);
    }

    public void hide(boolean hidePlayer, boolean showProgressBar) {

        if (showProgressBar) { // Buffering or Idled
            mPlayerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.bringToFront();
            mImageView.setVisibility(View.VISIBLE);

        } else if (hidePlayer) { // Not Playing
            mPlayerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mImageView.bringToFront();
        } else { // Playing
            mPlayerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
        }
    }

    public void hidePlayer(String image) {

        if (mPlayerView != null) {
            Log.d(TAG, "hidePlayer() STOP! ");
            mPlayerView.pause();
            mPlayerView.onStop();
        }

        Glide.with(mImageView)
                .load(image)
                .into(mImageView);

        hide(true, false);

    }

    public void showPlayer(PlaylistItem item) {
        Log.d(TAG, "showPlayer() PLAY!");

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

    public void currPosition(int position) {

        float[] heightParams = {0.02f, 0.1f, 0.4f, 0.8f, 3f};

        LinearLayout.LayoutParams mLayout = new LinearLayout.LayoutParams(MATCH_PARENT, 0, heightParams[position]);

        mParentLayout.getChildAt(0).setLayoutParams(mLayout);

        String size = "Player Size: " + heightParams[position];

        playerSizeTv.setText(size);
    }
}
