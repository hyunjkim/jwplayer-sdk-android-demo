package com.jwplayer.opensourcedemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<PlaylistItem> mList;
    private JWPlayerView mPlayer;

    MyRecyclerViewAdapter(List<PlaylistItem> list, JWPlayerView mPlayer) {
        mList = list;
        this.mPlayer = mPlayer;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_recyclerview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        int viewHolderPosition = myViewHolder.getAdapterPosition();
        String mediaId = mList.get(viewHolderPosition).getMediaId();

        myViewHolder.tv.setText(mediaId);
        myViewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if (!mPlayer.getState().equals(PlayerState.COMPLETE)) {
                        mPlayer.stop();
                        mPlayer.load(mList.get(viewHolderPosition));
                        mPlayer.play();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends ViewHolder {
        TextView tv;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.rv_tv);
        }
    }
}
