package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.MediaType;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SamplePlaylist {

    /*
     * Create a Playlist Example
     * */
    public static List<PlaylistItem> createPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] playlist = {
                "https://cdn.jwplayer.com/manifests/RDn7eg0o.m3u8",
                "https://content.jwplatform.com/videos/RDn7eg0o-cIp6U8lV.mp4",
                "https://cdn.jwplayer.com/manifests/8TbJTFy5.m3u8",
                "https://cdn.jwplayer.com/manifests/RDn7eg0o.m3u8",
        };

        for (String each : playlist) {

            String[] array;

            if (each.endsWith(".m3u8")) {
                array = each.split("/manifests/");
            } else {
                array = each.split("/videos/");
            }

            PlaylistItem item = new PlaylistItem.Builder()
                    .file(each)
                    .title(array[1])
                    .build();

            playlistItemList.add(item);
        }

        return playlistItemList;
    }


    /*
     * Create a Audio Playlist Example
     * */
    public static List<PlaylistItem> createAudioPlaylist() {
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] audios = {
                "1g8jjku3",
                "RDn7eg0o",
                "yp34SRmf",
                "1b02B03R"
        };

        for (int index = 0; index < audios.length; index++) {

            String mediaId = audios[index];

            String audio = "https://cdn.jwplayer.com/videos/" + mediaId + "-g8UjtXW6.m4a";

            PlaylistItem item = new PlaylistItem.Builder()
                    .file(audio)
                    .title(mediaId)
                    .image("https://cdn.jwplayer.com/v2/media/"+mediaId+"/poster.jpg")
                    .build();
            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    /**
     * MediaSource with only Audio Playlist Example
     * <p>
     * Adaptive Streaming for more info: https://support.jwplayer.com/articles/adaptive-streaming-reference
     */
    public static List<PlaylistItem> createAudioMediaSourcePlaylist() {

        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String[] audios = {
                "1g8jjku3",
                "RDn7eg0o",
                "yp34SRmf",
                "1b02B03R"
        };

        for (int index = 0; index < audios.length; index++) {

            String mediaId = audios[index];

            String audio = "https://cdn.jwplayer.com/videos/" + mediaId + "-g8UjtXW6.m4a";

            MediaSource ms = new MediaSource.Builder()
                    .file(audio)
                    .label("AAC Audio " + mediaId)
                    .type(MediaType.AAC)
                    .build();

            List<MediaSource> mediaSourceList = Collections.singletonList(ms);

            PlaylistItem item = new PlaylistItem.Builder()
                    .sources(mediaSourceList)
                    .image("https://cdn.jwplayer.com/v2/media/"+mediaId+"/poster.jpg")
                    .build();

            playlistItemList.add(item);
        }

        return playlistItemList;
    }

    /**
     * MediaSource Playlist Example
     * <p>
     * Adaptive Streaming for more info: https://support.jwplayer.com/articles/adaptive-streaming-reference
     */
    public static List<PlaylistItem> createMediaSourcePlaylist() {
        List<MediaSource> mediaSourceList = new ArrayList<>();
        List<PlaylistItem> playlistItemList = new ArrayList<>();

        String hls = "https://cdn.jwplayer.com/manifests/RDn7eg0o.m3u8";
        String mp4 = "https://cdn.jwplayer.com/videos/RDn7eg0o-Zq6530MP.mp4";
        String mp4_1 = "https://cdn.jwplayer.com/videos/RDn7eg0o-TNpruJId.mp4";
        String mp4_2 = "https://cdn.jwplayer.com/videos/RDn7eg0o-FctPAkow.mp4";

        MediaSource mshls = new MediaSource.Builder()
                .file(hls)
                .label("HLS")
                .type(MediaType.HLS)
                .build();
        MediaSource ms = new MediaSource.Builder()
                .file(mp4)
                .label("180px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms1 = new MediaSource.Builder()
                .file(mp4_1)
                .label("480px")
                .type(MediaType.MP4)
                .build();
        MediaSource ms2 = new MediaSource.Builder()
                .file(mp4_2)
                .label("1280px")
                .type(MediaType.MP4)
                .build();

        mediaSourceList.add(ms);
        mediaSourceList.add(ms1);
        mediaSourceList.add(ms2);
        mediaSourceList.add(mshls);

        PlaylistItem item = new PlaylistItem.Builder()
                .sources(mediaSourceList)
                .build();

        playlistItemList.add(item);

        return playlistItemList;
    }

}
