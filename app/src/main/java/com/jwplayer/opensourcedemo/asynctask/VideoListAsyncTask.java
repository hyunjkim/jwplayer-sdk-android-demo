package com.jwplayer.opensourcedemo.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.jwplayer.opensourcedemo.listener.MyThreadListener;
import com.jwplayer.opensourcedemo.samples.SamplePlaylist;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class VideoListAsyncTask extends AsyncTask<String, String, List<PlaylistItem>> {

    private final String API_FORMAT = "json";

    // TODO: Add your API KEY and API Secret
    private final String API_KEY = "API_KEY";
    private final String API_SECRET = "API_SECRET";

    private MyThreadListener threadListener;

    public VideoListAsyncTask(MyThreadListener threadListener) {
        super();
        this.threadListener = threadListener;
    }

    @Override
    protected List<PlaylistItem> doInBackground(String... strings) {

        if (!isCancelled()) {

            String host = "https://api.jwplatform.com/v1/videos/list?";
            String api_nonce = UUID.randomUUID().toString();
            String api_timestamp = String.valueOf(System.currentTimeMillis() / 1000);

            String api_signature = String.format(
                    "api_format=%s" +
                            "&api_key=%s" +
                            "&api_nonce=%s" +
                            "&api_timestamp=%s" + "%s", API_FORMAT, API_KEY, api_nonce, api_timestamp, API_SECRET);

            String queries = String.format(
                    "api_format=%s" +
                            "&api_nonce=%s" +
                            "&api_timestamp=%s", API_FORMAT, api_nonce, api_timestamp);

            String base = host + queries + "&api_signature=" + getEncodedSHA1HexSignature(api_signature) + "&api_key=" + API_KEY;

            try {
                byte[] response = Util.executePost(base);
                String strResponse = new String(response);

                JSONArray jsonArray = new JSONObject(strResponse).getJSONArray("videos");

                for (int each = 0; each < jsonArray.length(); each++) {

                    String mediaid = jsonArray.getJSONObject(each).getString("key");
                    String sourceurl = jsonArray.getJSONObject(each).getString("sourceurl");

                    PlaylistItem item;

                    /// Sample response -  Video List: {"status": "ok", "videos": [{"status": "ready", "updated": 123123123, "expires_date": null, "description": "", "title": "Sample Stock Video", "views": 0, "tags": "sample", "sourceformat": null, "mediatype": "video", "upload_session_id": null, "custom": {}, "duration": "9.00", "sourceurl": null, "link": "", "author": null, "key": "123123123", "error": null, "date": 123123123, "md5": lalalalaalalalalalalala", "sourcetype": "file", "size": "123123123"}, {"status": "ready", "updated": 123123123, "expires_date": null, "description": "", "title": "[VOD] Twice - Feel Special", "views": 0, "tags": "vod", "sourceformat": null, "mediatype": "video", "upload_session_id": null, "custom": {}, "duration": "1934.50", "sourceurl": null, "link": "", "author": null, "key": "123123123", "error": null, "date": 1574202486, "md5": "lalalalaalalalalalalala", "sourcetype": "file", "size": "123123123"}, {"status": "ready", "updated": 1575402507, "expires_date": null, "description": "", "title": "Dog", "views": 0, "tags": "pets", "sourceformat": "mp4", "mediatype": "video", "upload_session_id": null, "custom": {}, "duration": "0.00", "sourceurl": "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4", "link": "", "author": null, "key": "stu3SFNq", "error": null, "date": 123123123, "md5": "lalalalaalalalalalaalal", "sourcetype": "url", "size": "0"}], "rate_limit": {"reset": 1578589080, "limit": 30, "remaining": 28}, "limit": 50, "offset": 0, "total": 2}

                    if (!sourceurl.equals("null")) {
                        item = new PlaylistItem(sourceurl);
                    } else {
                        String file = String.format("https://cdn.jwplayer.com/manifests/%s.m3u8", mediaid);
                        print("Video file: " + file);
                        item = new PlaylistItem(file);
                    }
                    SamplePlaylist.mPlaylist.add(item);
                }

                return SamplePlaylist.mPlaylist;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                print("ERROR CATCH Localized Message: " + e.getLocalizedMessage());
                print("ERROR CATCH Get Stack Trace : " + Arrays.toString(e.getStackTrace()));
                print("ERROR CATCH Get Message: " + e.getMessage());
            }
        }
        return null;
    }

    private String getEncodedSHA1HexSignature(String signature) {

        String api_signature = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = signature.getBytes("UTF-8");
            md.update(bytes, 0, bytes.length);
            byte[] updatedMD = md.digest();
            StringBuilder hex = new StringBuilder(updatedMD.length * 2);

            for (byte aByte : updatedMD) {
                if (((int) aByte & 0xff) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toString((int) aByte & 0xff, 16));
            }

            api_signature = hex.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return api_signature;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        threadListener.beforeExecute();
    }

    @Override
    protected void onPostExecute(List<PlaylistItem> mediaids) {
        super.onPostExecute(mediaids);
        print("onPostExecute - setupJWPlayer");
        threadListener.clear();
        threadListener.setupJWPlayer();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        print("onProgressUpdate - countdown: " + values[0]);
        threadListener.countDown(values[0]);
    }

    @Override
    protected void onCancelled(List<PlaylistItem> mediaids) {
        super.onCancelled(mediaids);
        threadListener = null;
        print("oncancelled List<PlaylistItem> mediaids");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        threadListener = null;
        print("oncancelled VideoListAysncTask");
    }

    private void print(String s) {
        Log.i("SAMPLEPLAYLIST", " - HYUNJOO - " + s + "\r\n");
    }
}
