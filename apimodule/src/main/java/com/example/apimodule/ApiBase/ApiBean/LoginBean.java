package com.example.apimodule.ApiBase.ApiBean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBean implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status="1";
    }

    public String getMsg() {
        return msg;
    }

    public Result getResult() {
        return result;
    }

    protected LoginBean(Parcel in) {
        status = in.readString();
        msg = in.readString();
        result = (Result) in.readValue(Result.class.getClassLoader());
        token = in.readString();
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
        dest.writeValue(token);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginBean> CREATOR = new Parcelable.Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };
}