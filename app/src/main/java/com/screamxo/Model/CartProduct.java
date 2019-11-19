package com.screamxo.Model;

import java.util.ArrayList;

/**
 * Created by SONY on 18-03-2018.
 */

public class CartProduct {
    public String txtProduct;
    public ArrayList<CartChildImageProcuct> itemChildArrary;

    public CartProduct(String txtProduct,ArrayList<CartChildImageProcuct> itemChildArrary){
        this.txtProduct = txtProduct;
        this.itemChildArrary = itemChildArrary;
    }
    public CartProduct(){

    }
    public ArrayList<CartChildImageProcuct> getItemChildArrary() {
        return itemChildArrary;
    }

    public void setItemChildArrary(ArrayList<CartChildImageProcuct> itemChildArrary) {
        this.itemChildArrary = itemChildArrary;
    }

    public String getTxtProduct() {
        return txtProduct;
    }

    public void setTxtProduct(String txtProduct) {
        this.txtProduct = txtProduct;
    }

}
