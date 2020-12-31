package com.example.hp.test;

import com.google.firebase.database.Exclude;

public class event_class {
    private String topic,imageAddress,pdf,email;
    private String discription,saveCurrentTime,saveCurrentDate,mKey;

    public event_class() {
    }

    public event_class(String topic, String imageAddress, String pdf, String email, String discription, String saveCurrentTime, String saveCurrentDate, String mKey) {
        this.topic = topic;
        this.imageAddress = imageAddress;
        this.pdf = pdf;
        this.email = email;
        this.discription = discription;
        this.saveCurrentTime = saveCurrentTime;
        this.saveCurrentDate = saveCurrentDate;
        this.mKey = mKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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
    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
