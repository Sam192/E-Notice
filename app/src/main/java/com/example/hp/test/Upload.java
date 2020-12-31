package com.example.hp.test;

import android.widget.Toast;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl,key;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;

    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
