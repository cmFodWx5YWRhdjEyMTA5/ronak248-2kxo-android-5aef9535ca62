package com.example.apimodule.ApiBase.ApiBean;

import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Shubham.Agarwal on 11/11/16.
 */

public class ItemImage {
    private File imagefile;

    public ItemImage(File imagefile){
        this.imagefile=imagefile;
    }

    private File imageuri;

    public File getUri() {
        return imageuri;
    }

    public void setUri(File imageuri) {
        this.imageuri = imageuri;
    }
}
