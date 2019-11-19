package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trackingdetail implements Parcelable {

    //    @SerializedName("comapnyname")
    @SerializedName("companyname")
    @Expose
    private String comapnyname;
    @SerializedName("trackingid")
    @Expose
    private String trackingid;

    public String getComapnyname() {
        return comapnyname;
    }

    public String getTrackingid() {
        return trackingid;
    }

    public void setComapnyname(String comapnyname) {
        this.comapnyname = comapnyname;
    }

    public void setTrackingid(String trackingid) {
        this.trackingid = trackingid;
    }

    protected Trackingdetail(Parcel in) {
        comapnyname = in.readString();
        trackingid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comapnyname);
        dest.writeString(trackingid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trackingdetail> CREATOR = new Parcelable.Creator<Trackingdetail>() {
        @Override
        public Trackingdetail createFromParcel(Parcel in) {
            return new Trackingdetail(in);
        }

        @Override
        public Trackingdetail[] newArray(int size) {
            return new Trackingdetail[size];
        }
    };
}