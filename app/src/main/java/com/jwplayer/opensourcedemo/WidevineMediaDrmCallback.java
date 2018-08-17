package com.jwplayer.opensourcedemo;

import android.annotation.TargetApi;
import android.text.TextUtils;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.longtailvideo.jwplayer.media.drm.MediaDrmCallback;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.android.exoplayer2.util.Util.toByteArray;

@TargetApi(18)
public class WidevineMediaDrmCallback implements MediaDrmCallback {

    private static final String WIDEVINE_LICENSE_SERVER_BASE_URI =
           "https://widevine.licensekeyserver.com";

    private final String defaultUri;

    private final Map<String, String> KEY_REQUEST_PROPERTIES = new HashMap<>();

    public WidevineMediaDrmCallback() {
        defaultUri = WIDEVINE_LICENSE_SERVER_BASE_URI;
    }

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws IOException {
        String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
        return executePost(url, null, null);
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws IOException {
        String url = request.getDefaultUrl();
        if (TextUtils.isEmpty(url)) {
            url = defaultUri;
        }

        KEY_REQUEST_PROPERTIES.put("name","customdata");
        KEY_REQUEST_PROPERTIES.put("Authorization","PEtleU9TQXV0aGVudGljYXRpb25YTUw+DQogIDxEYXRhPg0KICAgIDxXaWRldmluZVBvbGljeSBmbF9DYW5QbGF5PSJ0cnVlIj48L1dpZGV2aW5lUG9saWN5Pg0KICAgIDxXaWRldmluZUNvbnRlbnRLZXlTcGVjIFRyYWNrVHlwZT0iSEQiPg0KICAgICAgPFNlY3VyaXR5TGV2ZWw+MTwvU2VjdXJpdHlMZXZlbD4NCiAgICA8L1dpZGV2aW5lQ29udGVudEtleVNwZWM+DQogICAgPFBsYXk+DQogICAgICA8RW5hYmxlcnM+DQogICAgICAgIDxJZD43ODY2MjdEOC1DMkE2LTQ0QkUtOEY4OC0wOEFFMjU1QjAxQTc8L0lkPg0KICAgICAgPC9FbmFibGVycz4NCiAgICA8L1BsYXk+DQogICAgPEdlbmVyYXRpb25UaW1lPjIwMTUtMDEtMDYgMTk6NDE6MzUuMDAwPC9HZW5lcmF0aW9uVGltZT4NCiAgICA8RXhwaXJhdGlvblRpbWU+MjAyNS0wMS0wNiAxOTo0MTozNS4wMDA8L0V4cGlyYXRpb25UaW1lPg0KICAgIDxVbmlxdWVJZD43Y2Q4MzdiOTE1YjBiMzVjMjcwZjU3YmNiZGJlYzZhZTwvVW5pcXVlSWQ+DQogICAgPFJTQVB1YktleUlkPmRmNDA4NzkyNjdmYTJmYWU4MThhOWEzYmE3YTcwZWIyPC9SU0FQdWJLZXlJZD4NCiAgICA8UlNBUHViS2V5SWQ+ZGY0MDg3OTI2N2ZhMmZhZTgxOGE5YTNiYTdhNzBlYjI8L1JTQVB1YktleUlkPg0KICA8L0RhdGE+DQogIDxTaWduYXR1cmU+WG8vOFZTNk40TDRvK3JtaVU3Q2FYYkNMbjJZN1hpL2VoT1BXMjNSUHJEendaNmFONzVucXNRUkJiWHlpdWRGdzlSQTBzTFdtU3pRRHZRRzR5R0tJWmxUd3N1elF2TmR6QnZQcmQ0L0xqYW1FZ3IyY1F2ZzBqNm1JeWJTQU5tT3loOGRyTHRaeFQ2Z2Y3Umx6b29GaUpHeFJZQmFPQms5N002eUxKMHZBUHROZWdQMWVEMFpNQXVlbVEwK0s2aEdXem5nR2VEbW1QazhhbjZjejg2MzA1YlhXdUptM1Y5SEFUY1JHSERZUW9UWmFXUDB3bm9DbjNLVHArSHNrclFpWXhPTXlQY1BSSlhISEZubURwRjlzeVBxWGhDUjFBQUZTYVBPNm5GYmxVeEQwRHdCMC9DSG1uL1lpTTFSeHp5eUg1N0RWMXpYZVgwV1VpV2dDSGhVZ3JnPT08L1NpZ25hdHVyZT4NCjwvS2V5T1NBdXRoZW50aWNhdGlvblhNTD4=");

        return executePost(url, request.getData(), KEY_REQUEST_PROPERTIES);
    }

    /**
     * Executes a post request using {@link HttpURLConnection}.
     *
     * @param url               The request URL.
     * @param data              The request body, or null.
     * @param requestProperties Request properties, or null.
     * @return The response body.
     * @throws IOException If an error occurred making the request.
     */
    public static byte[] executePost(String url, byte[] data, Map<String, String> requestProperties)
            throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(data != null);
            urlConnection.setDoInput(true);
            if (requestProperties != null) {
                for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                    urlConnection.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
                }
            }
            // Write the request body, if there is one.
            if (data != null) {
                OutputStream out = urlConnection.getOutputStream();
                try {
                    out.write(data);
                } finally {
                    out.close();
                }
            }
            // Read and return the response body.
            InputStream inputStream = urlConnection.getInputStream();
            try {
                return toByteArray(inputStream);
            } finally {
                inputStream.close();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
