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
 *
 * Credits to : Google
 * {link - @https://github.com/android/views-widgets-samples/blob/master/RecyclerView/Application/src/main/java/com/example/android/recyclerview/CustomAdapter.java}
 */
public class RecyclerViewFragment extends Fragment implements RecyclerAdapter.PlayerActiveCallback {

    private static final String TAG = "HYUNJOO-RecyclerFrag";

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PlaylistItem> playlist;
    private View mActiveViewHolder;
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


        // Credits to : https://androidwave.com/exoplayer-in-recyclerview-in-android/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //  if the recyclerview is scrolling vertically
                if (recyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)) {

                    LinearLayoutManager myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    // This is handled by the LinearLayoutManager Class that checks with layoutbounds and layout flags
                    int scrollPosition = myLayoutManager.findFirstCompletelyVisibleItemPosition();

                    // Active View Holder
                    mActiveViewHolder = recyclerView.getLayoutManager().findViewByPosition(scrollPosition);

                    oldPosition = activePosition;

                    // Get the Active Holder position in the Adapter
                    activePosition = recyclerView.getChildAdapterPosition(mActiveViewHolder);

                    Log.d(TAG, "onScroll - OLD position: " + oldPosition);
                    Log.d(TAG, "onScroll - ACTIVE position: " + activePosition);

                } else {

                    // If the recyclerview stopped scrolling
                    oldPosition = activePosition;

                    // I am at the end of the recyclerview, this is the active view
                    activePosition = recyclerView.getAdapter().getItemCount() - 1;
                }

                // If the old view and the active view are the same - do nothing
                if (oldPosition != activePosition) {

                    // STOP the video before
                    mAdapter.notifyItemChanged(oldPosition, false);

                    // SETUP + AUTOPLAY this video
                    mAdapter.notifyItemChanged(activePosition, true);
                }
            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        // Check when the view disappears
        // Credits to : https://androidwave.com/exoplayer-in-recyclerview-in-android/
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View currentView) {
//
//                int preparePosition = mRecyclerView.getChildAdapterPosition(currentView);
//
//                mRecyclerView.post(new Runnable(){
//                    @Override
//                    public void run() {
//                        mAdapter.notifyItemChanged(preparePosition, false);
//                    }
//                });
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View currView) {

                // STOP THE CURRENT VIDEO - If this was the active view and it is leaving the view

                if (mActiveViewHolder != null && mActiveViewHolder.equals(currView)) {

                    Log.d(TAG, "onChildViewDetachedFromWindow: ");

                    // TODO: https://stackoverflow.com/questions/43221847/cannot-call-this-method-while-recyclerview-is-computing-a-layout-or-scrolling-wh
                    mRecyclerView.post(() -> mAdapter.notifyItemChanged(oldPosition, false));
                }
            }
        });

        return rootView;
    }


    // TODO: work on the lifecycle callbacks
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

    public interface ViewHolderLifeCycleCallback {
        void onStart();

        void onResume();

        void onPause();

        void onStop();

        void onDestroy();
    }
}
