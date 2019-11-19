package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 02/12/16.
 */

public class WatchingItemResult {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("items")
    @Expose
    private ArrayList<WatchingItem> items = new ArrayList<WatchingItem>();

    /**
     *
     * @return
     * The count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The items
     */
    public ArrayList<WatchingItem> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(ArrayList<WatchingItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "WatchingItemResult{" +
                "count=" + count +
                ", items=" + items +
                '}';
    }
}

