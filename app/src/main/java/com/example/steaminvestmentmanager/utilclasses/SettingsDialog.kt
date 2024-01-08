package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.steaminvestmentmanager.R
import java.util.Objects

class SettingsDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.settings_dialog, null)
        Objects.requireNonNull(dialog)?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val currencySpinner = rootView.findViewById<Spinner>(R.id.currencySpinner)
        val currencyAdapter = context?.let { ArrayAdapter(it, R.layout.spinner_item, CurrencyData.currencyArray) }
        currencyAdapter?.setDropDownViewResource(R.layout.drop_down_item)
        currencySpinner.adapter = currencyAdapter
        currencySpinner.setSelection(CurrencyData.currencyArrayPosition)
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val newCurrency = CurrencyData.getCurrencyFromChar(CurrencyData.currencyArray[position])
                CurrencyData.setCurrency(newCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return rootView
    }
}