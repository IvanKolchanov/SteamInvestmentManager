package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;

public class SteamItem {
    private String market_hash_name;
    private String median_price;
    private Bitmap itemIcon;

    public SteamItem(String market_hash_name, String median_price, Bitmap itemIcon) {
        this.market_hash_name = market_hash_name;
        this.median_price = median_price;
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
}
