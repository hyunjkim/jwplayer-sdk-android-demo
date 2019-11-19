package com.jwplayer.opensourcedemo.jwrecyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jwplayer.opensourcedemo.R;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<JWPlayerViewHolder> {

    private static final String TAG = "HYUNJOO-RecyclerAdapter";

    private List<PlaylistItem> mPlaylist;
    private PlayerActiveCallback mPlayerActiveCallback;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param playlist List<PlaylistItem> containing the data to populate views to be used by RecyclerView.
     */
    // END_INCLUDE(recyclerViewSampleViewHolder)
    public RecyclerAdapter(List<PlaylistItem> playlist, PlayerActiveCallback playerActiveCallback) {
        mPlaylist = playlist;
        mPlayerActiveCallback = playerActiveCallback;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public JWPlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Create a new view.
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_row_item, viewGroup, false);

        return new JWPlayerViewHolder(v);
    }


    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(@NonNull JWPlayerViewHolder viewHolder, int position) {

        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        String title = mPlaylist.get(position).getTitle();
        String image = mPlaylist.get(position).getImage();
        String url = mPlaylist.get(position).getFile();

        viewHolder.currPosition(position);
        viewHolder.getPlayer().getConfig().setImage(image);
        viewHolder.getTextView().setText(title);

        Log.d(TAG, "1) mPlayerActiveCallback.ActivePosition(): " + mPlayerActiveCallback.playerActivePosition());

        if (mPlayerActiveCallback.playerActivePosition() == position) {
            Log.d(TAG, "1)  --------------------- onPlay Position: " + position);

            viewHolder.showPlayer(new PlaylistItem(url));

        } else {
            Log.d(TAG, "1)  --------------------- onStop Position: " + position);

            viewHolder.hidePlayer(image);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull JWPlayerViewHolder JWPlayerViewHolder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(JWPlayerViewHolder, position, payloads);
        Log.d(TAG, "onBindViewHolder + position: " + position + " payloads: " + payloads);

        String image = mPlaylist.get(position).getImage();

        if (!payloads.isEmpty() && payloads.get(0).equals("false")) {
            JWPlayerViewHolder.hidePlayer(image);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPlaylist.size();
    }

    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    public interface PlayerActiveCallback {
        int playerActivePosition();
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

}