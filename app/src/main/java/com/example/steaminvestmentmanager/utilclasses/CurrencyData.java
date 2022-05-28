package com.example.steaminvestmentmanager.utilclasses;

import com.example.steaminvestmentmanager.MainActivity;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyData {
    private static int currency = 0;
    private static final Gson gson = new Gson();
    private static final String[] currencies = new String[]{"$", "$", "£", "€", "", "руб."};
    private static final String firstPartOfURL = "https://steamcommunity.com/market/priceoverview/?currency=", secondPartOfURL = "&country=us&appid=440&market_hash_name=Mann%20Co.%20Supply%20Crate%20Key&format=json";

    public static String getCurrencyChar() {
        try {
            return currencies[currency];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "$";
        }
    }

    public static String getSpecificCurrencyChar(int currency) {
        try {
            return currencies[currency];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "$";
        }
    }

    public static int getCurrency() {
        return currency;
    }

    public static String[] getCurrencyArray() {
        return new String[] {"$", "£", "€", "руб."};
    }

    public static int getCurrencyFromChar(String currencyString) {
        for (int i = 0; i < currencies.length; i++) {
            if (currencyString.equals(currencies[i])) return i;
        }
        return 0;
    }

    public static void setCurrency(int currency) {
        CurrencyData.currency = currency;
        SteamItem[] items = MainActivity.getSteamItems();
        for (SteamItem item:
             items) {
            float ration = getCurrencyToCurrency(item.getcurrentCurrency(), currency);
            item.setbuyingPrice(Float.parseFloat(new DecimalFormat("#.##").format(item.getbuyingPrice() / ration).replace(',', '.')));
        }
        MainActivity.sendSteamItems(items);
    }

    public static float transformPriceToNumber(String price) {
        price = price.replaceAll("pуб.", "").replaceAll("\\$", "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
        return Float.parseFloat(price);
    }

    public static float getCurrencyToCurrency(int firstCurrency, int secondCurrency){
        String convertationURL1 = firstPartOfURL + firstCurrency + secondPartOfURL;
        String jsonPriceoverviewSteamItem1 = new DownloadingPageHtmlCode(convertationURL1).call();
        PriceoverviewSteamItem priceoverviewSteamItem1 = gson.fromJson(jsonPriceoverviewSteamItem1, PriceoverviewSteamItem.class);
        String convertationURL2 = firstPartOfURL + secondCurrency + secondPartOfURL;
        String jsonPriceoverviewSteamItem2 = new DownloadingPageHtmlCode(convertationURL2).call();
        PriceoverviewSteamItem priceoverviewSteamItem2 = gson.fromJson(jsonPriceoverviewSteamItem2, PriceoverviewSteamItem.class);
        return transformPriceToNumber(priceoverviewSteamItem1.getcurrentPrice()) / transformPriceToNumber(priceoverviewSteamItem2.getcurrentPrice());
    }

}
