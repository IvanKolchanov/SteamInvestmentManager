package com.example.steaminvestmentmanager.utilclasses;

import android.content.Context;

public class SteamItemsListViewData {
    private static Context mainActivityContext;

    public static Context getMainActivityContext() {
        return mainActivityContext;
    }

    public static void setMainActivityContext(Context mainActivityContext) {
        SteamItemsListViewData.mainActivityContext = mainActivityContext;
    }

}
