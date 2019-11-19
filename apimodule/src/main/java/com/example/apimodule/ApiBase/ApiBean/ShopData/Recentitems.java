
package com.example.apimodule.ApiBase.ApiBean.ShopData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recentitems {

    @SerializedName("itemdetails")
    @Expose
    private List<Itemdetail__> itemdetails = null;
    @SerializedName("itemcount")
    @Expose
    private Integer itemcount;

    public List<Itemdetail__> getItemdetails() {
        return itemdetails;
    }

    public void setItemdetails(List<Itemdetail__> itemdetails) {
        this.itemdetails = itemdetails;
    }

    public Recentitems withItemdetails(List<Itemdetail__> itemdetails) {
        this.itemdetails = itemdetails;
        return this;
    }

    public Integer getItemcount() {
        return itemcount;
    }

    public void setItemcount(Integer itemcount) {
        this.itemcount = itemcount;
    }

    public Recentitems withItemcount(Integer itemcount) {
        this.itemcount = itemcount;
        return this;
    }

}
