package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;

public class SteamItem {
    private String market_hash_name;
    private String median_price;
    private String itemIconURL;
    private String currency;
    private String appid;
    private Bitmap itemIcon;

    public SteamItem(String market_hash_name, String appid, String itemIconURL, Bitmap itemIcon) {
        this.market_hash_name = market_hash_name;
        this.appid = appid;
        this.itemIconURL = itemIconURL;
        this.itemIcon = itemIcon;
    }

    public String getMarket_hash_name() {
        return market_hash_name;
    }

    public String getMedian_price() {
        return median_price;
    }

    public Bitmap getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(Bitmap itemIcon) {
        this.itemIcon = itemIcon;
    }
}
