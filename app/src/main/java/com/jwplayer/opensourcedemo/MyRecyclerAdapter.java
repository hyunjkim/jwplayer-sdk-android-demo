package com.jwplayer.opensourcedemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {

    private List<PlaylistItem> list;
    private MyFragmentCallbackListener mListener;

    MyRecyclerAdapter(MyFragmentCallbackListener listener) {
        super();
        list = SamplePlaylist.createPlaylist();
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

        TextView tv = holder.itemView.findViewById(R.id.title_tv);
        tv.setText(list.get(position).getTitle());
        tv.setOnClickListener(v -> mListener.replaceFragment(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface MyFragmentCallbackListener {
        void replaceFragment(int position);
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
