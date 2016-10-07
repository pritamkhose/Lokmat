package com.pritam.lokmat.com.example.utility;

import android.app.Application;

import java.io.File;

/**
 * Created by Pritam on 10/1/2016.
 */
public class AppConstant extends Application {

    private String listurl = "http://115.112.141.11/lokmat/mobile_app.php?catid=";
    private String detailurl ="http://115.112.141.11/lokmat/mobile_storyapp.php?";
    private String cacheFolder = "Android"+ File.separator+"data"+File.separator+"com.pritam.lokmat"+File.separator;
    private boolean compressImage;

    public String getListurl() {
        return listurl;
    }

    public String getcacheFolder() {
        return cacheFolder;
    }


    public String getDetailurl() {
        return detailurl;
    }


    public boolean isCompressImage() {
        return compressImage;
    }

    public void setCompressImage(boolean compressImage) {
        this.compressImage = compressImage;
    }



}
