package com.example.steaminvestmentmanager;

import com.example.steaminvestmentmanager.utilclasses.*;

public class ItemsUpdatingThread extends Thread {
    private SteamItem[] steamItems;
    private boolean isFirstRun = true;

    @Override
    public void run() {
        while (!isInterrupted()) {
            SteamGetURLCreator steamGetURLCreator = new SteamGetURLCreator();
            steamItems = MainActivity.getSteamItems();
            if (steamItems != null) {
                if (isFirstRun) {
                    for (int i = 0; i < steamItems.length; i++) {

                    }
                    isFirstRun = false;
                }
                for (int i = 0; i < steamItems.length; i++) {
                    String url = steamGetURLCreator.getURL(steamItems[i]);
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
