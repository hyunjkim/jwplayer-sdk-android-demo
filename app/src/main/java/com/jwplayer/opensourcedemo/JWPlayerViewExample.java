package com.jwplayer.opensourcedemo;

import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;


public class JWPlayerViewExample extends AppCompatActivity {

    private final int REQUEST_PLAY = 1;
    private final int REQUEST_PAUSE = 0;
    private final int PLAYER_STATE_PLAY= 1;
    private final int PLAYER_STATE_PAUSE = 0;
    private final String JWPLAYER_MEDIA_ACTION_CONTROLS = "media_control";
    private final String EXTRA_PLAYER_STATE = "player_state";
    private JWPlayerView mPlayerView;
    /**
     * The arguments to be used for Picture-in-Picture mode.
     */
    private PictureInPictureParams.Builder mPictureInPictureParamsBuilder;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();

            mPlayerView.addOnPlayListener(playEvent -> {
                if (isInPictureInPictureMode()) {
                    updatePictureInPictureMode(REQUEST_PAUSE);
                }
            });
            mPlayerView.addOnPauseListener(pauseEvent -> {
                if (isInPictureInPictureMode()) {
                    updatePictureInPictureMode(REQUEST_PLAY);
                }
            });
        }

        // Load a media source
        PlaylistItem pi = new PlaylistItem.Builder()
                .file("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
                .title("BipBop")
                .description("A video player testing video.")
                .build();

        mPlayerView.load(pi);

        // Initialize event listeners
        CallbackScreen cbs = findViewById(R.id.callback_screen);
        cbs.registerListeners(mPlayerView);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            // Goes into picture in picture
            case KeyEvent.KEYCODE_BACK:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Rational aspectRatio = new Rational(mPlayerView.getWidth(), mPlayerView.getHeight());
                    mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio);
                    this.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                mPlayerView.play();
                updatePictureInPictureMode(REQUEST_PAUSE);
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                mPlayerView.pause();
                updatePictureInPictureMode(REQUEST_PLAY);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        // TODO: handle playback resume
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updatePictureInPictureMode(int request) {
        final ArrayList<RemoteAction> actions = new ArrayList<>();
        final int[] icons = {R.drawable.exo_icon_pause, R.drawable.exo_icon_play};
        Icon icon = Icon.createWithResource(getApplicationContext(), icons[request]);

        Intent mediaState = new Intent(JWPLAYER_MEDIA_ACTION_CONTROLS).putExtra(EXTRA_PLAYER_STATE, request);

        PendingIntent intent =
                PendingIntent.getBroadcast(
                        getApplicationContext(),
                        request,
                        mediaState,
                        0);

        RemoteAction action = new RemoteAction(icon, "", "", intent);
        actions.add(action);

        mPictureInPictureParamsBuilder.setActions(actions);
        setPictureInPictureParams(mPictureInPictureParamsBuilder.build());
    }

    void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    void showActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        if (isInPictureInPictureMode()) {
            hideActionBar();
            mPlayerView.setControls(false);
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
                                int playerState = 0;
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
            registerReceiver(mReceiver, new IntentFilter(JWPLAYER_MEDIA_ACTION_CONTROLS));
        } else {
            showActionBar();
            mPlayerView.setControls(true);
        }
        mPlayerView.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if (isInPictureInPictureMode()) {
            mPlayerView.play();
        } else {
            mPlayerView.onResume();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();
        if (!isInPictureInPictureMode()) {
            mPlayerView.onPause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStop() {
        super.onStop();
        // We are out of PiP mode. We can stop receiving events from it.
        if (isInPictureInPictureMode()) {
            mPlayerView.onPause();
        } else {
            mPlayerView.onStop();
            // We are out of PiP mode. We can stop receiving events from it.
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

}
