package com.jwplayer.opensourcedemo.api;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class JWURLSignature {

    //    TODO: ADD YOUR KEY AND SECRET
    private final String TAG = "JWEVENT";
    private final String API_FORMAT = "json";
    private final String API_SECRET = "API_SECRET";
    private final String API_KEY = "API_KEY";
    private String player_url_signature;
    private long time = 0L;

    /**
     * Credits to:
     *
     * First Step -
     * How to create a signature - {@link - https://developer.jwplayer.com/jwplayer/docs/authentication}
     * How to create a SHA-1 Hash - {@link - https://developer.android.com/reference/java/security/MessageDigest}
     */
    public void createSignature(String playerId) {

        long api_nonce = System.currentTimeMillis();
        long api_expireTimeStamp = (System.currentTimeMillis() / 1000L);

        String md5_signature =
                "api_format=" + API_FORMAT +
                        "&api_key=" + API_KEY +
                        "&api_nonce=" + api_nonce +
                        "&api_timestamp=" + api_expireTimeStamp +
                        "&player_key=" + playerId +
                        API_SECRET;

        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");

            byte[] messageDigestBytes = new byte[0];

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                messageDigestBytes = md5_signature.getBytes(StandardCharsets.UTF_8);
            }

            mDigest.update(messageDigestBytes, 0, messageDigestBytes.length);

            md5_signature = createHexString(mDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // An authenticated API call will look like this:
        player_url_signature =
                "?api_format=" + API_FORMAT +
                        "&api_key=" + API_KEY +
                        "&api_nonce=" + api_nonce +
                        "&api_timestamp=" + api_expireTimeStamp +
                        "&player_key=" + playerId +
                        "&api_signature=" + md5_signature +
                        "&api_key=" + API_KEY;

        print("Player URL signature: " + player_url_signature);
    }

    /**
     * Credits to:
     *
     * Second step - {@link - https://mobikul.com/converting-string-md5-hashes-android/}
     * */
    private String createHexString(byte[] messageDigest) {

        StringBuilder hex = new StringBuilder(messageDigest.length * 2);

        for (byte aByte : messageDigest) {
            if (((int) aByte & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString((int) aByte & 0xff, 16));
        }

        return hex.toString();
    }

    public String getPlayerApiUrlSignature() {
        return player_url_signature;
    }

    private void print(String s) {
        Log.i(TAG, "(URL-SIGNING) " + s);
    }

}
