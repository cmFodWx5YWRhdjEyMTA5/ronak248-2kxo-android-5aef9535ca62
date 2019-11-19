package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shubham.Agarwal on 12/11/16.
 */

public class ItemCategories {
    @SerializedName("id")
    @Expose
    private String id;
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

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     *
     * @param categoryName
     * The category_name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     *
     * @return
     * The categoryParent
     */
    public String getCategoryParent() {
        return categoryParent;
    }

    /**
     *
     * @param categoryParent
     * The category_parent
     */
    public void setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
    }

    /**
     *
     * @return
     * The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     * The created_date
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return
     * The isdeleted
     */
    public String getIsdeleted() {
        return isdeleted;
    }

    /**
     *
     * @param isdeleted
     * The isdeleted
     */
    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    /**
     *
     * @return
     * The deletedDate
     */
    public String getDeletedDate() {
        return deletedDate;
    }

    /**
     *
     * @param deletedDate
     * The deleted_date
     */
    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

}
