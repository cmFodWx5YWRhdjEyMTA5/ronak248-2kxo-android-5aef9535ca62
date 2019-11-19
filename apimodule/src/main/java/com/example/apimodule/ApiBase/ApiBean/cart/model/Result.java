package com.example.apimodule.ApiBase.ApiBean.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {

    @SerializedName("userdetails")
    @Expose
    private ArrayList<Userdetail> userdetails = null;
    @SerializedName("Count")
    @Expose
    private int count;

    public ArrayList<Userdetail> getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(ArrayList<Userdetail> userdetails) {
        this.userdetails = userdetails;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}