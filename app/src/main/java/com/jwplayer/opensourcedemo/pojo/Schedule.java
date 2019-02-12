package com.jwplayer.opensourcedemo.pojo;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    private Integer offset;
    private String type;
    private String tag;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}