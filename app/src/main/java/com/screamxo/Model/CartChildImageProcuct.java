package com.screamxo.Model;

/**
 * Created by SONY on 18-03-2018.
 */

public class CartChildImageProcuct {


    public CartChildImageProcuct() {
    }

    public CartChildImageProcuct(String url) {
        this.imgChild = url;
    }
    public String getImgChild() {
        return imgChild;
    }

    public void setImgChild(String imgChild) {
        this.imgChild = imgChild;
    }

    String imgChild;
}
