package com.jwplayer.opensourcedemo.handlers;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.jwplayer.opensourcedemo.listeners.CustomSessionListener;
import com.jwplayer.opensourcedemo.listeners.ValidateMenuListener;
import com.jwplayer.opensourcedemo.myutility.Logger;

public class CustomSessionHandler implements SessionManagerListener<CastSession> {

    private CastSession mCastSession;

    private ValidateMenuListener validateMenuListener;

    private CustomSessionListener mCastSessionListener;

    public CustomSessionHandler(ValidateMenuListener validateMenuListener, CustomSessionListener mCastSessionListener, CastSession session) {
        this.validateMenuListener = validateMenuListener;
        this.mCastSessionListener = mCastSessionListener;
        mCastSession = session;
    }

    /**
     * Credits to Google: https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/VideoBrowserActivity.java#L56
     */
    @Override
    public void onSessionEnded(CastSession session, int error) {
        if (session == mCastSession) {
            mCastSession = null;
            mCastSessionListener.castSessionInformation(mCastSession);

        }
        validateMenuListener.callInvalidateOptionMenu(); // displays the options on the menu
        Logger.log("onSessionEnded: " + session.getApplicationStatus());
    }

    @Override
    public void onSessionResumed(CastSession session, boolean wasSuspended) {
        mCastSession = session;
        mCastSessionListener.castSessionInformation(mCastSession);
        validateMenuListener.callInvalidateOptionMenu(); // displays the options on the menu
        Logger.log("onSessionResumed: " + session.getApplicationStatus());
    }

    @Override
    public void onSessionStarted(CastSession session, String sessionId) {
        mCastSession = session;
        mCastSessionListener.castSessionInformation(mCastSession);
        validateMenuListener.callInvalidateOptionMenu(); // displays the options on the menu
        Logger.log("onSessionStarted: " + session.getApplicationStatus());
    }

    @Override
    public void onSessionStarting(CastSession castSession) {
        Logger.logCastSession("onSessionStarting: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }

    @Override
    public void onSessionStartFailed(CastSession castSession, int i) {
        Logger.logCastSession("onSessionStartFailed: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }

    @Override
    public void onSessionEnding(CastSession castSession) {
        Logger.logCastSession("onSessionEnding: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }

    @Override
    public void onSessionResuming(CastSession castSession, String s) {
        Logger.logCastSession("onSessionResuming: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }

    @Override
    public void onSessionResumeFailed(CastSession castSession, int i) {
        Logger.logCastSession("onSessionResumeFailed: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }

    @Override
    public void onSessionSuspended(CastSession castSession, int i) {
        Logger.logCastSession("onSessionSuspended: "+ castSession.getApplicationMetadata().toString()+"\r\n Application Status: "+castSession.getApplicationStatus());
    }
}
