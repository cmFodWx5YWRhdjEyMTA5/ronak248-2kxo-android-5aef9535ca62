package com.example.apimodule.ApiBase.ApiBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parangat on 13/11/17.
 */

public class CardData {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("object")
    @Expose
    public String object;
    @SerializedName("address_city")
    @Expose
    public String addressCity;
    @SerializedName("address_country")
    @Expose
    public String addressCountry;
    @SerializedName("address_line1")
    @Expose
    public String addressLine1;
    @SerializedName("address_line1_check")
    @Expose
    public String addressLine1Check;
    @SerializedName("address_line2")
    @Expose
    public String addressLine2;
    @SerializedName("address_state")
    @Expose
    public String addressState;
    @SerializedName("address_zip")
    @Expose
    public String addressZip;
    @SerializedName("address_zip_check")
    @Expose
    public String addressZipCheck;
    @SerializedName("brand")
    @Expose
    public String brand;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("customer")
    @Expose
    public String customer;
    @SerializedName("cvc_check")
    @Expose
    public String cvcCheck;
    @SerializedName("dynamic_last4")
    @Expose
    public String dynamicLast4;
    @SerializedName("exp_month")
    @Expose
    public int expMonth;
    @SerializedName("exp_year")
    @Expose
    public int expYear;
    @SerializedName("fingerprint")
    @Expose
    public String fingerprint;
    @SerializedName("funding")
    @Expose
    public String funding;
    @SerializedName("last4")
    @Expose
    public String last4;

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("tokenization_method")
    @Expose
    public String tokenizationMethod;

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("isCard")
    @Expose
    public String isCard;

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getIsCard() {
        return isCard;
    }

    public String getId() {
        return id;
    }

    public String getString() {
        return object;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine1Check() {
        return addressLine1Check;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressState() {
        return addressState;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public String getAddressZipCheck() {
        return addressZipCheck;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() {
        return country;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCvcCheck() {
        return cvcCheck;
    }

    public String getDynamicLast4() {
        return dynamicLast4;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getFunding() {
        return funding;
    }

    public String getLast4() {
        return last4;
    }


    public String getName() {
        return name;
    }

    public String getTokenizationMethod() {
        return tokenizationMethod;
    }

    public String getAddress() {
        return getAddressCity() == null ? "" : getAddressCity() + ", "
                + getAddressState() == null ? "" : getAddressState() + ", "
                + getAddressCountry() == null ? "" : getAddressCountry() + ", "
                + getAddressZip() == null ? "" : getAddressZip() + ", ";
    }
}
