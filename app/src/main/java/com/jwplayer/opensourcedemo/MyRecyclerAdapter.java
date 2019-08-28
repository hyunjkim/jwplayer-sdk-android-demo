package com.jwplayer.opensourcedemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {

    private List<PlaylistItem> mPlaylist;

    MyRecyclerAdapter() {
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.layout_grids, viewGroup, false);
        return new MyRecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder myViewHolder, int position) {
        // Attach everything on the list
        myViewHolder.bind(mPlaylist.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mPlaylist.isEmpty()) {
            return 0;
        }
        return mPlaylist.size();
    }

    void setPlaylist(List<PlaylistItem> playlist) {
        mPlaylist = playlist;
        notifyDataSetChanged();
    }

    /*
     * MyRecyclerViewHolder
     */
    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView grid;
        TextView videotitle;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            grid = itemView.findViewById(R.id.grid);
            videotitle = itemView.findViewById(R.id.videotitle_tv);
        }

        private void bind(PlaylistItem playlistItem, int position) {
            Picasso.get()
                    .load(playlistItem.getImage())
                    .into(grid);
            videotitle.setText(playlistItem.getTitle());
            grid.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), playlistItem.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}