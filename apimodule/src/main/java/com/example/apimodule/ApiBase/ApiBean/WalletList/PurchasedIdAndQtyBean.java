package com.example.apimodule.ApiBase.ApiBean.WalletList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchasedIdAndQtyBean {

    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("productqty")
    @Expose
    private Integer productqty;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getProductqty() {
        return productqty;
    }

    public void setProductqty(Integer productqty) {
        this.productqty = productqty;
    }

}
