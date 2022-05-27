package com.example.steaminvestmentmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.steaminvestmentmanager.utilclasses.*;
import com.google.gson.Gson;

public class ItemsUpdatingThread extends Thread {
    private SteamItem[] steamItems;
    private boolean isFirstRun = true;
    private Gson gson = new Gson();
    private MainActivity mainActivity;

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
                    if (steamItem.getItemIcon() == null) {
                        steamItem.downloadItemIconAgain();
                    }
                    String priceoverviewURL = steamGetURLCreator.getURL(steamItem, true);
                    String jsonPriceoverviewSteamItem = new DownloadingPageHtmlCode(priceoverviewURL).call();
                    PriceoverviewSteamItem priceoverviewSteamItem = gson.fromJson(jsonPriceoverviewSteamItem, PriceoverviewSteamItem.class);
                    if (!CurrencyData.getCurrencyChar().equals(CurrencyData.getSpecificCurrencyChar(steamItem.getFirstInitializationCurrency()))) {
                        if (priceoverviewSteamItem.isSuccess()) {
                            steamItem.setLowest_price(priceoverviewSteamItem.getLowest_price());
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String priceoverviewRightCurrencyURL = steamGetURLCreator.getURL(steamItem, false);
                        String jsonPriceoverviewSteamItemRightCurrency = new DownloadingPageHtmlCode(priceoverviewRightCurrencyURL).call();
                        PriceoverviewSteamItem priceoverviewRightCurrencySteamItem = gson.fromJson(jsonPriceoverviewSteamItemRightCurrency, PriceoverviewSteamItem.class);
                        if (priceoverviewRightCurrencySteamItem.isSuccess()) {
                            steamItem.setFirstInitializationCurrencyLowestPrice(priceoverviewRightCurrencySteamItem.getLowest_price());
                        }
                    } else {
                        if (priceoverviewSteamItem.isSuccess()) {
                            steamItem.setLowest_price(priceoverviewSteamItem.getLowest_price());
                            steamItem.setFirstInitializationCurrencyLowestPrice(priceoverviewSteamItem.getLowest_price());
                        }
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
                }catch (Exception e) {

                }

            }
        }
    }
}
