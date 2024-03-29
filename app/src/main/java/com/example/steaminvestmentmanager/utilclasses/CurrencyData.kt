package com.example.steaminvestmentmanager.utilclasses

import android.util.Log
import com.example.steaminvestmentmanager.MainActivity.Companion.getSteamItems
import com.google.gson.Gson
import java.text.DecimalFormat

object CurrencyData {
    private var currency = 0
    private val gson = Gson()
    private val currencies = arrayOf("$", "$", "£", "€", "CHF", "₽")

    var exchangeRatios = HashMap<Int, Float>()

    init {
        exchangeRatios[0] = 1.0f
    }

    fun updateExchangeRatios() {
        for (i in currencyArray) {
            if (!i.equals("$")) {
                val iPos = getCurrencyFromChar(i)
                Log.d("SteamIvan", "$i")
                exchangeRatios[iPos] = getCurrencyToCurrency(0, iPos)
            }
        }
    }

    val currencyChar: String
        get() = try {
            currencies[currency]
        } catch (e: ArrayIndexOutOfBoundsException) {
            "$"
        }

    fun getCurrency(): Int {
        return currency
    }

    val currencyArray: Array<String?>
        get() = arrayOf("$", "£", "€", "₽")
    val currencyArrayPosition: Int
        get() {
            for (i in currencyArray.indices) {
                if (currencyArray[i] == currencyChar) return i
            }
            return -1
        }

    fun getCurrencyFromChar(currencyString: String?): Int {
        for (i in currencies.indices) {
            if (currencyString == currencies[i]) return i
        }
        return 0
    }

    fun setInitialCurrency(currency: Int) {
        CurrencyData.currency = currency
    }

    fun setCurrency(currency: Int) {
        CurrencyData.currency = currency
        val items = getSteamItems()
        for (item in items) {
            val ratio = exchangeRatios[item.currentCurrency]?.div(exchangeRatios[currency]!!)
            item.buyingPrice = DecimalFormat("#.##").format((item.buyingPrice * ratio!!).toDouble()).replace(',', '.').toFloat()
            item.currentPrice = DecimalFormat("#.##").format((item.currentPrice * ratio).toDouble()).replace(',', '.').toFloat()
            item.currentCurrency = currency
        }
        ItemListUpdater.updateSteamItemAdapter()
    }

    fun transformPriceToNumber(price: String?): Float {
        return price!!.replace("pуб.".toRegex(), "").replace("\\$".toRegex(), "").replace(",".toRegex(), ".").replace("€".toRegex(), "").replace("£".toRegex(), "").replace("CHF".toRegex(), "").toFloat()
    }

    private const val firstPartOfURL = "https://steamcommunity.com/market/priceoverview/?currency="
    private const val secondPartOfURL = "&country=us&appid=440&market_hash_name=Mann%20Co.%20Supply%20Crate%20Key&format=json"
    fun getCurrencyToCurrency(firstCurrency: Int, secondCurrency: Int): Float {
        if (firstCurrency == secondCurrency) return 1.0f
        val convURL1 = firstPartOfURL + firstCurrency + secondPartOfURL
        val jsonSteamItem1 = DownloadingPageHtmlCode(convURL1).call()
        val item1 = gson.fromJson(jsonSteamItem1, PriceoverviewSteamItem::class.java)

        val convURL2 = firstPartOfURL + secondCurrency + secondPartOfURL
        val jsonSteamItem2 = DownloadingPageHtmlCode(convURL2).call()
        val item2 = gson.fromJson(jsonSteamItem2, PriceoverviewSteamItem::class.java)
        return if (item1 == null || item2 == null) (-1.0f) else transformPriceToNumber(item1.lowest_price) / transformPriceToNumber(item2.lowest_price)
    }


}