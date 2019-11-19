package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 17/10/16.
 */
public class Streampost implements Parcelable {
    @SerializedName("mypost")
    @Expose
    private int mypost;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("posttype")
    @Expose
    private int posttype;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("userid")
    @Expose
    private int userid;
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
    @SerializedName("commentcount")
    @Expose
    private int commentcount;
    @SerializedName("likecount")
    @Expose
    private int likecount;
    @SerializedName("islike")
    @Expose
    private int islike;
    @SerializedName("iscomment")
    @Expose
    private int iscomment;
    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("media_url")
    @Expose
    private String media_url;


    @SerializedName("media_thumb")
    @Expose
    private String media_thumb;

    @SerializedName("boost_url")
    @Expose
    private String boost_url;

    public String getBoost_url() {
        return boost_url;
    }

    public void setBoost_url(String boost_url) {
        this.boost_url = boost_url;
    }

    public String getMedia_thumb() {
        return media_thumb;
    }

    public void setMedia_thumb(String media_thumb) {
        this.media_thumb = media_thumb;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public static Creator<Streampost> getCREATOR() {
        return CREATOR;
    }

    public int getMypost() {
        return mypost;
    }

    public int getId() {
        return id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public int getPosttype() {
        return posttype;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsername() {
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

    public int getCommentcount() {
        return commentcount;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public Streampost() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mypost);
        dest.writeInt(this.id);
        dest.writeString(this.postTitle);
        dest.writeInt(this.posttype);
        dest.writeString(this.updatedDate);
        dest.writeInt(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.userphoto);
        dest.writeString(this.userphotothumb);
        dest.writeInt(this.commentcount);
        dest.writeInt(this.likecount);
        dest.writeInt(this.islike);
        dest.writeInt(this.iscomment);
        dest.writeString(this.media_type);
        dest.writeString(this.media_url);
        dest.writeString(this.media_thumb);
    }

    protected Streampost(Parcel in) {
        this.mypost = in.readInt();
        this.id = in.readInt();
        this.postTitle = in.readString();
        this.posttype = in.readInt();
        this.updatedDate = in.readString();
        this.userid = in.readInt();
        this.username = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.userphoto = in.readString();
        this.userphotothumb = in.readString();
        this.commentcount = in.readInt();
        this.likecount = in.readInt();
        this.islike = in.readInt();
        this.iscomment = in.readInt();
        this.media_type = in.readString();
        this.media_url = in.readString();
        this.media_thumb = in.readString();
    }

    public static final Creator<Streampost> CREATOR = new Creator<Streampost>() {
        @Override
        public Streampost createFromParcel(Parcel source) {
            return new Streampost(source);
        }

        @Override
        public Streampost[] newArray(int size) {
            return new Streampost[size];
        }
    };
}