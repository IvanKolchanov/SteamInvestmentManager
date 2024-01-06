package com.example.steaminvestmentmanager.utilclasses

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object PriceURLCreator {
    fun getURL(steamItem: SteamItem, isUserCurrency: Boolean): String {
        var itemPriceoverviewURLBase = "https://steamcommunity.com/market/priceoverview/?"
        itemPriceoverviewURLBase += "appid=" + steamItem.appID
        itemPriceoverviewURLBase += if (isUserCurrency) {
            "&currency=" + CurrencyData.getCurrency()
        } else {
            "&currency=" + steamItem.currentCurrency
        }
        val market_hash_name_encoded: String = try {
            URLEncoder.encode(steamItem.market_hash_name, StandardCharsets.UTF_8.toString())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return ""
        }
        itemPriceoverviewURLBase += "&market_hash_name=$market_hash_name_encoded"
        return itemPriceoverviewURLBase
    }
}