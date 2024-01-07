package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.steaminvestmentmanager.R
import org.w3c.dom.Text

class CurrencyExchangeDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.currency_exchange_dialog, null)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val text1 = rootView.findViewById<TextView>(R.id.currency_exchange_text1)
        val text2 = rootView.findViewById<TextView>(R.id.currency_exchange_text2)
        val text3 = rootView.findViewById<TextView>(R.id.currency_exchange_text3)
        setDefaultText(text1, text2, text3)

        val changeImage = rootView.findViewById<ImageView>(R.id.change_exchange_button)
        changeImage.setOnClickListener {
            if (text1.text[1] == text2.text[1]) setAlternativeText(text1, text2, text3)
            else setDefaultText(text1, text2, text3)
        }
        return rootView
    }

    private fun setDefaultText(text1: TextView, text2: TextView, text3: TextView) {
        var count = 0
        val currencyChar = CurrencyData.currencyChar
        val currencyPos = CurrencyData.getCurrencyFromChar(currencyChar)
        for (i in CurrencyData.currencyArray) {
            if (currencyChar != i) {
                val iPos = CurrencyData.getCurrencyFromChar(i)
                Log.d("SteamIvan", "${CurrencyData.exchangeRatios[currencyPos]}")
                val ratio = CurrencyData.exchangeRatios[currencyPos]?.div(CurrencyData.exchangeRatios[iPos]!!)
                if (count == 0) text1.text = "1$currencyChar = ${String.format("%.2f", ratio)}$i"
                if (count == 1) text2.text = "1$currencyChar = ${String.format("%.2f", ratio)}$i"
                if (count == 2) text3.text = "1$currencyChar = ${String.format("%.2f", ratio)}$i"
                count++
            }
        }
    }

    private fun setAlternativeText(text1: TextView, text2: TextView, text3: TextView) {
        var count = 0
        val currencyChar = CurrencyData.currencyChar
        val currencyPos = CurrencyData.getCurrencyFromChar(currencyChar)
        for (i in CurrencyData.currencyArray) {
            if (currencyChar != i) {
                val iPos = CurrencyData.getCurrencyFromChar(i)
                Log.d("SteamIvan", "${CurrencyData.exchangeRatios[currencyPos]}")
                val ratio = CurrencyData.exchangeRatios[iPos]?.div(CurrencyData.exchangeRatios[currencyPos]!!)
                if (count == 0) text1.text = "1$i = ${String.format("%.2f", ratio)}$currencyChar"
                if (count == 1) text2.text = "1$i = ${String.format("%.2f", ratio)}$currencyChar"
                if (count == 2) text3.text = "1$i = ${String.format("%.2f", ratio)}$currencyChar"
                count++
            }
        }
    }
}