package com.example.apimodule.ApiBase.ApiBean.Boost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 13/11/17.
 */

public class StripeProcessBoostResult {
    @SerializedName("stripe_payment_id")
    @Expose
    private String stripePaymentId;

    public String getStripePaymentId() {
        return stripePaymentId;
    }
}

