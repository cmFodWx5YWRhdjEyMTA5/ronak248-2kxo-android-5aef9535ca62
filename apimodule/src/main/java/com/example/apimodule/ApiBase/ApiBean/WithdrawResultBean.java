package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.TwitterCollection;

public class WithdrawResultBean {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("amount_reversed")
    @Expose
    private int amountReversed;
    @SerializedName("balance_transaction")
    @Expose
    private String balanceTransaction;
    @SerializedName("created")
    @Expose
    private int created;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("destination_payment")
    @Expose
    private String destinationPayment;
    @SerializedName("livemode")
    @Expose
    private boolean livemode;
    @SerializedName("reversed")
    @Expose
    private boolean reversed;
    @SerializedName("source_transaction")
    @Expose
    private Object sourceTransaction;
    @SerializedName("source_type")
    @Expose
    private String sourceType;
    @SerializedName("transfer_group")
    @Expose
    private String transferGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountReversed() {
        return amountReversed;
    }

    public void setAmountReversed(int amountReversed) {
        this.amountReversed = amountReversed;
    }

    public String getBalanceTransaction() {
        return balanceTransaction;
    }

    public void setBalanceTransaction(String balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationPayment() {
        return destinationPayment;
    }

    public void setDestinationPayment(String destinationPayment) {
        this.destinationPayment = destinationPayment;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public Object getSourceTransaction() {
        return sourceTransaction;
    }

    public void setSourceTransaction(Object sourceTransaction) {
        this.sourceTransaction = sourceTransaction;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTransferGroup() {
        return transferGroup;
    }

    public void setTransferGroup(String transferGroup) {
        this.transferGroup = transferGroup;
    }
}
