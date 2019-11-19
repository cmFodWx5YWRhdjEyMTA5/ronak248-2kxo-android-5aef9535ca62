package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 17/10/16.
 */
public class Mediapost implements Parcelable{
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

    @SerializedName("post_type")
    @Expose
    private int postTypeNew;

    public int getPostTypeNew() {
        return postTypeNew;
    }

    public void setPostTypeNew(int postTypeNew) {
        this.postTypeNew = postTypeNew;
    }

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
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("media_thumb")
    @Expose
    private String mediaThumb;
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

    @SerializedName("media_id")
    @Expose
    private int mediaId;


    @SerializedName("boost_url")
    @Expose
    private String boost_url;

    public String getBoost_url() {
        return boost_url;
    }

    public void setBoost_url(String boost_url) {
        this.boost_url = boost_url;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    protected Mediapost(Parcel in) {
        mypost = in.readInt();
        id = in.readInt();
        postTitle = in.readString();
        posttype = in.readInt();
        postTypeNew = in.readInt();
        updatedDate = in.readString();
        userid = in.readInt();
        username = in.readString();
        fname = in.readString();
        lname = in.readString();
        userphoto = in.readString();
        userphotothumb = in.readString();
        mediaType = in.readString();
        mediaUrl = in.readString();
        mediaThumb = in.readString();
        commentcount = in.readInt();
        likecount = in.readInt();
        islike = in.readInt();
        iscomment = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mypost);
        dest.writeInt(id);
        dest.writeString(postTitle);
        dest.writeInt(posttype);
        dest.writeInt(postTypeNew);
        dest.writeString(updatedDate);
        dest.writeInt(userid);
        dest.writeString(username);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(userphoto);
        dest.writeString(userphotothumb);
        dest.writeString(mediaType);
        dest.writeString(mediaUrl);
        dest.writeString(mediaThumb);
        dest.writeInt(commentcount);
        dest.writeInt(likecount);
        dest.writeInt(islike);
        dest.writeInt(iscomment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mediapost> CREATOR = new Creator<Mediapost>() {
        @Override
        public Mediapost createFromParcel(Parcel in) {
            return new Mediapost(in);
        }

        @Override
        public Mediapost[] newArray(int size) {
            return new Mediapost[size];
        }
    };

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
        if(posttype==0)
            posttype=postTypeNew;
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

    public String getMediaType() {
        return mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaThumb() {
        return mediaThumb;
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

    public int getIscomment() {
        return iscomment;
    }

    @Override
    public String toString() {
        return "Mediapost{" +
                "mypost=" + mypost +
                ", id=" + id +
                ", postTitle='" + postTitle + '\'' +
                ", posttype=" + posttype +
                ", updatedDate='" + updatedDate + '\'' +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", userphoto='" + userphoto + '\'' +
                ", userphotothumb='" + userphotothumb + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaThumb='" + mediaThumb + '\'' +
                ", commentcount=" + commentcount +
                ", likecount=" + likecount +
                ", islike=" + islike +
                ", iscomment=" + iscomment +
                '}';
    }
}
