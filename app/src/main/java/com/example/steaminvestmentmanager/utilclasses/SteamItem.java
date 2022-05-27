package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;


public class SteamItem {
    private final String market_hash_name;
    private String lowest_price;
    private final String itemIconURL;
    private final String appid;
    private Bitmap itemIcon;
    private String starterPrice;
    private int amount;
    private int firstInitializationCurrency;
    private String firstInitializationCurrencyMedianPrice;

    public SteamItem(String market_hash_name, String appid, String itemIconURL, String enteredPrice, String enteredAmount) {
        this.market_hash_name = market_hash_name;
        this.appid = appid;
        this.itemIconURL = itemIconURL;
        this.itemIcon = new DownloadBitmapImage(this.getItemIconURL(), "/215fx215").call();
        this.starterPrice = enteredPrice;
        this.amount = Integer.parseInt(enteredAmount);
        firstInitializationCurrency = CurrencyData.getCurrency();
    }

    public void downloadItemIconAgain() {
        this.itemIcon = new DownloadBitmapImage(this.getItemIconURL(), "/215fx215").call();
    }

    public void setStarterPrice(String starterPrice) {
        this.starterPrice = "";
        this.starterPrice += starterPrice;
        System.out.println(this.starterPrice);
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

    public void setFirstInitializationCurrency(int firstInitializationCurrency) {
        this.firstInitializationCurrency = firstInitializationCurrency;
    }

    public void setLowest_price(String lowest_price) {
        this.lowest_price = lowest_price;
    }

    public int getFirstInitializationCurrency() {
        return firstInitializationCurrency;
    }

    public void setFirstInitializationCurrencyLowestPrice(String firstInitializationCurrencyMedianPrice) {
        this.firstInitializationCurrencyMedianPrice = "";
        this.firstInitializationCurrencyMedianPrice += firstInitializationCurrencyMedianPrice;
    }

    public String getFirstInitializationCurrencyLowestPrice() {
        return firstInitializationCurrencyMedianPrice;
    }

    public String getStarterPrice() {
        return starterPrice;
    }

    public boolean checkForBeingFull() {
        if ((market_hash_name != null) && (itemIconURL != null) && (appid != null) && (starterPrice != null)) {
            return !market_hash_name.equals("") && !itemIconURL.equals("") && !appid.equals("") && !starterPrice.equals("") && (amount >= 0) && (firstInitializationCurrency >= 0);
        }
        return false;
    }

    @Override
    public String toString() {
        return market_hash_name + "\n" +
                itemIconURL + "\n" +
                appid + "\n" +
                starterPrice + "\n" +
                amount + "\n" +
                firstInitializationCurrency + "\n" +
                firstInitializationCurrencyMedianPrice;
    }
}
