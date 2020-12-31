package com.example.hp.test;

import com.google.firebase.database.Exclude;

public class notice_retrive {
    private String topic,saveCurrentTime,saveCurrentDate,key;

    public notice_retrive() {
    }

    public notice_retrive(String topic, String saveCurrentTime, String saveCurrentDate) {
        this.topic = topic;
        this.saveCurrentTime = saveCurrentTime;
        this.saveCurrentDate = saveCurrentDate;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }
}
