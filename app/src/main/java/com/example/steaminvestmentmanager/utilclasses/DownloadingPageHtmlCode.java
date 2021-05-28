package com.example.steaminvestmentmanager.utilclasses;

import com.example.steaminvestmentmanager.MainActivity;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class DownloadingPageHtmlCode extends Thread {
    private String htmlPageUrl;
    private String content;

    public DownloadingPageHtmlCode(String htmlPageUrl) {
        this.htmlPageUrl = htmlPageUrl;
    }

    @Override
    public void run(){
        content = null;
        URLConnection connection = null;
        try {
            connection =  new URL("https://steamcommunity.com/market/listings/730/Snakebite%20Case").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        MainActivity.setContent(content);
    }
}
