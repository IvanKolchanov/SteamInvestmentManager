package com.example.steaminvestmentmanager

import android.util.Log
import com.example.steaminvestmentmanager.utilclasses.DownloadingPageHtmlCode
import com.example.steaminvestmentmanager.utilclasses.SteamItem
import java.util.Scanner

class ItemAddingThread(private val steamItemURL: String, private val enteredPrice: String, private val enteredAmount: String, private val mainActivity: MainActivity) : Thread() {
    private var content: String? = null
    private var market_hash_name: String? = null
    private var icon_url = "https://community.cloudflare.steamstatic.com/economy/image/"
    override fun run() {
        itemHtmlPage
        createSteamItem()
    }

    private val itemHtmlPage: Unit
        get() {
            val downloadingPageHtmlCode = DownloadingPageHtmlCode(steamItemURL)
            content = downloadingPageHtmlCode.call()
        }

    private fun createSteamItem() {
        val urlArray = steamItemURL.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val appid = urlArray[5]
        val cin = Scanner(content)
        var isFound = false
        var g_rgAssetsLine = ""
        while (!isFound && cin.hasNext()) {
            val currentRow = cin.nextLine()
            val currentRowArray = currentRow.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (currentRowArray.size > 1) {
                if (currentRowArray[1] == "g_rgAssets") {
                    g_rgAssetsLine = currentRow
                    isFound = true
                }
            }
        }
        cin.close()
        val g_rgAssetsLineArray = g_rgAssetsLine.toCharArray()
        processRgAssets(g_rgAssetsLineArray)
        val addingItem = SteamItem(market_hash_name, appid, icon_url, enteredPrice, enteredAmount)
        Log.d("SteamIvan", addingItem.toString() + "")
        mainActivity.sendNewSteamItem(addingItem)
    }

    private fun processRgAssets(g_rgAssetsLineArray: CharArray) {
        val WRONG_WORD = 0
        val MARKET_HASH_NAME = 2
        val ICON_URL = 1
        var isStillWord = false
        var presentOperation = 0
        var howMuchWeFound = 0
        var currentWord = ""
        for (c in g_rgAssetsLineArray) {
            if (howMuchWeFound != 2) {
                if (presentOperation == WRONG_WORD) {
                    if (!isStillWord && c == '"') {
                        isStillWord = true
                    } else if (isStillWord && c == '"') {
                        isStillWord = false
                        when (currentWord) {
                            "market_hash_name" -> presentOperation = MARKET_HASH_NAME
                            "icon_url" -> presentOperation = ICON_URL
                            else -> {}
                        }
                        currentWord = ""
                    } else if (isStillWord) {
                        currentWord += c.toString()
                    }
                } else {
                    if (!isStillWord && c == '"') {
                        isStillWord = true
                    } else if (isStillWord && c == '"') {
                        isStillWord = false
                        when (presentOperation) {
                            MARKET_HASH_NAME -> {
                                market_hash_name = currentWord
                                howMuchWeFound++
                            }

                            ICON_URL -> {
                                icon_url += currentWord
                                howMuchWeFound++
                            }
                        }
                        presentOperation = WRONG_WORD
                        currentWord = ""
                    } else if (isStillWord) {
                        currentWord += c.toString()
                    }
                }
            } else {
                break
            }
        }
    }
}