package com.jwplayer.opensourcedemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {

    private List<PlaylistItem> list;
    private MyClickListener myClickListener;

    MyRecyclerAdapter(List<PlaylistItem> myPlaylist, MyClickListener myClickListener){
        list = myPlaylist;
        this.myClickListener = myClickListener;
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
        myViewHolder.bind(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView grid;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            grid = itemView.findViewById(R.id.grid);
        }

        private void bind (PlaylistItem playlistItem, int position) {
            Picasso.get()
                    .load(playlistItem.getImage())
                    .into(grid);

            grid.setOnClickListener(view -> {
                myClickListener.click(position);
                Toast.makeText(itemView.getContext(), playlistItem.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
