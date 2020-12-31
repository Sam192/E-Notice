package com.example.hp.test;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class notice {
    private String topic,imageAddress,pdf;
    private String discription,saveCurrentTime,saveCurrentDate,mKey;


    public notice() {
    }

    public notice(String topic, String discription,String imageAddress,String pdf, String saveCurrentTime, String saveCurrentDate) {
        this.topic = topic;
        this.discription = discription;
        this.imageAddress=imageAddress;
        this.pdf=pdf;
        this.saveCurrentDate=saveCurrentDate;
        this.saveCurrentTime=saveCurrentTime;
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

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getTopic() {
        return topic;
    }

    public String getDiscription() {
        return discription;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
