package com.example.steaminvestmentmanager.utilclasses

import com.example.steaminvestmentmanager.MainActivity

object ItemListUpdater {
    var mainActivity: MainActivity? = null
    fun updateSteamItemAdapter() {
        mainActivity!!.runOnUiThread { MainActivity.steamItemAdapter!!.notifyDataSetChanged() }
    }
}