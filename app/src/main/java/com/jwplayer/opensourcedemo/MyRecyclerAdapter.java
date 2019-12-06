package com.jwplayer.opensourcedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {

    private List<PlaylistItem> list;
    private MyFragmentCallbackListener mListener;
    private String playlistName;

    MyRecyclerAdapter(MyFragmentCallbackListener listener, String playlistName, List<PlaylistItem> list) {
        super();
        this.playlistName = playlistName;
        this.list = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_container, parent, false);

        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {

        PlaylistItem item = list.get(position);

        TextView tv = holder.itemView.findViewById(R.id.title_tv);
        ImageView iv = holder.itemView.findViewById(R.id.image_view);

        // Show image
        Glide.with(iv)
                .load(item.getImage())
                .into(iv);

        tv.setText(item.getTitle());

        // When clicked, show the video
        iv.setOnClickListener(v -> mListener.reload(playlistName, position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    void updateData(String playlistName) {
        list = SamplePlaylist.getPlaylistItem(playlistName);
        notifyDataSetChanged();
    }

    interface MyFragmentCallbackListener {
        void reload(String playlistName, int position);
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
