package com.example.steaminvestmentmanager;

import com.example.steaminvestmentmanager.utilclasses.*;

public class ItemsUpdatingThread extends Thread {

    public ItemsUpdatingThread() {}

    @Override
    public void run() {
        while (!isInterrupted()) {
            SteamItem[] steamItems = MainActivity.getSteamItems();
            if (steamItems != null) {
                for (SteamItem steamItem : steamItems) {
                    if (steamItem.getItemIcon() == null) {
                        steamItem.updateIcon();
                    }
                    steamItem.updateItemPrice();
                    ItemListUpdater.updateSteamItemAdapter();
                }
            }
            try {
                Thread.sleep(20000);
            }catch (Exception e) {}
        }
    }
}
