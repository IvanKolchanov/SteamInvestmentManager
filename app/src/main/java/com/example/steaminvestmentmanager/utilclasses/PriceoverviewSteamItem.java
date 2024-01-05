package com.example.steaminvestmentmanager.utilclasses;

public class PriceoverviewSteamItem {

    private boolean success;
    private String lowest_price;
    private String volume;
    private String median_price;

    public boolean isSuccess() {
        return success;
    }

    public String getCurrentPrice() {
        return lowest_price;
    }
}
