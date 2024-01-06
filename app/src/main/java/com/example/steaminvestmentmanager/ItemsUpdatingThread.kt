package com.example.steaminvestmentmanager

import com.example.steaminvestmentmanager.MainActivity.Companion.getSteamItems
import com.example.steaminvestmentmanager.utilclasses.ItemListUpdater

class ItemsUpdatingThread : Thread() {
    override fun run() {
        while (!isInterrupted) {
            val steamItems = getSteamItems()
            for (steamItem in steamItems) {
                if (steamItem.itemIcon == null) {
                    steamItem.updateIcon()
                }
                steamItem.updateItemPrice()
                ItemListUpdater.updateSteamItemAdapter()
            }
            try {
                sleep(20000)
            } catch (_: Exception) {
            }
        }
    }
}