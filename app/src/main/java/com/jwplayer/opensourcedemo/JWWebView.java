package com.jwplayer.opensourcedemo;

import android.view.View;
import android.webkit.WebView;

import com.longtailvideo.jwplayer.JWPlayerView;

class JWWebView {
    static WebView getWebView(JWPlayerView mPlayerView) {
        WebView webView = null;
        for (int i = 0 ; i < mPlayerView.getChildCount() ; i++) {
            View v = mPlayerView.getChildAt(i);
            if (v instanceof WebView) {
                webView = (WebView)v;
                break;
            }
        }
        if (webView != null) {
            webView.getSettings().setUserAgentString("Android");
        }
        return webView;
    }
}
