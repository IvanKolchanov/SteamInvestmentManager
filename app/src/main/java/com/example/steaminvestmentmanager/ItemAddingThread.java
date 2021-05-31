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
    private String enteredAmount, enteredPrice;
    private final int WRONG_WORD = 0, MARKET_HASH_NAME = 2, ICON_URL = 1;

    @Override
    public void run() {
        getItemHtmlPage();
        createSteamItem();
    }

    public ItemAddingThread(String steamItemURL, String enteredPrice, String enteredAmount) {
        this.steamItemURL = steamItemURL;
        this.enteredPrice = enteredPrice;
        this.enteredAmount = enteredAmount;
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
        boolean isFound = false;
        String g_rgAssetsLine = "";
        while (!isFound && cin.hasNext()) {
            String currentRow = cin.nextLine();
            String[] currentRowArray = currentRow.split(" ");
            if (currentRowArray.length > 1) {
                if (currentRowArray[1].equals("g_rgAssets")) {
                    g_rgAssetsLine = currentRow;
                    isFound = true;
                }
            }
            counter++;
        }
        cin.close();
        char[] g_rgAssetsLineArray = g_rgAssetsLine.toCharArray();
        processRgAssets(g_rgAssetsLineArray);
        addingItem = new SteamItem(market_hash_name, appid, icon_url, enteredPrice, enteredAmount);
        MainActivity.sendNewSteamItem(addingItem);
    }

    private void processRgAssets(char[] g_rgAssetsLineArray) {
        boolean isStillWord = false;
        int presentOperation = 0, howMuchWeFound = 0;
        String currentWord = "";
        for (int i = 0; i < g_rgAssetsLineArray.length; i++) {
            if (howMuchWeFound != 2) {
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
                                howMuchWeFound++;
                                break;
                            case ICON_URL:
                                icon_url += currentWord;
                                howMuchWeFound++;
                                break;
                        }
                        presentOperation = WRONG_WORD;
                        currentWord = "";
                    }else if (isStillWord && g_rgAssetsLineArray[i] != '"') {
                        currentWord += Character.toString(g_rgAssetsLineArray[i]);
                    }
                }
            }else {
                break;
            }
        }
    }
}
