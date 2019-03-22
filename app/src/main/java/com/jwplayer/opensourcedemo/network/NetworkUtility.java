package com.jwplayer.opensourcedemo.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static com.jwplayer.opensourcedemo.JWPlayerViewExample.sPref;

public class NetworkUtility extends BroadcastReceiver {

    private static final String TAG = "JWEVENTHANDLER";
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    public static boolean refreshDisplay = true;
    private static ConnectivityManager conn;
    private static NetworkInfo networkInfo;
    private static Network network;

    public static boolean networkCheck(Context context) {
        conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = conn.getActiveNetworkInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network = conn.getActiveNetwork();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkCapabilities networkCapabilities = conn.getNetworkCapabilities(network);

            int downloadStreamBandwidth = networkCapabilities.getLinkDownstreamBandwidthKbps();
            int uploadStreamBandwidth = networkCapabilities.getLinkDownstreamBandwidthKbps();

            Log.i(TAG,"(NETWORK) downloadStreamBandwidth: " +  downloadStreamBandwidth);
            Log.i(TAG,"(NETWORK) uploadStreamBandwidth: " +  uploadStreamBandwidth);

            for (Network network : conn.getAllNetworks()) {
                NetworkInfo networkInfo = conn.getNetworkInfo(network);
                switch(networkInfo.getType()){
                    case TYPE_WIFI:
                        Log.i(TAG, "(NETWORK) WIFI CONNECTED: " + networkInfo.isConnected());
                        break;
                    case TYPE_MOBILE:
                        Log.i(TAG, "(NETWORK) Mobile CONNECTED: " + networkInfo.isConnected());
                        break;

                }
            }
        }


        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conn.getActiveNetworkInfo();

        // Checks the user prefs and the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
        if (WIFI.equals(sPref) && networkInfo != null
                && networkInfo.getType() == TYPE_WIFI) {
            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.
            refreshDisplay = true;
            Toast.makeText(context, "WIFI CONNECTED", Toast.LENGTH_SHORT).show();

            // If the setting is ANY network and there is a network connection
            // (which by process of elimination would be mobile), sets refreshDisplay to true.
        } else if (ANY.equals(sPref) && networkInfo != null) {
            refreshDisplay = true;

            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
        } else {
            refreshDisplay = false;
            Toast.makeText(context, "CONNECTION LOST", Toast.LENGTH_SHORT).show();
        }
    }

}