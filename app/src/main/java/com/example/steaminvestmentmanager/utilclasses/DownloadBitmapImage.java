package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class DownloadBitmapImage implements Callable<Bitmap> {
    private final String itemIconURL;
    private final String size;
    public DownloadBitmapImage(String itemIconURL, String size) {
        this.itemIconURL = itemIconURL;
        this.size = size;
    }

    @Override
    public Bitmap call() {
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(itemIconURL + size).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception ignored) {
        }
        return icon;
    }
}
