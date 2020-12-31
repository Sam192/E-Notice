package com.example.hp.test;

import com.google.firebase.database.Exclude;

public class mypost_user {

    private String Price,Discription,imageAddress,uid,key;

    public mypost_user() {
    }

    public mypost_user(String price, String discription, String imageAddress,String uid) {
        Price = price;
        Discription = discription;
        this.imageAddress = imageAddress;
        this.uid=uid;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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
