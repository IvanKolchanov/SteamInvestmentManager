package com.example.steaminvestmentmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.steaminvestmentmanager.utilclasses.*;
import com.google.gson.Gson;

public class ItemsUpdatingThread extends Thread {
    private SteamItem[] steamItems;
    private final Gson gson = new Gson();
    private final MainActivity mainActivity;

    public ItemsUpdatingThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            SteamGetURLCreator steamGetURLCreator = new SteamGetURLCreator();
            steamItems = MainActivity.getSteamItems();
            Context mainActivityContext = SteamItemsListViewData.getMainActivityContext();
            if (steamItems != null) {
                for (SteamItem steamItem : steamItems) {
                    String priceoverviewURL = steamGetURLCreator.getURL(steamItem, true);
                    String jsonPriceoverviewSteamItem = new DownloadingPageHtmlCode(priceoverviewURL).call();
                    PriceoverviewSteamItem priceoverviewSteamItem = gson.fromJson(jsonPriceoverviewSteamItem, PriceoverviewSteamItem.class);
                    if (priceoverviewSteamItem.isSuccess()) {
                        steamItem.setcurrentPrice(CurrencyData.transformPriceToNumber(priceoverviewSteamItem.getcurrentPrice()));
                        steamItem.setcurrentCurrencyLowestPrice(priceoverviewSteamItem.getcurrentPrice());
                    }
                    MainActivity.sendSteamItems(steamItems);
                }
                try {
                    Thread.sleep(6000 * steamItems.length + 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    SteamItemAdapter steamItemAdapter = new SteamItemAdapter(mainActivityContext, MainActivity.getSteamItems());
                    mainActivity.setSteamItemsAdapter(steamItemAdapter);
                } catch (Exception e) {

                }

            }
        }
    }
}
