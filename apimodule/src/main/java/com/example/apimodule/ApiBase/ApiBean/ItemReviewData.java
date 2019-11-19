package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 12/12/16.
 */

public class ItemReviewData implements Parcelable {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("reviewid")
    @Expose
    private int reviewid;
    @SerializedName("revieweusername")
    @Expose
    private String revieweusername;
    @SerializedName("reviewuserid")
    @Expose
    private int reviewuserid;
    @SerializedName("reviewfname")
    @Expose
    private String reviewfname;
    @SerializedName("reviewlname")
    @Expose
    private String reviewlname;
    @SerializedName("reviewuserphoto")
    @Expose
    private String reviewuserphoto;
    @SerializedName("reviewuserphotothumb")
    @Expose
    private String reviewuserphotothumb;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReviewid() {
        return reviewid;
    }

    public void setReviewid(int reviewid) {
        this.reviewid = reviewid;
    }

    public String getRevieweusername() {
        return revieweusername;
    }

    public void setRevieweusername(String revieweusername) {
        this.revieweusername = revieweusername;
    }


    public int getReviewuserid() {
        return reviewuserid;
    }


    public void setReviewuserid(int reviewuserid) {
        this.reviewuserid = reviewuserid;
    }


    public String getReviewfname() {
        return reviewfname;
    }


    public void setReviewfname(String reviewfname) {
        this.reviewfname = reviewfname;
    }


    public String getReviewlname() {
        return reviewlname;
    }


    public void setReviewlname(String reviewlname) {
        this.reviewlname = reviewlname;
    }


    public String getReviewuserphoto() {
        return reviewuserphoto;
    }

    public void setReviewuserphoto(String reviewuserphoto) {
        this.reviewuserphoto = reviewuserphoto;
    }


    public String getReviewuserphotothumb() {
        return reviewuserphotothumb;
    }

   
    public void setReviewuserphotothumb(String reviewuserphotothumb) {
        this.reviewuserphotothumb = reviewuserphotothumb;
    }


    protected ItemReviewData(Parcel in) {
        description = in.readString();
        reviewid = in.readInt();
        revieweusername = in.readString();
        reviewuserid = in.readInt();
        reviewfname = in.readString();
        reviewlname = in.readString();
        reviewuserphoto = in.readString();
        reviewuserphotothumb = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeInt(reviewid);
        dest.writeString(revieweusername);
        dest.writeInt(reviewuserid);
        dest.writeString(reviewfname);
        dest.writeString(reviewlname);
        dest.writeString(reviewuserphoto);
        dest.writeString(reviewuserphotothumb);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemReviewData> CREATOR = new Parcelable.Creator<ItemReviewData>() {
        @Override
        public ItemReviewData createFromParcel(Parcel in) {
            return new ItemReviewData(in);
        }

        @Override
        public ItemReviewData[] newArray(int size) {
            return new ItemReviewData[size];
        }
    };
}

