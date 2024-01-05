package com.example.steaminvestmentmanager.utilclasses;

import com.example.steaminvestmentmanager.MainActivity;
import com.google.gson.Gson;
import java.text.DecimalFormat;


public class CurrencyData {
    private static int currency = 0;
    private static final Gson gson = new Gson();
    private static final String[] currencies = new String[]{"$", "$", "£", "€", "", "руб."};

    public static String getCurrencyChar() {
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

    public static int getCurrencyArrayPosition() {
        for (int i = 0; i < getCurrencyArray().length; i++) {
            if (getCurrencyArray()[i].equals(getCurrencyChar())) return i;
        }
        return -1;
    }

    public static int getCurrencyFromChar(String currencyString) {
        for (int i = 0; i < currencies.length; i++) {
            if (currencyString.equals(currencies[i])) return i;
        }
        return 0;
    }

    public static void setInitialCurrency(int currency) {
        CurrencyData.currency = currency;
    }

    public static void setCurrency(int currency) {
        CurrencyData.currency = currency;
        SteamItem[] items = MainActivity.getSteamItems();
        if (items == null) return;
        for (SteamItem item:
             items) {
            if (item == null) continue;
            float ration = getCurrencyToCurrency(item.getCurrentCurrency(), currency);
            item.setBuyingPrice(Float.parseFloat(new DecimalFormat("#.##").format(item.getBuyingPrice() / ration).replace(',', '.')));
            item.setCurrentPrice(Float.parseFloat(new DecimalFormat("#.##").format(item.getCurrentPrice() / ration).replace(',', '.')));
            item.setCurrentCurrency(currency);
        }
        ItemListUpdater.updateSteamItemAdapter();
    }

    public static float transformPriceToNumber(String price) {
        price = price.replaceAll("pуб.", "").replaceAll("\\$", "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "").replaceAll("CHF", "");
        return Float.parseFloat(price);
    }

    private static final String firstPartOfURL = "https://steamcommunity.com/market/priceoverview/?currency=", secondPartOfURL = "&country=us&appid=440&market_hash_name=Mann%20Co.%20Supply%20Crate%20Key&format=json";

    public static float getCurrencyToCurrency(int firstCurrency, int secondCurrency){
        if (firstCurrency == secondCurrency) return 1.0f;
        String convURL1 = firstPartOfURL + firstCurrency + secondPartOfURL;
        String jsonSteamItem1 = new DownloadingPageHtmlCode(convURL1).call();
        PriceoverviewSteamItem item1 = gson.fromJson(jsonSteamItem1, PriceoverviewSteamItem.class);

        String convURL2 = firstPartOfURL + secondCurrency + secondPartOfURL;
        String jsonSteamItem2 = new DownloadingPageHtmlCode(convURL2).call();
        PriceoverviewSteamItem item2 = gson.fromJson(jsonSteamItem2, PriceoverviewSteamItem.class);
        if (item1 == null || item2 == null) return -1;
        return transformPriceToNumber(item1.getCurrentPrice()) / transformPriceToNumber(item2.getCurrentPrice());
    }

}
