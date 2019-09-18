package com.jwplayer.opensourcedemo;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.jwplayer.opensourcedemo.handlers.JWEventHandler;
import com.jwplayer.opensourcedemo.handlers.KeepScreenOnHandler;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class VideoDetailFragment extends Fragment implements VideoPlayerEvents.OnFullscreenListener {
    /**
     * Reference to the {@link CastSession}
     */
    private final SessionManagerListener<CastSession> mSessionManagerListener = new MySessionManagerListener();
    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;
    private LinearLayout mLinearLayout;
    private String file = "https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8";
    private CastSession mCastSession;
    private CastContext mCastContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("HYUNJOO", "onCreateView - VideoDetailFragment");

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_videodetailfragment, container, false);
        mLinearLayout = view.findViewById(R.id.layout_videodetailfragment);


        mPlayerView = view.findViewById(R.id.playerFragment);
        TextView outputTextView = view.findViewById(R.id.output);

        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getActivity().getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView);

        // Instantiate Cast Context
        mCastContext = CastContext.getSharedInstance(getActivity());

        // Setup JWPlayerView
        setupJWPlayerFrag(file);

        return view;
    }

    /*
     * Setup JWPlayerView
     * */
    private void setupJWPlayerFrag(String url) {
        PlayerConfig config = new PlayerConfig.Builder()
                .file(url)
                .autostart(true)
                .build();

        mPlayerView.setup(config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jwplayerview, menu);
        CastButtonFactory.setUpMediaRouteButton(getActivity(), menu, R.id.media_route_menu_item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onStart()");
            mPlayerView.onStart();
        }
    }

    @Override
    public void onResume() {
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(getActivity()).getSessionManager()
                    .getCurrentCastSession();
        }

        // Let JW Player know that the app has returned from the background
        super.onResume();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onResume()");
            mPlayerView.onResume();
        }
    }

    @Override
    public void onPause() {

        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        // Let JW Player know that the app is going to the background
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onPause()");
            mPlayerView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onStop()");
            mPlayerView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        // Let JW Player know that the app is being destroyed
        if (mPlayerView != null) {
            Log.i("HYUNJOO", "onDestroy()");
            mPlayerView.onDestroy();
        }
        super.onDestroy();
    }

    public void passFile(String fileClicked) {
        file = fileClicked;
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mLinearLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            setHasOptionsMenu(true);
            Log.i("HYUNJOO", "onSessionEnded");
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            setHasOptionsMenu(true);
            Log.i("HYUNJOO", "onSessionResumed");
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            setHasOptionsMenu(true);
            Log.i("HYUNJOO", "onSessionStarted");
        }

        @Override
        public void onSessionStarting(CastSession session) {
            Log.i("HYUNJOO", "onSessionStarting");
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
            Log.i("HYUNJOO", "onSessionStartFailed");
        }

        @Override
        public void onSessionEnding(CastSession session) {
            Log.i("HYUNJOO", "onSessionEnding");
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
            Log.i("HYUNJOO", "onSessionResuming");
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
            Log.i("HYUNJOO", "onSessionResumeFailed");
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
            Log.i("HYUNJOO", "onSessionSuspended");
        }
    }
}
