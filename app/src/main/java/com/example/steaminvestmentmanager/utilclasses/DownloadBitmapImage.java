package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.concurrent.Callable;

public class DownloadBitmapImage implements Callable<Bitmap> {
    private String itemIconURL;
    private String size;
    public DownloadBitmapImage(String itemIconURL, String size) {
        this.itemIconURL = itemIconURL;
        this.size = size;
    }

    @Override
    public Bitmap call() {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(itemIconURL + size).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
}
