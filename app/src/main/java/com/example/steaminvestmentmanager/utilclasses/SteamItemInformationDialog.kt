package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.fragment.app.DialogFragment
import com.example.steaminvestmentmanager.MainActivity
import com.example.steaminvestmentmanager.MainActivity.Companion.deleteSelectedItem
import com.example.steaminvestmentmanager.R
import java.util.Objects

class SteamItemInformationDialog(private val chosenSteamItem: SteamItem) : DialogFragment() {
    private var text: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.steam_item_information_dialog, null)

        //настраиваю окно диалога
        Objects.requireNonNull(dialog)?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //устанавливаю окно предмета
        val itemIcon = rootView.findViewById<ImageView>(R.id.item_icon)
        itemIcon.setImageBitmap(chosenSteamItem.itemIcon)

        //устанавливаю имя предмета
        val itemName = rootView.findViewById<TextView>(R.id.item_name)
        itemName.text = chosenSteamItem.market_hash_name

        //делаю всё для редактирования начальной цены
        val buyingPriceViewSwitcher = rootView.findViewById<ViewSwitcher>(R.id.starter_price)
        buyingPriceViewSwitcher.showNext()
        val pencilbuyingPrice = rootView.findViewById<ImageView>(R.id.change_view_pencil_price)
        pencilbuyingPrice.setImageDrawable(resources.getDrawable(R.drawable.outline_create_24, null))
        val currencyText = rootView.findViewById<TextView>(R.id.currency)
        currencyText.text = CurrencyData.currencyChar
        pencilbuyingPrice.setOnClickListener { v: View? ->
            text = ""
            val currentSwitcherView = buyingPriceViewSwitcher.currentView
            if (currentSwitcherView is TextView) {
                text = "" + (buyingPriceViewSwitcher.currentView as TextView).text
                val priceEditText = buyingPriceViewSwitcher.findViewById<EditText>(R.id.item_starter_priceEdit)
                priceEditText.setText(text)
                priceEditText.setOnKeyListener { v1: View?, keyCode: Int, event: KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        buyingPriceViewSwitcher.showNext()
                        val itemPriceCurrentTextView = buyingPriceViewSwitcher.currentView as TextView
                        try {
                            itemPriceCurrentTextView.text = priceEditText.text
                            chosenSteamItem.buyingPrice = priceEditText.text.toString().toFloat()
                            val profitView = rootView.findViewById<TextView>(R.id.item_profit)
                            setProfit(profitView, chosenSteamItem)
                            return@setOnKeyListener true
                        } catch (e: Exception) {
                            return@setOnKeyListener false
                        }
                    } else {
                        return@setOnKeyListener false
                    }
                }
            } else {
                val editText = buyingPriceViewSwitcher.currentView as EditText
                try {
                    if (editText.text.toString() != "") {
                        val i = editText.text.toString().replace(",", ".").toFloat()
                        (buyingPriceViewSwitcher.findViewById<View>(R.id.item_buy_price) as TextView).text = String.format(i.toString())
                    }
                } catch (e: Exception) {
                    text = (buyingPriceViewSwitcher.findViewById<View>(R.id.item_amount) as TextView).text as String
                }
            }
            buyingPriceViewSwitcher.showNext()
            if (buyingPriceViewSwitcher.currentView is EditText) {
                val ed = buyingPriceViewSwitcher.currentView as EditText
                ed.requestFocus()
                ed.setSelection(ed.text.length)
            }
        }

        //установка нынешней цены
        val itemCurrentPrice = rootView.findViewById<TextView>(R.id.item_current_price)
        itemCurrentPrice.text = "${getString(R.string.current_price_text)}  " + chosenSteamItem.currentPrice + CurrencyData.currencyChar

        //установка и настройка редактирования amount
        val viewSwitcher = rootView.findViewById<ViewSwitcher>(R.id.amount)
        viewSwitcher.showNext()
        val itemAmount = viewSwitcher.currentView as TextView
        itemAmount.text = String.format(Integer.toString(chosenSteamItem.amount))
        val pencilIcon = rootView.findViewById<ImageView>(R.id.change_view_pencil)
        pencilIcon.setImageDrawable(resources.getDrawable(R.drawable.outline_create_24, null))
        pencilIcon.setOnClickListener { v: View? ->
            text = ""
            val currentSwitcherView = viewSwitcher.currentView
            if (currentSwitcherView is TextView) {
                text = "" + (viewSwitcher.currentView as TextView).text
                val amountEditText = viewSwitcher.findViewById<EditText>(R.id.item_amountEdit)
                amountEditText.setText(text)
                amountEditText.setOnKeyListener { v12: View?, keyCode: Int, event: KeyEvent? ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        viewSwitcher.showNext()
                        val itemAmountCurrentTextView = viewSwitcher.currentView as TextView
                        try {
                            chosenSteamItem.amount = ("" + amountEditText.text).toInt()
                            itemAmountCurrentTextView.text = amountEditText.text
                            setProfit(rootView.findViewById<TextView>(R.id.item_profit), chosenSteamItem)
                        } catch (e: NumberFormatException) { }
                        return@setOnKeyListener true
                    } else {
                        return@setOnKeyListener false
                    }
                }
            } else {
                val editText = viewSwitcher.currentView as EditText
                try {
                    if (editText.text.toString() != "") {
                        val i = editText.text.toString().toInt()
                        (viewSwitcher.findViewById<View>(R.id.item_amount) as TextView).text = String.format(Integer.toString(i))
                    }
                } catch (e: Exception) {
                    text = (viewSwitcher.findViewById<View>(R.id.item_amount) as TextView).text as String
                }
            }
            viewSwitcher.showNext()
            if (viewSwitcher.currentView is EditText) {
                val ed = viewSwitcher.currentView as EditText
                ed.requestFocus()
                ed.setSelection(ed.text.length)
            }
        }

        //delete button
        val steamItemDeleteButton = rootView.findViewById<Button>(R.id.item_delete_button)
        steamItemDeleteButton.setOnClickListener { v: View? ->
            deleteSelectedItem(chosenSteamItem)
            ItemListUpdater.mainActivity?.runOnUiThread { MainActivity.steamItemAdapter!!.remove(chosenSteamItem) }
            dismiss()
        }

        //устанавливаю цену покупки
        val buyingPriceText = buyingPriceViewSwitcher.findViewById<TextView>(R.id.item_buy_price)
        val itemText = chosenSteamItem.buyingPrice.toString()
        buyingPriceText.text = itemText
        //устанавливаю прибыль
        val itemProfit = rootView.findViewById<TextView>(R.id.item_profit)
        setProfit(itemProfit, chosenSteamItem)
        return rootView
    }

    private fun setProfit(itemProfit: TextView, chosenSteamItem: SteamItem) {
        val buyingPrice = chosenSteamItem.buyingPrice
        val currentPrice = chosenSteamItem.currentPrice
        var profit = ((currentPrice - buyingPrice) * chosenSteamItem.amount)
        itemProfit.text = "${getString(R.string.profit_text)}  ${String.format("%.2f", profit)}${CurrencyData.currencyChar}"
    }
}