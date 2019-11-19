package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shubham.Agarwal on 12/11/16.
 */

public class ItemCategoriesResult {
    @SerializedName("categories")
    @Expose
    private ArrayList<ItemCategories> categories = new ArrayList<ItemCategories>();

    /**
     *
     * @return
     * The categories
     */
    public ArrayList<ItemCategories> getCategories() {
        return categories;
    }

    /**
     *
     * @param categories
     * The categories
     */
    public void setCategories(ArrayList<ItemCategories> categories) {
        this.categories = categories;
    }
}
