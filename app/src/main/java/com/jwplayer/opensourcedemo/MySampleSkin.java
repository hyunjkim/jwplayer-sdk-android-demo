package com.jwplayer.opensourcedemo;

import com.longtailvideo.jwplayer.configuration.SkinConfig;

class MySampleSkin {

    static SkinConfig getSkinConfig() {
        return new SkinConfig.Builder()
                .url("https://s3.amazonaws.com/qa.jwplayer.com/~hyunjoo/css/showcontrolsalways.css")
                .name("showcontrolsalways")
                .build();

    }
}
