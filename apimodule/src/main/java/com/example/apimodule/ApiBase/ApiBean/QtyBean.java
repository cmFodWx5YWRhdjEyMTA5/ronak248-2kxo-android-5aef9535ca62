package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QtyBean {
    public QtyBean(Integer itemId, Integer productqty) {
        this.itemId = itemId;
        this.productqty = productqty;
    }

    private Integer itemId;
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
