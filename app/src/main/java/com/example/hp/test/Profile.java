package com.example.hp.test;

public class Profile {
    private String imageAddress;
    private String price;
    private String discription;
    private String uid;

    public Profile(){

    }

    public Profile(String imageAddress, String price, String discription,String uid) {
        this.imageAddress = imageAddress;
        this.price = price;
        this.discription = discription;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
