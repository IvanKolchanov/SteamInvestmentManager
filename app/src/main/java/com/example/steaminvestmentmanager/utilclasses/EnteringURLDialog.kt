package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.steaminvestmentmanager.ItemAddingThread
import com.example.steaminvestmentmanager.MainActivity
import com.example.steaminvestmentmanager.R
import java.util.Objects

class EnteringURLDialog(private val mainActivity: MainActivity) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.entering_url_dialog, null)
        Objects.requireNonNull(dialog)?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        (rootView.findViewById<View>(R.id.currencyText) as TextView).text = CurrencyData.currencyChar
        rootView.findViewById<View>(R.id.entered_url).requestFocus()
        val enteredURL = rootView.findViewById<EditText>(R.id.entered_url)
        val enteredPrice = rootView.findViewById<EditText>(R.id.entered_price)
        val enteredAmount = rootView.findViewById<EditText>(R.id.entered_amount)
        rootView.findViewById<View>(R.id.accept_dialog_button).setOnClickListener { v: View? ->
            val enteredURLText = enteredURL.text.toString()
            val enteredPriceText = enteredPrice.text.toString()
            val enteredAmountText = enteredAmount.text.toString()
            try {
                val itemAddingThread = ItemAddingThread(enteredURLText, enteredPriceText, enteredAmountText, mainActivity)
                itemAddingThread.start()
                dismiss()
            } catch (e: Exception) {
                Toast.makeText(context, "Неправильно введены данные", Toast.LENGTH_LONG).show()
            }
        }
        rootView.findViewById<View>(R.id.cancel_dialog_button).setOnClickListener { v: View? -> dismiss() }
        return rootView
    }
}