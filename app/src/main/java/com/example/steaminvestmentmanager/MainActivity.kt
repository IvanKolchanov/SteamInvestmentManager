package com.example.steaminvestmentmanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.steaminvestmentmanager.utilclasses.CurrencyData
import com.example.steaminvestmentmanager.utilclasses.CurrencyExchangeDialog
import com.example.steaminvestmentmanager.utilclasses.EnteringURLDialog
import com.example.steaminvestmentmanager.utilclasses.ItemListUpdater
import com.example.steaminvestmentmanager.utilclasses.SettingsDialog
import com.example.steaminvestmentmanager.utilclasses.SteamItem
import com.example.steaminvestmentmanager.utilclasses.SteamItemAdapter
import com.example.steaminvestmentmanager.utilclasses.SteamItemInformationDialog
import com.google.gson.Gson
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private var steamItemsListView: ListView? = null
    private val preferenceName = "savedDataSIM"
    private val TAG = "SteamIvan"
    private val itemsUpdatingThread = ItemsUpdatingThread()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        steamItemsListView = findViewById(R.id.itemListView)
        steamItemAdapter = SteamItemAdapter(applicationContext, ArrayList())
        setSteamItemsAdapter(steamItemAdapter)

        Thread {CurrencyData.updateExchangeRatios()}.start()
        for (i in CurrencyData.exchangeRatios.values) {
            Log.d("SteamIvan", "$i AAA")
        }

        try {
            retrieveItemsFromPreference()
        } catch (e: Exception) {
            Log.d(TAG, "onCreate: $e")
        }
        ItemListUpdater.mainActivity = this
        itemsUpdatingThread.start()
    }

    override fun onStop() {
        saveSteamItems()
        super.onStop()
    }

    override fun onResume() {
        Thread {CurrencyData.updateExchangeRatios()}.start()
        super.onResume()
    }

    private fun setSteamItemsAdapter(steamItemAdapter: SteamItemAdapter?) {
        runOnUiThread {
            try {
                steamItemsListView!!.adapter = steamItemAdapter
            } catch (ignored: Exception) { }
            steamItemsListView!!.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                val steamItem = Objects.requireNonNull(getSteamItems())[position]
                val steamItemInformationDialog = SteamItemInformationDialog(steamItem)
                steamItemInformationDialog.show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.open_menu) {
            showPopupMenu(findViewById(R.id.open_menu))
        }
        return true
    }

    private fun showPopupMenu(v: View) {
        val wrapper: Context = ContextThemeWrapper(applicationContext, R.style.popupMenuStyle)
        val popupMenu = PopupMenu(wrapper, v)
        popupMenu.inflate(R.menu.user_popup_menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.add_steamItem) {
                val enteringURLDialog = EnteringURLDialog(this)
                enteringURLDialog.show(supportFragmentManager, null)
                return@setOnMenuItemClickListener true
            }
            if (item.itemId == R.id.open_settings) {
                val settingsDialog = SettingsDialog()
                settingsDialog.show(supportFragmentManager, null)
                return@setOnMenuItemClickListener true
            }
            if (item.itemId == R.id.currency_exchange_rate) {
                val currencyExchangeDialog = CurrencyExchangeDialog()
                currencyExchangeDialog.show(supportFragmentManager, null)
                return@setOnMenuItemClickListener true
            }
            if (item.itemId == R.id.scan_account_items) {
                itemsUpdatingThread.pauseThread()
                Thread {

                }
                return@setOnMenuItemClickListener true
            }
            false
        }
        popupMenu.show()
    }

    fun sendNewSteamItem(addingSteamItem: SteamItem) {
        steamItems.add(addingSteamItem)
        runOnUiThread { steamItemAdapter!!.add(addingSteamItem) }
    }

    private fun clearSP() {
        getSharedPreferences(preferenceName, MODE_PRIVATE).edit().clear().apply()
    }

    private fun retrieveItemsFromPreference() {
        val sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
        val steamItemLength = sharedPreferences.getInt("NUMBER_OF_STEAM_ITEMS", 0)
        if (steamItemLength != 0) {
            for (i in 1..steamItemLength) {
                val jsonSteamItem = sharedPreferences.getString("item$i", "")
                val currentSteamItem = gson.fromJson(jsonSteamItem, SteamItem::class.java)
                if (currentSteamItem != null && currentSteamItem.checkForBeingFull()) {
                    Thread { currentSteamItem.updateIcon(); }.start()
                    steamItems.add(currentSteamItem)
                    Log.d("SteamIvan", steamItems.toString())
                    steamItemAdapter!!.add(currentSteamItem)
                }
            }
        }
        val userCurrency = sharedPreferences.getInt("USER_CURRENCY", 5)
        CurrencyData.setInitialCurrency(userCurrency)
    }

    private fun saveSteamItems() {
        val sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE)
        val preferenceEditor = sharedPreferences.edit()
        preferenceEditor.clear()
        for (i in 1..steamItems.size) {
            steamItems[i - 1].itemIcon = null
            val itemJson = gson.toJson(steamItems[i - 1])
            preferenceEditor.putString("item$i", itemJson)
        }
        preferenceEditor.putInt("NUMBER_OF_STEAM_ITEMS", steamItems.size)
        preferenceEditor.putInt("USER_CURRENCY", CurrencyData.getCurrency())
        preferenceEditor.apply()
    }

    companion object {
        private val steamItems: ArrayList<SteamItem> = ArrayList()
        private val gson = Gson()
        @JvmField
        var steamItemAdapter: SteamItemAdapter? = null
        @JvmStatic
        fun deleteSelectedItem(chosenSteamItem: SteamItem) {
            steamItems.remove(chosenSteamItem)
        }

        @JvmStatic
        fun getSteamItems(): Array<SteamItem> {
            return steamItems.toTypedArray()
        }
    }
}