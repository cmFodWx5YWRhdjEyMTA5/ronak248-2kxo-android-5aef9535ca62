package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 3/11/17.
 */

public class AccountAr {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("type")
    @Expose
    public String type;

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
}
