package com.jwplayer.opensourcedemo.pojo;


import java.util.HashMap;
import java.util.Map;

public class Rules {

    private String startOnSeek;
    private Integer timeBetweenAds;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStartOnSeek() {
        return startOnSeek;
    }

    public void setStartOnSeek(String startOnSeek) {
        this.startOnSeek = startOnSeek;
    }

    public Integer getTimeBetweenAds() {
        return timeBetweenAds;
    }

    public void setTimeBetweenAds(Integer timeBetweenAds) {
        this.timeBetweenAds = timeBetweenAds;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
