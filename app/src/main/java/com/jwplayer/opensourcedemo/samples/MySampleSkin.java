package com.jwplayer.opensourcedemo.samples;

import com.longtailvideo.jwplayer.configuration.SkinConfig;

public class MySampleSkin {

    public static SkinConfig getSkinConfig() {
        return new SkinConfig.Builder()
                .url("https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/android/css/removesettings.css")
                .name("removesettings")
                .build();

    }
}
