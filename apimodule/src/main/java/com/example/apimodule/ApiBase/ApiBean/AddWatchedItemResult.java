package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 01/12/16.
 */

public class AddWatchedItemResult {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("myid")
    @Expose
    private String myid;
    @SerializedName("id")
    @Expose
    private int id;

    /**
     *
     * @return
     * The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     *
     * @param uid
     * The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @return
     * The itemid
     */
    public String getItemid() {
        return itemid;
    }

    /**
     *
     * @param itemid
     * The itemid
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     *
     * @return
     * The action
     */
    public String getAction() {
        return action;
    }

    /**
     *
     * @param action
     * The action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     *
     * @return
     * The myid
     */
    public String getMyid() {
        return myid;
    }

    /**
     *
     * @param myid
     * The myid
     */
    public void setMyid(String myid) {
        this.myid = myid;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AddWatchedItemResult{" +
                "uid='" + uid + '\'' +
                ", itemid='" + itemid + '\'' +
                ", action='" + action + '\'' +
                ", myid='" + myid + '\'' +
                ", id=" + id +
                '}';
    }
}
