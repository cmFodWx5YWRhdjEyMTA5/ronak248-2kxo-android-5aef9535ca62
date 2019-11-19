
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Globaltrend {

    @SerializedName("itemdetails")
    @Expose
    private List<Itemdetail___> itemdetails = null;
    @SerializedName("itemcount")
    @Expose
    private Integer itemcount;

    public List<Itemdetail___> getItemdetails() {
        return itemdetails;
    }

    public void setItemdetails(List<Itemdetail___> itemdetails) {
        this.itemdetails = itemdetails;
    }

    public Globaltrend withItemdetails(List<Itemdetail___> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }

    public Integer getItemcount() {
        return itemcount;
    }

    public void setItemcount(Integer itemcount) {
        this.itemcount = itemcount;
    }

    public Globaltrend withItemcount(Integer itemcount) {
        this.itemcount = itemcount;
        return this;
    }

}
