package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham Agarwal on 10/02/17.
 */
public class AdressResult {
    @SerializedName("shipping_address")
    @Expose
    private ShippingAddress shippingAddress;

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

}
