package com.example.hp.test;

public class upload_donate {

    private String Discription,imageAddress,uid;
    public upload_donate() {
    }
    public upload_donate(String discription, String imageAddress,String uid) {
        Discription = discription;
        this.imageAddress = imageAddress;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
