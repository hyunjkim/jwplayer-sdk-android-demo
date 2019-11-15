package com.jwplayer.opensourcedemo.jwrecyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jwplayer.opensourcedemo.R;
import com.jwplayer.opensourcedemo.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager}.
 */
public class RecyclerViewFragment extends Fragment implements RecyclerAdapter.PlayerActiveCallback {

    private static final String TAG = "HYUNJOO-RecyclerFrag";

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PlaylistItem> playlist;
    private View viewHolderParent;
    private int activePosition, oldPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);

        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mAdapter = new RecyclerAdapter(playlist, this);

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // There's a special case when the end of the list has been reached.
                // Need to handle that with this bit of logic

                if (recyclerView.canScrollVertically(0)) {

                    LinearLayoutManager myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int scrollPosition = myLayoutManager.findFirstCompletelyVisibleItemPosition();

                    viewHolderParent = recyclerView.getLayoutManager().findViewByPosition(scrollPosition);

                    oldPosition = activePosition;
                    activePosition = recyclerView.getChildAdapterPosition(viewHolderParent);

                    Log.d(TAG, "onScroll - old position: " + oldPosition);
                    Log.d(TAG, "onScroll - active position: " + activePosition);

                } else {
                    oldPosition = activePosition;
                    activePosition = recyclerView.getAdapter().getItemCount()-1;
                }


                // release the older position of the video
                mAdapter.notifyItemChanged(oldPosition, false);

                // autoplay the next video
                mAdapter.notifyItemChanged(activePosition, true);
            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (viewHolderParent != null && viewHolderParent.equals(view)) {
                    Log.d(TAG, "onChildViewDetachedFromWindow: ");


                    // TODO: https://stackoverflow.com/questions/43221847/cannot-call-this-method-while-recyclerview-is-computing-a-layout-or-scrolling-wh
                    mRecyclerView.post(new Runnable(){
                        @Override
                        public void run() {
                            mAdapter.notifyItemChanged(oldPosition, false);
                        }
                    });
                }
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
//        viewHolderCallBack.onStart();
    }

    @Override
    public void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
//        viewHolderCallBack.onResume();
    }

    @Override
    public void onPause() {
        // Let JW Player know that the app is going to the background
//        viewHolderCallBack.onPause();
        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
//        viewHolderCallBack.onStop();
    }

    @Override
    public void onDestroy() {
        // Let JW Player know that the app is being destroyed
//        viewHolderCallBack.onDestroy();
        super.onDestroy();
    }

    public void addViewHolderLifeCycleCallback(){

    }

    public interface ViewHolderLifeCycleCallback{
        void onStart();
        void onResume();
        void onPause();
        void onStop();
        void onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        playlist = SamplePlaylist.createPlaylist();
    }

    @Override
    public int playerActivePosition() {
        return activePosition;
    }
}
