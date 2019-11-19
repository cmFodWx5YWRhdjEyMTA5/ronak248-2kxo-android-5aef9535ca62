
package com.example.apimodule.ApiBase.ApiBean.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("wallet_balance")
    @Expose
    private Double walletBalance;

    @SerializedName("stripe_payment_id")
    @Expose
    private String stripePaymentId;

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Double getAmount() {
        return amount;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

}
