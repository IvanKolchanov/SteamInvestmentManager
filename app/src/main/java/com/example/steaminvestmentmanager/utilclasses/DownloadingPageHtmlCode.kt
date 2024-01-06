package com.example.steaminvestmentmanager.utilclasses

import java.net.URL
import java.net.URLConnection
import java.util.Scanner
import java.util.concurrent.Callable

class DownloadingPageHtmlCode(private val htmlPageUrl: String?) : Callable<String?> {
    override fun call(): String? {
        var content: String? = null
        val connection: URLConnection
        try {
            connection = URL(htmlPageUrl).openConnection()
            val scanner = Scanner(connection.getInputStream())
            scanner.useDelimiter("\\Z")
            content = scanner.next()
            scanner.close()
        } catch (ignored: Exception) {
        }
        return content
    }
}