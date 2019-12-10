package com.jwplayer.opensourcedemo.data;

import com.longtailvideo.jwplayer.configuration.PlayerConfig;

public class JWPlayerConfig {

    public static PlayerConfig mconfig ;

    public static PlayerConfig getConfig() {
        return mconfig ;
    }

    public static void setConfig(PlayerConfig config) {
        mconfig = config;
    }
}
