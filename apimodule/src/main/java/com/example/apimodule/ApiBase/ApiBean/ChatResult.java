package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 10/01/17.
 */
public class ChatResult {
    @SerializedName("messages")
    @Expose
    private ArrayList<Message> messages = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("otheruser")
    @Expose
    private Otheruser otheruser;
    @SerializedName("item")
    @Expose
    private Item item;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Integer getCount() {
        return count;
    }

    public Otheruser getOtheruser() {
        return otheruser;
    }

    public Item getItem() {
        return item;
    }

}
