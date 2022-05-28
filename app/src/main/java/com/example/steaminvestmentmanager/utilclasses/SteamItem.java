package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;


public class SteamItem {
    private final String market_hash_name;
    private final String itemIconURL;
    private final String appid;
    private Bitmap itemIcon;
    private float currentPrice;
    private float buyingPrice;
    private int amount;
    private int currentCurrency;
    private String currentCurrencyMedianPrice;

    public SteamItem(String market_hash_name, String appid, String itemIconURL, String enteredPrice, String enteredAmount) {
        this.market_hash_name = market_hash_name;
        this.appid = appid;
        this.itemIconURL = itemIconURL;
        this.itemIcon = new DownloadBitmapImage(this.getItemIconURL(), "/215fx215").call();
        this.buyingPrice = Float.parseFloat(enteredPrice);
        this.amount = Integer.parseInt(enteredAmount);
        currentCurrency = CurrencyData.getCurrency();
    }

    public void setbuyingPrice(Float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getMarket_hash_name() {
        return market_hash_name;
    }

    public Float getcurrentPrice() {
        return currentPrice;
    }

    public Bitmap getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(Bitmap itemIcon) {
        this.itemIcon = itemIcon;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemIconURL() {
        return itemIconURL;
    }

    public String getAppid() {
        return appid;
    }

    public int getAmount() {
        return amount;
    }

    public void setcurrentCurrency(int currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public void setcurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getcurrentCurrency() {
        return currentCurrency;
    }

    public void setcurrentCurrencyLowestPrice(String currentCurrencyMedianPrice) {
        this.currentCurrencyMedianPrice = "";
        this.currentCurrencyMedianPrice += currentCurrencyMedianPrice;
    }

    public String getcurrentCurrencyLowestPrice() {
        return currentCurrencyMedianPrice;
    }

    public Float getbuyingPrice() {
        return buyingPrice;
    }

    public boolean checkForBeingFull() {
        if (market_hash_name != null && itemIconURL != null && appid != null) {
            return !market_hash_name.equals("") && !itemIconURL.equals("") && !appid.equals("") && (amount >= 0) && (currentCurrency >= 0);
        }
        return false;
    }

    @Override
    public String toString() {
        return "m" + market_hash_name + "\n" +
                itemIconURL + "\n" +
                appid + "\n" +
                buyingPrice + "\n" +
                amount + "\n" +
                currentCurrency + "\n" +
                currentCurrencyMedianPrice;
    }
}
