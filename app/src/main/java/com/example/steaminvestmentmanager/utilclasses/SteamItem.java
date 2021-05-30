package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;

public class SteamItem {
    private String market_hash_name;
    private String lowest_price;
    private String itemIconURL;
    private String currency;
    private String appid;
    private Bitmap itemIcon;
    private String starterPrice;
    private int amount;
    private SteamGetURLCreator steamGetURLCreator = new SteamGetURLCreator();
    private String firstInitializationPrice;
    private int firstInitializationCurrency;
    private String firstInitializationCurrencyMedianPrice;

    public SteamItem(String market_hash_name, String appid, String itemIconURL) {
        this.market_hash_name = market_hash_name;
        this.appid = appid;
        this.itemIconURL = itemIconURL;
        this.itemIcon = new DownloadBitmapImage(this.getItemIconURL(), "/48fx48").call();
        firstInitializationCurrency = CurrencyData.getCurrency();
    }

    public String getMarket_hash_name() {
        return market_hash_name;
    }

    public String getLowest_price() {
        return lowest_price;
    }

    public Bitmap getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(Bitmap itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemIconURL() {
        return itemIconURL;
    }

    public String getAppid() {
        return appid;
    }

    public void setLowest_price(String lowest_price) {
        this.lowest_price = lowest_price;
    }

    public int getFirstInitializationCurrency() {
        return firstInitializationCurrency;
    }

    public void setFirstInitializationCurrencyLowestPrice(String firstInitializationCurrencyMedianPrice) {
        this.firstInitializationCurrencyMedianPrice = firstInitializationCurrencyMedianPrice;
    }

    public String getFirstInitializationCurrencyLowestPrice() {
        return firstInitializationCurrencyMedianPrice;
    }

    @Override
    public String toString() {
        return market_hash_name + "\n" +
                itemIconURL + "\n" +
                appid;
    }
}
