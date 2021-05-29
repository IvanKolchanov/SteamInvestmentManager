package com.example.steaminvestmentmanager;

import android.graphics.Bitmap;

import com.example.steaminvestmentmanager.utilclasses.DownloadBitmapImage;
import com.example.steaminvestmentmanager.utilclasses.DownloadingPageHtmlCode;
import com.example.steaminvestmentmanager.utilclasses.SteamItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Scanner;

public class ItemAddingThread extends Thread{
    private SteamItem addingItem;
    private String steamItemURL;
    private String content;
    private String appid;
    private String market_hash_name;
    private String icon_url = "https://community.cloudflare.steamstatic.com/economy/image/";
    private Bitmap steamItemIcon = null;
    private final int WRONG_WORD = 0, MARKET_HASH_NAME = 2, ICON_URL = 1;

    @Override
    public void run() {
        getItemHtmlPage();
        createSteamItem();
    }

    public ItemAddingThread(String steamItemURL) {
        this.steamItemURL = steamItemURL;
    }

    private void getItemHtmlPage() {
        DownloadingPageHtmlCode downloadingPageHtmlCode = new DownloadingPageHtmlCode(steamItemURL);
        content = downloadingPageHtmlCode.call();
    }

    private void createSteamItem() {
        String[] urlArray = steamItemURL.split("/");
        appid = urlArray[5];
        Scanner cin = new Scanner(content);
        int counter = 1;
        while (counter != 605) {
            cin.nextLine();
            counter++;
        }
        String g_rgAssetsLine = cin.nextLine();
        cin.close();
        char[] g_rgAssetsLineArray = g_rgAssetsLine.toCharArray();
        processRgAssets(g_rgAssetsLineArray);
        DownloadBitmapImage downloadBitmapImage = new DownloadBitmapImage(icon_url, "/48fx48f");
        try {
            steamItemIcon = downloadBitmapImage.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addingItem = new SteamItem(market_hash_name, appid, icon_url, steamItemIcon);
        MainActivity.sendSteamItem(addingItem);
    }

    private void processRgAssets(char[] g_rgAssetsLineArray) {
        boolean isStillWord = false;
        int presentOperation = 0;
        String currentWord = "";
        for (int i = 0; i < g_rgAssetsLineArray.length; i++) {
            if (presentOperation == WRONG_WORD) {
                if (!isStillWord && g_rgAssetsLineArray[i] == '"') {
                    isStillWord = true;
                }else if (isStillWord && g_rgAssetsLineArray[i] == '"') {
                    isStillWord = false;
                    switch (currentWord) {
                        case "market_hash_name":
                            presentOperation = MARKET_HASH_NAME;
                            break;
                        case "icon_url":
                            presentOperation = ICON_URL;
                            break;
                        default:
                            break;
                    }
                    currentWord = "";
                }else if (isStillWord && g_rgAssetsLineArray[i] != '"') {
                    currentWord += Character.toString(g_rgAssetsLineArray[i]);
                }
            }else {
                if (!isStillWord && g_rgAssetsLineArray[i] == '"') {
                    isStillWord = true;
                }else if (isStillWord && g_rgAssetsLineArray[i] == '"') {
                    isStillWord = false;
                    switch (presentOperation) {
                        case MARKET_HASH_NAME:
                            market_hash_name = currentWord;
                            break;
                        case ICON_URL:
                            icon_url += currentWord;
                            break;
                    }
                    presentOperation = WRONG_WORD;
                    currentWord = "";
                }else if (isStillWord && g_rgAssetsLineArray[i] != '"') {
                    currentWord += Character.toString(g_rgAssetsLineArray[i]);
                }
            }
        }
        System.out.println(market_hash_name + " " + icon_url);
    }
}
