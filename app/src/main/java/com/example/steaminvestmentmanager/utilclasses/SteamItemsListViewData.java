package com.example.steaminvestmentmanager.utilclasses;

import android.content.Context;
import android.widget.ListView;

public class SteamItemsListViewData {
    private static ListView steamItemsListView;
    private static Context mainActivityContext;
    private static SteamItemAdapter steamItemAdapter;
    private static boolean isNewAdapter = false;

    public static Context getMainActivityContext() {
        return mainActivityContext;
    }

    public static ListView getSteamItemsListView() {
        return steamItemsListView;
    }

    public static void setMainActivityContext(Context mainActivityContext) {
        SteamItemsListViewData.mainActivityContext = mainActivityContext;
    }

    public static void setSteamItemsListView(ListView steamItemsListView) {
        SteamItemsListViewData.steamItemsListView = steamItemsListView;
    }

    public static SteamItemAdapter getSteamItemAdapter() {
        isNewAdapter = false;
        return steamItemAdapter;
    }

    public static void setSteamItemAdapter(SteamItemAdapter steamItemAdapter) {
        SteamItemsListViewData.steamItemAdapter = steamItemAdapter;
        isNewAdapter = true;
    }

    public static boolean isIsNewAdapter() {
        return isNewAdapter;
    }
}
