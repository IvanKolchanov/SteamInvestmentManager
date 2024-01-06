package com.example.steaminvestmentmanager.utilclasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import java.util.concurrent.Callable

class DownloadBitmapImage(private val itemIconURL: String?, private val size: String) : Callable<Bitmap?> {
    override fun call(): Bitmap? {
        var icon: Bitmap? = null
        try {
            val `in` = URL(itemIconURL + size).openStream()
            icon = BitmapFactory.decodeStream(`in`)
        } catch (ignored: Exception) {
        }
        return icon
    }
}