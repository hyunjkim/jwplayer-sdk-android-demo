package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

import static com.google.android.gms.cast.CastStatusCodes.APPLICATION_NOT_FOUND;
import static com.google.android.gms.cast.CastStatusCodes.APPLICATION_NOT_RUNNING;
import static com.google.android.gms.cast.CastStatusCodes.AUTHENTICATION_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.CANCELED;
import static com.google.android.gms.cast.CastStatusCodes.DEVICE_CONNECTION_SUSPENDED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_SERVICE_CREATION_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_SERVICE_DISCONNECTED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_STOPPING_SERVICE_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.FAILED;
import static com.google.android.gms.cast.CastStatusCodes.INTERNAL_ERROR;
import static com.google.android.gms.cast.CastStatusCodes.INTERRUPTED;
import static com.google.android.gms.cast.CastStatusCodes.INVALID_REQUEST;
import static com.google.android.gms.cast.CastStatusCodes.MESSAGE_SEND_BUFFER_TOO_FULL;
import static com.google.android.gms.cast.CastStatusCodes.MESSAGE_TOO_LARGE;
import static com.google.android.gms.cast.CastStatusCodes.NETWORK_ERROR;
import static com.google.android.gms.cast.CastStatusCodes.NOT_ALLOWED;
import static com.google.android.gms.cast.CastStatusCodes.REPLACED;
import static com.google.android.gms.cast.CastStatusCodes.SUCCESS;
import static com.google.android.gms.cast.CastStatusCodes.TIMEOUT;
import static com.google.android.gms.cast.CastStatusCodes.UNKNOWN_ERROR;

public class JWPlayerFragmentExample extends AppCompatActivity {

    private final SessionManagerListener<CastSession> mSessionManagerListener = new MySessionManagerListener();

    /**
     * A reference to the {@link JWPlayerSupportFragment}.
     */
    private JWPlayerSupportFragment mPlayerFragment;

    /**
     * A reference to the {@link JWPlayerView} used by the JWPlayerSupportFragment.
     */
    private JWPlayerView mPlayerView;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManager mSessionManager;
    private MyCastListener myCastListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerfragment);

        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollview = findViewById(R.id.scrollview);
        MediaRouteButton mMediaRouteButton = findViewById(R.id.media_route_button);
        mMediaRouteButton.bringToFront();

        // Setup JWPlayer
        setupJWPlayer();

        // Print Build Version
        Logger.newStringBuilder();
        String jwplayerBuildVersion = Logger.updateOutput("Build Version: " + mPlayerView.getVersionCode() + "\r\nJW Player Fragment Example \r\n");
        outputTextView.setText(jwplayerBuildVersion);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollview);

        // Add my Custom cast button
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);

        // Add Cast Listener
        myCastListener = new MyCastListener(mMediaRouteButton, mPlayerView);
//        myCastListener = new MyCastListener(mPlayerView);
//
//        // Get a reference to the CastContext
//        mCastContext = CastContext.getSharedInstance(this);
//
//        // Set my Cast Listener to the Cast Context
//        mCastContext.addCastStateListener(myCastListener);
//
//        // Session Manager Listener
//        mSessionManager = mCastContext.getSessionManager();
    }

    /*
     * Setup JWPlayer Fragment Example
     */
    private void setupJWPlayer() {

        String caps = "https://playertest.longtailvideo.com/adaptive/bbcc/output.m3u8";

        // Construct a new JWPlayerSupportFragment (since we're using AppCompatActivity)
        mPlayerFragment = JWPlayerSupportFragment.newInstance(new PlayerConfig.Builder()
                .file(caps)
                .autostart(true)
                .build());

        // Attach the Fragment to our layout
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, mPlayerFragment);
        ft.commit();

        // Make sure all the pending fragment transactions have been completed, otherwise  mPlayerFragment.getPlayer() may return null
        fm.executePendingTransactions();

        // Get a reference to the JWPlayerView from the fragment
        mPlayerView = mPlayerFragment.getPlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerfragment, menu);

