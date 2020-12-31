package com.example.hp.test;

public class maintainence_class {
    private String imageAddress;
    private String room;
    private String discription;
    private String uid,saveCurrentTime,saveCurrentDate,mKey;

    public maintainence_class() {
    }



    public maintainence_class(String imageAddress, String room, String discription, String uid,String saveCurrentTime, String saveCurrentDate, String mKey) {
        this.imageAddress = imageAddress;
        this.room = room;
        this.discription = discription;
        this.uid = uid;
        this.saveCurrentTime = saveCurrentTime;
        this.saveCurrentDate = saveCurrentDate;
        this.mKey = mKey;
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

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
