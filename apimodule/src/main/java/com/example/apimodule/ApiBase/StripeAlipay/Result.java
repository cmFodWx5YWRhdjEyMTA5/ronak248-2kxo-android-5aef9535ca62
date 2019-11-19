package com.example.apimodule.ApiBase.StripeAlipay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 30/10/17.
 */

public class Result {

   /* @SerializedName("stripe_source_id")
    @Expose
    public String stripeSourceId;*/

    @SerializedName("paypal_source_id")
    @Expose
    public String payPalSourceId;

    @SerializedName("paypal_payment_id")
    @Expose
    public String payPalpaymentId;

    @SerializedName("payid")
    @Expose
    public String payId;

    @SerializedName("approvelink")
    @Expose
    public String approveLink;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getApproveLink() {
        return approveLink;
    }

    public void setApproveLink(String approveLink) {
        this.approveLink = approveLink;
    }

    public String getPayPalpaymentId() {
        return payPalpaymentId;
    }

    public void setPayPalpaymentId(String payPalpaymentId) {
        this.payPalpaymentId = payPalpaymentId;
    }

    public String getPayPalSourceId() {
        return payPalSourceId;
    }

    public void setPayPalSourceId(String payPalSourceId) {
        this.payPalSourceId = payPalSourceId;
    }

    public void setStripeSourceId(String stripeSourceId) {
        this.stripeSourceId = stripeSourceId;
    }

    @SerializedName("stripe_payment_id")
    @Expose
    public String stripeSourceId;

    public String getStripeSourceId() {
        return stripeSourceId;
    }
}
