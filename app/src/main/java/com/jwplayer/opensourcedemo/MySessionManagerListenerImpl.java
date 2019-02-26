package com.jwplayer.opensourcedemo;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManagerListener;

import static com.google.android.gms.cast.CastStatusCodes.NETWORK_ERROR;
import static com.google.android.gms.cast.CastStatusCodes.TIMEOUT;
import static com.google.android.gms.cast.CastStatusCodes.INTERRUPTED;
import static com.google.android.gms.cast.CastStatusCodes.INTERNAL_ERROR;
import static com.google.android.gms.cast.CastStatusCodes.UNKNOWN_ERROR;
import static com.google.android.gms.cast.CastStatusCodes.AUTHENTICATION_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.INVALID_REQUEST;
import static com.google.android.gms.cast.CastStatusCodes.CANCELED;
import static com.google.android.gms.cast.CastStatusCodes.NOT_ALLOWED;
import static com.google.android.gms.cast.CastStatusCodes.APPLICATION_NOT_FOUND;
import static com.google.android.gms.cast.CastStatusCodes.APPLICATION_NOT_RUNNING;
import static com.google.android.gms.cast.CastStatusCodes.MESSAGE_TOO_LARGE;
import static com.google.android.gms.cast.CastStatusCodes.MESSAGE_SEND_BUFFER_TOO_FULL;
import static com.google.android.gms.cast.CastStatusCodes.DEVICE_CONNECTION_SUSPENDED;
import static com.google.android.gms.cast.CastStatusCodes.FAILED;
import static com.google.android.gms.cast.CastStatusCodes.REPLACED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_SERVICE_CREATION_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_SERVICE_DISCONNECTED;
import static com.google.android.gms.cast.CastStatusCodes.ERROR_STOPPING_SERVICE_FAILED;
import static com.google.android.gms.cast.CastStatusCodes.SUCCESS;


/*
* https://developers.google.com/cast/docs/android_sender/integrate#how_session_management_works
* */
public class MySessionManagerListenerImpl implements SessionManagerListener {

    @Override
    public void onSessionStarting(Session session) {
        print("onSessionStarting - category: "+session.getCategory());
        print("onSessionStarting - Session ID: " + session.getSessionId());
        check("onSessionStarting", session);
    }

    @Override
    public void onSessionStarted(Session session, String sessionId) {
        print("onSessionStarted Session ID: " + session.getSessionId() + " Session ID: " + sessionId);
        check("onSessionStarted", session);
    }

    @Override
    public void onSessionStartFailed(Session session, int error) {
        print("onSessionStartFailed Session ID: " + session.getSessionId());
        printError("onSessionStartFailed",error);
        check("onSessionStartFailed", session);
    }

    @Override
    public void onSessionEnding(Session session) {
        print("onSessionEnding Session ID" + session.getSessionId());
        check("onSessionEnding", session);
    }

    @Override
    public void onSessionEnded(Session session, int error) {
        print("onSessionEnded Session ID" + session.getSessionId());
        printError("onSessionEnded", error);
        check("onSessionEnded", session);
    }

    @Override
    public void onSessionResuming(Session session, String sessionId) {
        print("onSessionResuming Session ID: " + session.getSessionId() + " Session ID: " + sessionId);
        check("onSessionResuming", session);
    }

    @Override
    public void onSessionResumed(Session session, boolean wasSuspended) {
        print("onSessionResumed Session ID: " + session.getSessionId() + " wasSuspended: " + wasSuspended);
        check("onSessionResumed", session);
    }

    @Override
    public void onSessionResumeFailed(Session session, int error) {
        print("onSessumeResumeFailed Session ID: " + session.getSessionId() + " error : "+ error);
        printError("onSessionResumeFailed", error);
        check("onSessionResumeFailed", session);
    }

    @Override
    public void onSessionSuspended(Session session, int reason) {
        print("onSessionSuspended Session ID: " + session.getSessionId() + " reason : " + reason);
        check("onSessionSuspended", session);
    }

    private void printError(String output, int error) {

        switch(error){
            case SUCCESS:
                print(output + " ERROR CODE: SUCCESS");
                break;
            case APPLICATION_NOT_FOUND:
                print(output + " ERROR CODE: APPLICATION_NOT_FOUND");
                break;
            case NETWORK_ERROR:
                print(output + " ERROR CODE: NETWORK_ERROR");
                break;
            case TIMEOUT:
                print(output + " ERROR CODE: TIMEOUT");
                break;
            case INTERRUPTED:
                print(output + " ERROR CODE: INTERRUPTED");
                break;
            case INTERNAL_ERROR:
                print(output + " ERROR CODE: INTERNAL_ERROR");
                break;
            case UNKNOWN_ERROR:
                print(output + " ERROR CODE: UNKNOWN_ERROR");
                break;
            case AUTHENTICATION_FAILED:
                print(output + " ERROR CODE: AUTHENTICATION_FAILED");
                break;
            case INVALID_REQUEST:
                print(output + " ERROR CODE: INVALID_REQUEST");
                break;
            case CANCELED:
                print(output + " ERROR CODE: CANCELED");
                break;
            case NOT_ALLOWED:
                print(output + " ERROR CODE: NOT_ALLOWED");
                break;
            case APPLICATION_NOT_RUNNING:
                print(output + " ERROR CODE: APPLICATION_NOT_RUNNING");
                break;
            case MESSAGE_TOO_LARGE:
                print(output + " ERROR CODE: MESSAGE_TOO_LARGE");
                break;
            case MESSAGE_SEND_BUFFER_TOO_FULL:
                print(output + " ERROR CODE: MESSAGE_SEND_BUFFER_TOO_FULL");
                break;
            case DEVICE_CONNECTION_SUSPENDED:
                print(output + " ERROR CODE: DEVICE_CONNECTION_SUSPENDED");
                break;
            case FAILED:
                print(output + " ERROR CODE: FAILED");
                break;
            case REPLACED:
                print(output + " ERROR CODE: REPLACED");
                break;
            case ERROR_SERVICE_CREATION_FAILED:
                print(output + " ERROR CODE: ERROR_SERVICE_CREATION_FAILED");
                break;
            case ERROR_SERVICE_DISCONNECTED:
                print(output + " ERROR CODE: ERROR_SERVICE_DISCONNECTED");
                break;
            case ERROR_STOPPING_SERVICE_FAILED:
                print(output + " ERROR CODE: ERROR_STOPPING_SERVICE_FAILED");
                break;
        }
    }

    private void check(String s, Session session) {
        if(session.isConnecting()) print(s + " - session is connecting");
        if(session.isConnected()) print(s + " - session is connected");
        if(session.isDisconnecting()) print(s + " - session is disconnecting");
        if(session.isDisconnected()) print(s + " - session is disconnected");
        if(session.isResuming()) print(s + " - session is resuming");
        if(session.isSuspended()) print(s + " - session is suspended");
    }

    private void print(String output){
        String TAG = "MyCast SessionManagerListener";
        Log.i(TAG, "(CAST) " + output);
    }
}
