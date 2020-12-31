package com.example.hp.test;

import com.google.firebase.database.Exclude;

public class donate_image {
    private String Discription,imageAddress,key;

    public donate_image() {
    }

    public donate_image(String discription, String imageAddress) {
        Discription = discription;
        this.imageAddress = imageAddress;
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
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
