package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Bitmap
import android.util.Log
import com.google.gson.Gson

class SteamItem(val market_hash_name: String?, val appID: String?, val itemIconURL: String?, enteredPrice: String, enteredAmount: String) {
    var itemIcon: Bitmap?
    var currentPrice = 0f
    var buyingPrice: Float
    var amount: Int
    var currentCurrency: Int

    init {
        itemIcon = DownloadBitmapImage(itemIconURL, "/215fx215").call()
        buyingPrice = enteredPrice.toFloat()
        amount = enteredAmount.toInt()
        currentCurrency = CurrencyData.getCurrency()
        updateItemPrice()
    }

    fun updateItemPrice() {
        val priceURL = PriceURLCreator.getURL(this, true)
        val jsonSteamItem = DownloadingPageHtmlCode(priceURL).call()
        val poSteamItem = gson.fromJson(jsonSteamItem, PriceoverviewSteamItem::class.java) ?: return
        if (poSteamItem.success) {
            currentPrice = CurrencyData.transformPriceToNumber(poSteamItem.lowest_price)
        }else {
            Log.d("SteamIvan", "Couldn't update Item Price $priceURL $jsonSteamItem $this")
        }
    }

    fun updateIcon() {
        itemIcon = DownloadBitmapImage(itemIconURL, "/215fx215").call()
    }

    fun checkForBeingFull(): Boolean {
        return if (market_hash_name != null && itemIconURL != null && appID != null) {
            market_hash_name != "" && itemIconURL != "" && appID != "" && amount >= 0 && currentCurrency >= 0
        } else false
    }

    override fun toString(): String {
        return """
            m$market_hash_name
            $itemIconURL
            $appID
            $buyingPrice
            $amount
            $currentCurrency
            $currentPrice
            """.trimIndent()
    }

    companion object {
        private val gson = Gson()
    }
}