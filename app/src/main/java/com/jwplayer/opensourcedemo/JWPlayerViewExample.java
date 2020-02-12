package com.jwplayer.opensourcedemo;

import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.List;


public class JWPlayerViewExample extends AppCompatActivity {

    /**
     * Intent action for media controls from Picture-in-Picture mode.
     */
    private final String JWPLAYER_MEDIA_ACTION_CONTROLS = "media_control";

    /**
     * Intent extra for media controls from Picture-in-Picture mode.
     */
    private final String EXTRA_PLAYER_STATE = "player_state";

    /**
     * The request code for play action PendingIntent.
     */
    private final int REQUEST_PLAY = 1;

    /**
     * The request code for pause action PendingIntent.
     */
    private final int REQUEST_PAUSE = 0;

    /**
     * The intent extra value for play action.
     */
    private final int PLAYER_STATE_PLAY = 1;

    /**
     * The intent extra value for pause action.
     */
    private final int PLAYER_STATE_PAUSE = 0;

    /*
     * JWPlayerView
     * */
    private JWPlayerView mPlayerView;
    /**
     * The arguments to be used for Picture-in-Picture mode.
     */
    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;

    /*
     * Send broadcast to update the picture-in-picture UI
     * */
    private BroadcastReceiver mReceiver;
    private boolean gotoPipMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        // Instantiate Picture in Picture Builder
        mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();

        // On Play, update UI to show Pause button
        mPlayerView.addOnPlayListener(playEvent -> {
            if (isInPictureInPictureMode()) {
                updatePictureInPictureMode(REQUEST_PAUSE);
            }
        });

        // On Pause, update UI to show Play button
        mPlayerView.addOnPauseListener(pauseEvent -> {
            if (isInPictureInPictureMode()) {
                updatePictureInPictureMode(REQUEST_PLAY);
            }
        });

        // Load a media source
        PlaylistItem pi = new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .title("ShortVideo")
                .description("A video player testing video.")
                .build();

        // List of Playlistitem
        List<PlaylistItem> item = new ArrayList<>();

        item.add(pi);
        item.add(pi);

        // Load Playlist
        mPlayerView.load(item);

        // Initialize event listeners
        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
    }

    /*
     * If user clicks back & home button
     * Then, they will enter PIP
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Goes into picture in picture
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Rational aspectRatio = new Rational(mPlayerView.getWidth(), mPlayerView.getHeight());
            mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio);
            this.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Adding Controls - Handles the PiP UI
     * <p>
     * For more info:
     * {@link - https://developer.android.com/guide/topics/ui/picture-in-picture#adding-controls}
     * {@link -  https://github.com/googlearchive/android-PictureInPicture/blob/master/app/src/main/java/com/example/android/pictureinpicture/MediaSessionPlaybackActivity.java}
     */
    void updatePictureInPictureMode(int request) {

        final ArrayList<RemoteAction> actions = new ArrayList<>();
        final int[] icons = {R.drawable.exo_icon_pause, R.drawable.exo_icon_play};

        Icon icon = Icon.createWithResource(getApplicationContext(), icons[request]);
        Intent mediaState = new Intent(JWPLAYER_MEDIA_ACTION_CONTROLS).putExtra(EXTRA_PLAYER_STATE, request);

        PendingIntent intent = PendingIntent.getBroadcast(
                getApplicationContext(),
                request,
                mediaState,
                0);

        RemoteAction action = new RemoteAction(icon, "", "", intent);
        actions.add(action);

        mPictureInPictureParamsBuilder.setActions(actions);
        setPictureInPictureParams(mPictureInPictureParamsBuilder.build());
    }

    /*
     * Show/Hide action bar
     * */
    void hideActionBar(boolean hide) {
        ActionBar mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            if (hide) {
                mActionBar.hide();
            } else {
                mActionBar.show();
            }
        }
    }

    /*
     * Register Receivers
     * */
    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();

        if (isInPictureInPictureMode()) {
            // Hide action bar
            hideActionBar(true);

            // Don't show player controls, PIP controls can show
            mPlayerView.setControls(false);

            // When PIP UI needs update
            mReceiver =
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent == null || !JWPLAYER_MEDIA_ACTION_CONTROLS.equals(intent.getAction())) {
                                Log.i("JWPLAYEREXAMPLE", "- BroadcastReceiver - intent null ");
                                return;
                            }
                            // This is where we are called back from Picture-in-Picture action items.
                            if (intent.hasExtra(EXTRA_PLAYER_STATE)) {

                                // Default player state
                                int playerState = PLAYER_STATE_PAUSE;

                                Bundle bundle = intent.getExtras();

                                if (bundle != null) {
                                    playerState = bundle.getInt(EXTRA_PLAYER_STATE, playerState);
                                }

                                switch (playerState) {
                                    case PLAYER_STATE_PLAY:
                                        mPlayerView.play();
                                        break;
                                    case PLAYER_STATE_PAUSE:
                                        mPlayerView.pause();
                                        break;
                                }
                            }
                        }
                    };

            // Registered receiver
            registerReceiver(mReceiver, new IntentFilter(JWPLAYER_MEDIA_ACTION_CONTROLS));
        } else {
            // not PIP
            hideActionBar(false);
            mPlayerView.setControls(true);
        }
    }

    /**
     * Resume Playback
     * When your activity switches out of PIP mode back to full-screen mode,
     * the system resumes your activity and calls your onResume() method.
     * {@link - https://developer.android.com/guide/topics/ui/picture-in-picture#continuing_playback}
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isInPictureInPictureMode()) {
            mPlayerView.play();
        } else {
            mPlayerView.onResume();
        }
    }

    /*
     * When your activity switches to PIP,
     * the system places the activity in the paused state and calls the activity's onPause() method.
     * Video playback should not be paused and should continue playing if the activity is paused while in PIP mode.
     * */
    @Override
    protected void onPause() {
        super.onPause();
        if (!isInPictureInPictureMode()) {
            mPlayerView.onPause();
        }
    }

    /*
     * In Android 7.0 and later,
     * you should pause and resume video playback
     * when the system calls your activity's onStop() and onStart().
     *
     * Todo: this needs fix
     * */
    @Override
    protected void onStop() {
        super.onStop();

        if (isInPictureInPictureMode()) {
            mPlayerView.onPause();
        } else {
            mPlayerView.onStop();
            // We are out of PiP mode. We can stop receiving events from it.
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isInPictureInPictureMode()) {
            mPlayerView.onDestroy();
        }
    }

}
