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

    private List<PlaylistItem> list;

    MyRecyclerAdapter(List<PlaylistItem> myPlaylist) {
        list = myPlaylist;
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
        myViewHolder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
                Log.i("HYUNJOO", "Click on grid: " + playlistItem.getTitle() + "position: " + position);
                Toast.makeText(itemView.getContext(), playlistItem.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}