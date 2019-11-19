package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("messageid")
    @Expose
    private String messageid;
    @SerializedName("senderid")
    @Expose
    private String senderid;
    @SerializedName("messagedate")
    @Expose
    private String messagedate;
    @SerializedName("messagetext")
    @Expose
    private String messagetext;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("mediathumb")
    @Expose
    private String mediathumb;
    @SerializedName("mediatype")
    @Expose
    private String mediatype;
    @SerializedName("mediaoriginalname")
    @Expose
    private String mediaoriginalname;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("userphotothumb")
    @Expose
    private String userphotothumb;
    @SerializedName("itemname")
    @Expose
    private String itemname;
    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("itemdescription")
    @Expose
    private String itemdescription;
    @SerializedName("itemprice")
    @Expose
    private String itemprice;
    @SerializedName("itemmediatype")
    @Expose
    private String itemmediatype;
    @SerializedName("itemmedianame")
    @Expose
    private String itemmedianame;
    @SerializedName("itemthumbnail")
    @Expose
    private String itemthumbnail;
    @SerializedName("itemmedia")
    @Expose
    private String itemmedia;

    private boolean isplaying = false;

    public boolean isIsplaying() {
        return isplaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getMessagedate() {
        return messagedate;
    }

    public void setMessagedate(String messagedate) {
        this.messagedate = messagedate;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMediathumb() {
        return mediathumb;
    }

    public void setMediathumb(String mediathumb) {
        this.mediathumb = mediathumb;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getMediaoriginalname() {
        return mediaoriginalname;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        if(username==null)
            username="";
        return username;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemid() {
        return itemid;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public String getItemprice() {
        return itemprice;
    }

    public String getItemmediatype() {
        return itemmediatype;
    }

    public String getItemmedianame() {
        return itemmedianame;
    }

    public String getItemthumbnail() {
        return itemthumbnail;
    }

    public String getItemmedia() {
        return itemmedia;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageid='" + messageid + '\'' +
                ", senderid='" + senderid + '\'' +
                ", messagedate='" + messagedate + '\'' +
                ", messagetext='" + messagetext + '\'' +
                ", media='" + media + '\'' +
                ", mediathumb='" + mediathumb + '\'' +
                ", mediatype='" + mediatype + '\'' +
                ", mediaoriginalname='" + mediaoriginalname + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", userphoto='" + userphoto + '\'' +
                ", userphotothumb='" + userphotothumb + '\'' +
                ", itemname='" + itemname + '\'' +
                ", itemid='" + itemid + '\'' +
                ", itemdescription='" + itemdescription + '\'' +
                ", itemprice='" + itemprice + '\'' +
                ", itemmediatype='" + itemmediatype + '\'' +
                ", itemmedianame='" + itemmedianame + '\'' +
                ", itemthumbnail='" + itemthumbnail + '\'' +
                ", itemmedia='" + itemmedia + '\'' +
                '}';
    }
}