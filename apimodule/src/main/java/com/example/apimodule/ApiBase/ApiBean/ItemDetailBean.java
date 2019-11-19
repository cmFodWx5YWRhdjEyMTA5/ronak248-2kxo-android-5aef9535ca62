package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 23/11/16.
 */

public class ItemDetailBean implements Parcelable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private ItemDetailResult result;

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public ItemDetailResult getResult() {
        return result;
    }

    protected ItemDetailBean(Parcel in) {
        status = in.readString();
        msg = in.readString();
        result = (ItemDetailResult) in.readValue(ItemDetailResult.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(msg);
        dest.writeValue(result);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemDetailBean> CREATOR = new Parcelable.Creator<ItemDetailBean>() {
        @Override
        public ItemDetailBean createFromParcel(Parcel in) {
            return new ItemDetailBean(in);
        }

        @Override
        public ItemDetailBean[] newArray(int size) {
            return new ItemDetailBean[size];
        }
    };
}