//         Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_view:
                Intent i = new Intent(this, JWPlayerViewExample.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void removeCastSession() {
//        mCastContext.getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);
//    }

    @Override
    protected void onStart() {
//        removeCastSession();
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onStop() {
//        removeCastSession();
        super.onStop();
        mPlayerView.onStop();
    }

//    @Override
//    protected void onResume() {
//        mCastContext.addCastStateListener(myCastListener);
//        mCastContext.getSessionManager().addSessionManagerListener(
//                mSessionManagerListener, CastSession.class);
//        if (mCastSession == null) {
//            mCastSession = CastContext.getSharedInstance(this).getSessionManager().getCurrentCastSession();
//        }
//        super.onResume();
//        mPlayerView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        mCastContext.removeCastStateListener(myCastListener);
//        mCastContext.getSessionManager().addSessionManagerListener(mSessionManagerListener, CastSession.class);
//        mCastSession = null;
//        mPlayerView.onPause();
//        super.onPause();
//    }

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        public void onSessionStarting(CastSession castSession) {
            print("onSessionStarting - category: " + castSession.getCategory());
            print("onSessionStarting - Session ID: " + castSession.getSessionId());
            check("onSessionStarting", castSession);
        }

        @Override
        public void onSessionStarted(CastSession castSession, String sessionId) {
            mCastSession = castSession;
            print("onSessionStarted - a session has been successfully started.\n" +
                    "\n Cast Session: " + castSession.getSessionId() + "\n Session ID: " + sessionId);
            check("onSessionStarted", castSession);
        }

        @Override
        public void onSessionStartFailed(CastSession castSession, int error) {
            print("onSessionStartFailed Session ID: " + castSession.getSessionId());
            printError("onSessionStartFailed", error);
            check("onSessionStartFailed", castSession);
        }

        @Override
        public void onSessionEnding(CastSession castSession) {
            print("onSession Ending Session ID: " + castSession.getSessionId());
            check("onSession Ending", castSession);
        }

        @Override
        public void onSessionEnded(CastSession castSession, int error) {
            print("onSession Ended Session ID: " + castSession.getSessionId());
            printError("onSessionEnded", error);
            check("onSessionEnded", castSession);
            if (castSession == mCastSession) {
                mCastSession = null;
            }
        }

        @Override
        public void onSessionResuming(CastSession castSession, String sessionId) {
            print("onSession Resuming Session ID: " + castSession.getSessionId() + " Session ID: " + sessionId);
            check("onSessionResuming", castSession);
        }

        @Override
        public void onSessionResumed(CastSession castSession, boolean wasSuspended) {
            mCastSession = castSession;
            print("onSession Resumed Session ID: " + castSession.getSessionId() + " wasSuspended: " + wasSuspended);
            check("onSessionResumed", castSession);
        }

        @Override
        public void onSessionResumeFailed(CastSession castSession, int error) {
            print("onSession Resume Failed Session ID: " + castSession.getSessionId() + " error : " + error);
            printError("onSessionResumeFailed", error);
            check("onSessionResumeFailed", castSession);
        }

        @Override
        public void onSessionSuspended(CastSession castSession, int reason) {
            print("onSessionSuspended Session ID: " + castSession.getSessionId() + " reason : " + reason);
            check("onSessionSuspended", castSession);

        }

        private void printError(String output, int error) {

            switch (error) {
                case SUCCESS:
                    print(output + " ERROR CODE: SUCCESS CODE: " + error);
                    break;
                case APPLICATION_NOT_FOUND:
                    print(output + " ERROR CODE: APPLICATION_NOT_FOUND CODE: " + error);
                    break;
                case NETWORK_ERROR:
                    print(output + " ERROR CODE: NETWORK_ERROR CODE: " + error);
                    break;
                case TIMEOUT:
                    print(output + " ERROR CODE: TIMEOUT CODE: " + error);
                    break;
                case INTERRUPTED:
                    print(output + " ERROR CODE: INTERRUPTED CODE: " + error);
                    break;
                case INTERNAL_ERROR:
                    print(output + " ERROR CODE: INTERNAL_ERROR CODE: " + error);
                    break;
                case UNKNOWN_ERROR:
                    print(output + " ERROR CODE: UNKNOWN_ERROR CODE: " + error);
                    break;
                case AUTHENTICATION_FAILED:
                    print(output + " ERROR CODE: AUTHENTICATION_FAILED CODE: " + error);
                    break;
                case INVALID_REQUEST:
                    print(output + " ERROR CODE: INVALID_REQUEST CODE: " + error);
                    break;
                case CANCELED:
                    print(output + " ERROR CODE: CANCELED CODE: " + error);
                    break;
                case NOT_ALLOWED:
                    print(output + " ERROR CODE: NOT_ALLOWED CODE: " + error);
                    break;
                case APPLICATION_NOT_RUNNING:
                    print(output + " ERROR CODE: APPLICATION_NOT_RUNNING CODE: " + error);
                    break;
                case MESSAGE_TOO_LARGE:
                    print(output + " ERROR CODE: MESSAGE_TOO_LARGE CODE: " + error);
                    break;
                case MESSAGE_SEND_BUFFER_TOO_FULL:
                    print(output + " ERROR CODE: MESSAGE_SEND_BUFFER_TOO_FULL CODE: " + error);
                    break;
                case DEVICE_CONNECTION_SUSPENDED:
                    print(output + " ERROR CODE: DEVICE_CONNECTION_SUSPENDED CODE: " + error);
                    break;
                case FAILED:
                    print(output + " ERROR CODE: FAILED CODE: " + error);
                    break;
                case REPLACED:
                    print(output + " ERROR CODE: REPLACED CODE: " + error);
                    break;
                case ERROR_SERVICE_CREATION_FAILED:
                    print(output + " ERROR CODE: ERROR_SERVICE_CREATION_FAILED CODE: " + error);
                    break;
                case ERROR_SERVICE_DISCONNECTED:
                    print(output + " ERROR CODE: ERROR_SERVICE_DISCONNECTED CODE: " + error);
                    break;
                case ERROR_STOPPING_SERVICE_FAILED:
                    print(output + " ERROR CODE: ERROR_STOPPING_SERVICE_FAILED CODE: " + error);
                    break;
            }
        }

        private void check(String s, Session session) {
            if (session.isConnecting()) print(s + " - Session is connecting");
            if (session.isConnected()) print(s + " - Session is connected");
            if (session.isDisconnecting()) print(s + " - Session is disconnecting");
            if (session.isDisconnected()) print(s + " - Session is disconnected");
            if (session.isResuming()) print(s + " - Session is resuming");
            if (session.isSuspended()) print(s + " - Session is suspended");
        }

        private void print(String output) {
            String TAG = "MyCast SessionManagerListener";
            Log.i(TAG, "Fragment (CAST) " + output);
        }

    }
}

