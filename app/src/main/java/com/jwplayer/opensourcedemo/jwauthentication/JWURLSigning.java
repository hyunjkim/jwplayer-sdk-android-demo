package com.jwplayer.opensourcedemo.jwauthentication;

import android.util.Log;

import com.jwplayer.opensourcedemo.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class JWURLSigning {

    //    TODO: ADD YOUR KEY AND SECRET
    private final String COLON = ":";
    private final String API_SECRET = BuildConfig.ApiSecret;
    private String url_signature;
    private long time = 0L;

    /*
     * For more info: https://developer.android.com/reference/java/security/MessageDigest
     * https://mobikul.com/converting-string-md5-hashes-android/
     * */
    public void createSignature(String videoPath, long seconds) {

        long milliseconds = TimeUnit.SECONDS.toMillis(seconds);

        time = time == 0L? milliseconds : (time+=milliseconds);

        print("Expire in : " + time + " seconds \r\n");

        print("Current time: " + (System.currentTimeMillis()/1000L) +"\r\n");

        long api_expireTime = (System.currentTimeMillis() / 1000L) + time;

        print("Expire time: " + api_expireTime + "\r\n");

        String md5_signature = videoPath + COLON + api_expireTime + COLON + API_SECRET;


        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");

            byte[] messageDigestBytes = md5_signature.getBytes();

            mDigest.update(messageDigestBytes);

            md5_signature = createHexString(mDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // An authenticated API call will look like this:
        url_signature = "/" + videoPath +
                        "?exp=" + api_expireTime+
                        "&sig=" + md5_signature;

        print("URL signature: "+ url_signature);
    }

    /*
    * Second step: https://mobikul.com/converting-string-md5-hashes-android/
    * */
    private String createHexString(byte[] messageDigest){

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (byte aMessageDigest : messageDigest)
            hexString.append(Integer.toHexString(0xFF & aMessageDigest));

        return hexString.toString();
    }

    public String getApiUrlSignature() {
        return url_signature;
    }

    private void print(String s){
        Log.i("JWEVENT", "(URL-SIGNING) " + s);
    }

}
