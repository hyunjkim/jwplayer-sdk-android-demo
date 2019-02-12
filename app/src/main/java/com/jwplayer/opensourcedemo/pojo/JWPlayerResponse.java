package com.jwplayer.opensourcedemo.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JWPlayerResponse {

    private Rules rules;
    private String adscheduleid;
    private String client;
    private String vpaidmode;
    private List<Schedule> schedule = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public String getAdscheduleid() {
        return adscheduleid;
    }

    public void setAdscheduleid(String adscheduleid) {
        this.adscheduleid = adscheduleid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVpaidmode() {
        return vpaidmode;
    }

    public void setVpaidmode(String vpaidmode) {
        this.vpaidmode = vpaidmode;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


