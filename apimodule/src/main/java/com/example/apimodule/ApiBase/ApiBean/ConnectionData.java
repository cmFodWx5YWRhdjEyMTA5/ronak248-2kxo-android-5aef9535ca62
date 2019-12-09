package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionData {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_from")
    @Expose
    private int user_from;

    @SerializedName("user_to")
    @Expose
    private int user_to;

    @SerializedName("action")
    @Expose
    private int action;

    @SerializedName("is_business_block")
    @Expose
    private int is_business_block;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_from() {
        return user_from;
    }

    public void setUser_from(int user_from) {
        this.user_from = user_from;
    }

    public int getUser_to() {
        return user_to;
    }

    public void setUser_to(int user_to) {
        this.user_to = user_to;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getIs_business_block() {
        return is_business_block;
    }

    public void setIs_business_block(int is_business_block) {
        this.is_business_block = is_business_block;
    }
}
