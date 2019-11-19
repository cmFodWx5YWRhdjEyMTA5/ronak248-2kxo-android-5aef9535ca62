package com.example.apimodule.ApiBase.ApiBean.StripeCreateCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("stripe_customer_id")
    @Expose
    public String stripeCustomerId;

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }
}
