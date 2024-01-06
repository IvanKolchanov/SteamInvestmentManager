package com.example.steaminvestmentmanager.utilclasses

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.steaminvestmentmanager.R
import java.util.Locale
import kotlin.math.roundToInt

class SteamItemAdapter(context: Context?, steamItems: ArrayList<SteamItem?>?) : ArrayAdapter<SteamItem?>(context!!, R.layout.list_item, steamItems!!) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val steamItem = getItem(position)
        val convertViewN: View = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.list_item, null)
        if (steamItem != null) {
            val itemIcon = convertViewN.findViewById<ImageView>(R.id.itemIcon)
            if (steamItem.itemIcon != null) itemIcon.setImageBitmap(steamItem.itemIcon)
            val itemName = convertViewN.findViewById<TextView>(R.id.itemName)
            itemName.text = steamItem.market_hash_name
            val itemPercent = convertViewN.findViewById<TextView>(R.id.itemPercent)
            val buyingPrice = steamItem.buyingPrice
            val currentPrice = steamItem.currentPrice
            val profitString: String
            val percentage = currentPrice / buyingPrice
            Log.d("SteamIvan", steamItem.toString())
            profitString = percentage.toString()
            val finalPercentage = (profitString.replace(",", ".").toFloat() * 100).roundToInt()
            itemPercent.text = "$finalPercentage%"
            if (finalPercentage > 100) {
                itemPercent.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                itemPercent.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }

        return convertViewN
    }
}