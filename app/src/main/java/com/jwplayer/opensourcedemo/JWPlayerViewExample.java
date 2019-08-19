package com.jwplayer.opensourcedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.jwplayer.opensourcedemo.jwutil.Logger;
import com.jwplayer.opensourcedemo.listeners.JWAdEventHandler;
import com.jwplayer.opensourcedemo.listeners.JWEventHandler;
import com.jwplayer.opensourcedemo.listeners.KeepScreenOnHandler;
import com.jwplayer.opensourcedemo.samples.SampleAds;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.ControlBarVisibilityEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.ads.ImaAdvertising;
import com.longtailvideo.jwplayer.media.ads.VMAPAdvertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

public class JWPlayerViewExample extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener,
        VideoPlayerEvents.OnControlBarVisibilityListener{

    /**
     * Reference to the {@link JWPlayerView}
     */
    private JWPlayerView mPlayerView;

    /**
     * Reference to the {@link CastContext}
     */
    private CastContext mCastContext;

    /**
     * Stored instance of CoordinatorLayout
     * {@link - http://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html}
     */
    private CoordinatorLayout mCoordinatorLayout;
    private WebView webview;

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        TextView outputTextView = findViewById(R.id.output);
        ScrollView scrollView = findViewById(R.id.scroll);
        mCoordinatorLayout = findViewById(R.id.activity_jwplayerview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Print JWPlayer Version
        outputTextView.setText(Logger.generateLogLine("JWPlayerViewExample \r\nBuild version: " + mPlayerView.getVersionCode()));

        // Handle hiding/showing of ActionBar
        mPlayerView.addOnFullscreenListener(this);

        // Keep the screen on during playback
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Instantiate the JW Player event handler class
        new JWEventHandler(mPlayerView, outputTextView, scrollView);

        // Instantiate the JW Player Ad event handler class
        new JWAdEventHandler(mPlayerView, outputTextView, scrollView);

        // Setup JWPlayer
        setupJWPlayer();

        // TODO:
        //  1- get current HTML from webview ,
        //  2- add listener for both JS & Android,
        //  3- reload the url to webview

        webview = new WebView(mPlayerView.getContext());
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "addSettingsListener");
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

//        WebChromeClient client = new WebChromeClient();
//        webview.setWebChromeClient(client);
//        client.onRequestFocus(webview);
        webview.loadUrl(
                "javascript:(function(){" +
                        "var isClicked = false;" +
                        "jwplayer().on(\"time\", () => { " +
                        "   document.getElementsByClassName(\"jw-icon-settings\")[0].addEventListener(\"click\", ()=> { " +
                        "           isClicked = true; " +
                        "       })" +
                        "});" +
                        "jwplayer().on(\"beforePlay\", () => { " +
                        "           isClicked = false; " +
                        "});" +
                        "})()"
        );

//        MyTouch touch = new MyTouch(getWindow().getContext());
//        webview.setOnTouchListener(touch);
//        mPlayerView.setOnTouchListener(touch);

        mPlayerView.addOnControlBarVisibilityListener(this);

        // CastContext is lazily initialized when the CastContext.getSharedInstance() is called.
        mCastContext = CastContext.getSharedInstance(this);
    }

    /**
     * Setup JW Player
     * <p>
     * 1 - PlayerConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlayerConfig.Builder.html
     * 2 - LogoConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/LogoConfig.html
     * 3 - PlaybackRateConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/PlaybackRateConfig.html
     * 4 - CaptionsConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/CaptionsConfig.html
     * 5 - RelatedConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/RelatedConfig.html
     * 6 - SharingConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SharingConfig.html
     * 7 - SkinConfig - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/SkinConfig.Builder.html
     * <p>
     * More info about our Player Configuration and other available Configurations:
     * {@link - https://developer.jwplayer.com/sdk/android/reference/com/longtailvideo/jwplayer/configuration/package-summary.html}
     */
    private void setupJWPlayer() {

        List<PlaylistItem> playlistItemList = SamplePlaylist.createPlaylist();
//		List<PlaylistItem> playlistItemList = SamplePlaylist.createMediaSourcePlaylist();

        // Ima Tag Example
        ImaAdvertising imaAdvertising = SampleAds.getImaAd();

        // VAST Tag Example
        Advertising vastAdvertising = SampleAds.getVastAd();

        // VMAP Tag Example
        VMAPAdvertising vmapAdvertising = SampleAds.getVMAP("vast");

        // Skin Config
        SkinConfig skinConfig = new SkinConfig.Builder()
                .url("https://www.host.com/css/mycustomcss.css")
                .name("mycustomcss")
                .build();

        // PlayerConfig
        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlistItemList)
                .autostart(true)
                .preload(true)
                .mute(true)
                .allowCrossProtocolRedirects(true)
//				.skinConfig(skinConfig)
//				.advertising(vastAdvertising)
//				.advertising(imaAdvertising)
//				.advertising(vmapAdvertising)
                .build();

        mPlayerView.setup(config);
    }

    /*
     * In landscape mode, set to fullscreen or if the user clicks the fullscreen button
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
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

    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreenEvent true if the player is fullscreen
     */
    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreenEvent.getFullscreen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jwplayerview, menu);

        // Register the MediaRouterButton
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_fragment:
                Intent i = new Intent(this, JWPlayerFragmentExample.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onControlBarVisibilityChanged(ControlBarVisibilityEvent controlBarVisibilityEvent) {
        String script1 = "javascript:(function(){" +
                "var isClicked = false;" +
                "jwplayer().on(\"time\", () => { " +
                "   document.getElementsByClassName(\"jw-icon-settings\")[0].addEventListener(\"click\", ()=> { " +
                "           isClicked = true; " +
                "       });" +
                "});" +
                "jwplayer().on(\"beforePlay\", () => { " +
                "           isClicked = false; " +
                "});" +
                "})()";
        String script = "javascript:window";
            String script2 = "javascript:document.getElementsByClassName('jw-settings-menu')[0].hidden";
//            String script = "javascript:document.getElementsByClassName('jw-settings-menu');";
//            String script = "javascript:(function(){document.getElementsByClassName('jw-settings-menu')[0].hidden;})()";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("HYUNJOO", "isClicked = " + value);
                }
            });
        }
    }

    private class MyJavaScriptInterface {

        @JavascriptInterface
        public void addSettingsListener() {
//            String settingsListener = "var isClicked = false; " +
//                    "jwplayer().on(\"time\", ()=> { document.getElementsByClassName(\"jw-icon-settings\")[0].addEventListener(\"click\", ()=> { isClicked = true; console.log(\"hey there\");})});";
            String start = "javascript:( function() {" +
                    "var isClicked = false;" +
                    " document.getElementsByClassName(\"jw-icon-settings\")[0].addEventListener(\"click\", () => { " +
                    " isClicked = true; " +
                    "console.log(\"hey there\");" +
                    "});" +
                    "})()";
        }

//        @JavascriptInterface
//        public String isShowing(){
//            return "document.getElementsByClassName('jw-settings-menu')[0].hidden";
//        }

    }

    class MyTouch extends ViewGroup implements View.OnTouchListener {

        private Context mContext;

        public MyTouch(Context context) {
            super(context);
            Log.i("HYUNJOO", "MyTouch ");
        }

        public MyTouch(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyTouch(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {

            WebView.HitTestResult hr = webview.getHitTestResult();

            Log.i("HYUNJOO", "onInterceptTouchEvent - getExtra = " + hr.getExtra() + " Type= " + hr.getType());


            return super.onInterceptTouchEvent(ev);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }
}
