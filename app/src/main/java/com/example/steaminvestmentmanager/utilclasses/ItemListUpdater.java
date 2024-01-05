package com.example.steaminvestmentmanager.utilclasses;

import com.example.steaminvestmentmanager.MainActivity;

public class ItemListUpdater {
    private static MainActivity mainActivity;

    public static void setMainActivity(MainActivity mA) {
        mainActivity = mA;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void updateSteamItemAdapter() {
        mainActivity.runOnUiThread(() -> MainActivity.steamItemAdapter.notifyDataSetChanged());
    }
}
