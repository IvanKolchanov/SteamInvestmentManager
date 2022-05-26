package com.example.steaminvestmentmanager.utilclasses;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class DownloadingPageHtmlCode implements Callable<String> {
    private String htmlPageUrl;
    private String content;

    public DownloadingPageHtmlCode(String htmlPageUrl) {
        this.htmlPageUrl = htmlPageUrl;
    }

    @Override
    public String call(){
        content = null;
        URLConnection connection;
        try {
            connection =  new URL(htmlPageUrl).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return content;
    }
}
