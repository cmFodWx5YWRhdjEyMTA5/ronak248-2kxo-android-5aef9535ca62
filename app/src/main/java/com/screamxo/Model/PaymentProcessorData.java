package com.screamxo.Model;

import com.google.gson.Gson;

/**
 * Created by parangat on 26/10/17.
 */

public class PaymentProcessorData {
    @SuppressWarnings("unused")
    private static final String TAG = "PaymentProcessorData";

    private int paymentProcessorType;
    private String email;

    public PaymentProcessorData(int paymentProcessorType, String email) {
        this.paymentProcessorType = paymentProcessorType;
        this.email = email;
    }

    public int getPaymentProcessorType() {
        return paymentProcessorType;
    }

    public String getEmail() {
        return email;
    }

    public String toJsonString() {
        return new Gson().toJson(this, PaymentProcessorData.class);
    }
}
