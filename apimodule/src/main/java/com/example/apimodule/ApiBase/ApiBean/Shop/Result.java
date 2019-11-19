
package com.example.apimodule.ApiBase.ApiBean.Shop;

import com.example.apimodule.ApiBase.ApiBean.Itemdetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("items")
    @Expose
    private List<Itemdetail> items = null;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;

    public List<Itemdetail> getItems() {
        return items;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

}
