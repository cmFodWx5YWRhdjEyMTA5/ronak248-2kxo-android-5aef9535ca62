package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham Agarwal on 07/11/16.
 */
public class UserResult {

    @SerializedName("friendshipstate")
    @Expose
    private Integer friendshipstate=0;
    @SerializedName("friendshipid")
    @Expose
    private Integer friendshipid;
    @SerializedName("count")
    @Expose
    private Integer count;
    /*  @SerializedName("posts")
      @Expose
      private List<Post> posts = new ArrayList<Post>();*/
    @SerializedName("streampost")
    @Expose
    private ArrayList<Streampost> streampost = new ArrayList<>();
    @SerializedName("mediapost")
    @Expose
    private ArrayList<Mediapost> mediapost = new ArrayList<>();
    @SerializedName("itemdetails")
    @Expose
    private ArrayList<Itemdetail> itemdetails = new ArrayList<>();

    @SerializedName("friendscount")
    @Expose
    private Integer friendscount;
    @SerializedName("itemcount")
    @Expose
    private Integer itemcount=0;

    public ArrayList<Streampost> getStreampost() {
        return streampost;
    }

    /**
     * @param streampost The streampost
     */
    public void setStreampost(ArrayList<Streampost> streampost) {
        this.streampost = streampost;
    }

    /**
     * @return The mediapost
     */
    public ArrayList<Mediapost> getMediapost() {
        return mediapost;
    }

    /**
     * @param mediapost The mediapost
     */
    public void setMediapost(ArrayList<Mediapost> mediapost) {
        this.mediapost = mediapost;
    }

    /**
     * @return The itemdetails
     */
    public ArrayList<Itemdetail> getItemdetails() {
        return itemdetails;
    }

    /**
     * @param itemdetails The itemdetails
     */
    public void setItemdetails(ArrayList<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
    }

    /**
     * @return The friendshipstate
     */
    public Integer getFriendshipstate() {
        return friendshipstate;
    }

    /**
     * @param friendshipstate The friendshipstate
     */
    public void setFriendshipstate(Integer friendshipstate) {
        this.friendshipstate = friendshipstate;
    }

    /**
     * @return The friendshipid
     */
    public Integer getFriendshipid() {
        return friendshipid;
    }

    /**
     * @param friendshipid The friendshipid
     */
    public void setFriendshipid(Integer friendshipid) {
        this.friendshipid = friendshipid;
    }

    /**
     * @return The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The posts
     */
/*    public List<Post> getPosts() {
        return posts;
    }

    *//**
     *
     * @param posts
     * The posts
     *//*
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }*/

    /**
     * @return The friendscount
     */
    public Integer getFriendscount() {
        return friendscount;
    }

    /**
     * @param friendscount The friendscount
     */
    public void setFriendscount(Integer friendscount) {
        this.friendscount = friendscount;
    }

    /**
     * @return The itemcount
     */
    public Integer getItemcount() {
        return itemcount;
    }

    /**
     * @param itemcount The itemcount
     */
    public void setItemcount(Integer itemcount) {
        this.itemcount = itemcount;
    }


}
