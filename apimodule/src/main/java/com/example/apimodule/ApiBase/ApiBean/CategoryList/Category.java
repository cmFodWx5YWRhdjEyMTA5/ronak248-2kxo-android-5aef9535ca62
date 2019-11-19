
package com.example.apimodule.ApiBase.ApiBean.CategoryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_parent")
    @Expose
    private String categoryParent;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("isdeleted")
    @Expose
    private String isdeleted;
    @SerializedName("deleted_date")
    @Expose
    private String deletedDate;
    @SerializedName("sub_category")
    @Expose
    private List<Category> subcategories;


    public Integer getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }
}
