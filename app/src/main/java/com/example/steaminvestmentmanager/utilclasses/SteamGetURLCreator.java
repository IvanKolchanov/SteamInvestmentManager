package com.example.steaminvestmentmanager.utilclasses;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SteamGetURLCreator {
    public String getURL(SteamItem steamItem, boolean isUserCurrency) {
        String itemPriceoverviewURLBase = "https://steamcommunity.com/market/priceoverview/?";
        itemPriceoverviewURLBase += "appid=" + steamItem.getAppid();
        if (isUserCurrency) {
            itemPriceoverviewURLBase += "&currency=" + CurrencyData.getCurrency();
        }else {
            itemPriceoverviewURLBase += "&currency=" + steamItem.getFirstInitializationCurrency();
        }
        String market_hash_name_encoded;
        try {
            market_hash_name_encoded = URLEncoder.encode(steamItem.getMarket_hash_name(), String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        itemPriceoverviewURLBase += "&market_hash_name=" + market_hash_name_encoded;
        return itemPriceoverviewURLBase;
    }
}
