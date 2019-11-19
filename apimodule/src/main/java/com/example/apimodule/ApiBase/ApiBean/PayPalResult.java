package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham.Agarwal on 21/11/16.
 */

public class PayPalResult {
    @SerializedName("Errors")
    @Expose
    private ArrayList<Object> errors = new ArrayList<Object>();
    @SerializedName("Ack")
    @Expose
    private String ack;
    @SerializedName("Build")
    @Expose
    private String build;
    @SerializedName("CorrelationID")
    @Expose
    private String correlationID;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("PayKey")
    @Expose
    private String payKey;
    @SerializedName("PaymentExecStatus")
    @Expose
    private String paymentExecStatus;
    @SerializedName("RedirectURL")
    @Expose
    private String redirectURL;

    /**
     *
     * @return
     * The errors
     */
    public ArrayList<Object> getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The Errors
     */
    public void setErrors(ArrayList<Object> errors) {
        this.errors = errors;
    }

    /**
     *
     * @return
     * The ack
     */
    public String getAck() {
        return ack;
    }

    /**
     *
     * @param ack
     * The Ack
     */
    public void setAck(String ack) {
        this.ack = ack;
    }

    /**
     *
     * @return
     * The build
     */
    public String getBuild() {
        return build;
    }

    /**
     *
     * @param build
     * The Build
     */
    public void setBuild(String build) {
        this.build = build;
    }

    /**
     *
     * @return
     * The correlationID
     */
    public String getCorrelationID() {
        return correlationID;
    }

    /**
     *
     * @param correlationID
     * The CorrelationID
     */
    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    /**
     *
     * @return
     * The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     * The Timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     * The payKey
     */
    public String getPayKey() {
        return payKey;
    }

    /**
     *
     * @param payKey
     * The PayKey
     */
    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    /**
     *
     * @return
     * The paymentExecStatus
     */
    public String getPaymentExecStatus() {
        return paymentExecStatus;
    }

    /**
     *
     * @param paymentExecStatus
     * The PaymentExecStatus
     */
    public void setPaymentExecStatus(String paymentExecStatus) {
        this.paymentExecStatus = paymentExecStatus;
    }

    /**
     *
     * @return
     * The redirectURL
     */
    public String getRedirectURL() {
        return redirectURL;
    }

    /**
     *
     * @param redirectURL
     * The RedirectURL
     */
    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}
