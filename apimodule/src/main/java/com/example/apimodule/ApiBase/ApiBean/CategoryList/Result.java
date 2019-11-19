
package com.example.apimodule.ApiBase.ApiBean.CategoryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    public List<Category> getCategories() {
        return categories;
    }

    public ArrayList<Category> getSubcategories(String id) {
        try {
            for (Category category : getCategories()) {
                if (category.getId() == Integer.parseInt(id)) {
                    return (ArrayList<Category>) category.getSubcategories();
                }
            }
        } catch (Exception ignored) {

        }
        return new ArrayList<>();
    }

}
