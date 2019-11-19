
package com.example.apimodule.ApiBase.ApiBean.Comments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    @SerializedName("mypost")
    @Expose
    private Integer mypost;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_oldtitle")
    @Expose
    private String postOldtitle;
/*    @SerializedName("post_tagids")
    @Expose
    private String postTagids;*/
    @SerializedName("posttype")
    @Expose
    private Integer posttype;


    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("media_thumb")
    @Expose
    private String media_thumb;
    @SerializedName("media_url")
    @Expose
    private String media_url;

    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
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
    @SerializedName("commentcount")
    @Expose
    private Integer commentcount;
    @SerializedName("likecount")
    @Expose
    private Integer likecount;
    @SerializedName("islike")
    @Expose
    private Integer islike;
    @SerializedName("iscomment")
    @Expose
    private Integer iscomment;

    public Integer getMypost() {
        return mypost;
    }

    public void setMypost(Integer mypost) {
        this.mypost = mypost;
    }

    public Post withMypost(Integer mypost) {
        this.mypost = mypost;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Post withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Post withPostTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
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

    public String getPostOldtitle() {
        return postOldtitle;
    }

    public void setPostOldtitle(String postOldtitle) {
        this.postOldtitle = postOldtitle;
    }

    public Post withPostOldtitle(String postOldtitle) {
        this.postOldtitle = postOldtitle;
        return this;
    }

    public String getMedia_thumb() {
        return media_thumb;
    }

    public void setMedia_thumb(String media_thumb) {
        this.media_thumb = media_thumb;
    }
    /*  public String getPostTagids() {
        return postTagids;
    }

    public void setPostTagids(String postTagids) {
        this.postTagids = postTagids;
    }

    public Post withPostTagids(String postTagids) {
        this.postTagids = postTagids;
        return this;
    }*/

    public Integer getPosttype() {
        return posttype;
    }

    public void setPosttype(Integer posttype) {
        this.posttype = posttype;
    }

    public Post withPosttype(Integer posttype) {
        this.posttype = posttype;
        return this;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Post withUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Post withUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Post withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Post withFname(String fname) {
        this.fname = fname;
        return this;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Post withLname(String lname) {
        this.lname = lname;
        return this;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public Post withUserphoto(String userphoto) {
        this.userphoto = userphoto;
        return this;
    }

    public String getUserphotothumb() {
        return userphotothumb;
    }

    public void setUserphotothumb(String userphotothumb) {
        this.userphotothumb = userphotothumb;
    }

    public Post withUserphotothumb(String userphotothumb) {
        this.userphotothumb = userphotothumb;
        return this;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }

    public Post withCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
        return this;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }

    public Post withLikecount(Integer likecount) {
        this.likecount = likecount;
        return this;
    }

    public Integer getIslike() {
        return islike;
    }

    public void setIslike(Integer islike) {
        this.islike = islike;
    }

    public Post withIslike(Integer islike) {
        this.islike = islike;
        return this;
    }

    public Integer getIscomment() {
        return iscomment;
    }

    public void setIscomment(Integer iscomment) {
        this.iscomment = iscomment;
    }

    public Post withIscomment(Integer iscomment) {
        this.iscomment = iscomment;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mypost);
        dest.writeValue(this.id);
        dest.writeString(this.postTitle);
        dest.writeString(this.postOldtitle);
        dest.writeValue(this.posttype);
        dest.writeString(this.media_type);
        dest.writeString(this.media_thumb);
        dest.writeString(this.media_url);
        dest.writeString(this.updatedDate);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.userphoto);
        dest.writeString(this.userphotothumb);
        dest.writeValue(this.commentcount);
        dest.writeValue(this.likecount);
        dest.writeValue(this.islike);
        dest.writeValue(this.iscomment);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.mypost = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.postTitle = in.readString();
        this.postOldtitle = in.readString();
        this.posttype = (Integer) in.readValue(Integer.class.getClassLoader());
        this.media_type = in.readString();
        this.media_thumb = in.readString();
        this.media_url = in.readString();
        this.updatedDate = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.userphoto = in.readString();
        this.userphotothumb = in.readString();
        this.commentcount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.likecount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.islike = (Integer) in.readValue(Integer.class.getClassLoader());
        this.iscomment = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
