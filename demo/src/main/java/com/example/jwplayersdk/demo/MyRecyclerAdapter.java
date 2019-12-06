package com.example.jwplayersdk.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jwplayersdk.demo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {

    private List<PlaylistItem> list = new ArrayList<>();
    private MyFragmentCallbackListener mListener;

    MyRecyclerAdapter() {
        super();
        list = SamplePlaylist.createPlaylist();
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_container, parent, false);

        return new MyRecyclerViewHolder(view);
    }

    /*
     * Click listener for each view
     * */
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {

        TextView tv = holder.itemView.findViewById(R.id.title_tv);
        tv.setText(list.get(position).getTitle());
        tv.setOnClickListener(v -> mListener.passNewFragmentInfo(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
     * Communicate to MainActivity that I want to replace JWPlayerView
     * */
    void setOnFragmentCallbackListener(MyFragmentCallbackListener listener) {
        mListener = listener;
    }

    interface MyFragmentCallbackListener {
        void passNewFragmentInfo(int position);
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
