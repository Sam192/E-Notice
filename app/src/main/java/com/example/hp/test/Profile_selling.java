package com.example.hp.test;


import com.google.firebase.database.Exclude;

public class Profile_selling {
    private String Price,Discription,imageAddress,uid,key;
  //  private boolean permission;
    public Profile_selling() {
    }

    public Profile_selling(String price, String discription, String imageAddress,String uid) {
        this.Price = price;
        this.Discription = discription;
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
