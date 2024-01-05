package com.example.steaminvestmentmanager;

import android.util.Log;
import com.example.steaminvestmentmanager.utilclasses.DownloadingPageHtmlCode;
import com.example.steaminvestmentmanager.utilclasses.SteamItem;
import java.util.Scanner;

public class ItemAddingThread extends Thread{
    private final String steamItemURL;
    private String content;
    private String market_hash_name;
    private String icon_url = "https://community.cloudflare.steamstatic.com/economy/image/";
    private final String enteredAmount;
    private final String enteredPrice;

    private final MainActivity mainActivity;

    @Override
    public void run() {
        getItemHtmlPage();
        createSteamItem();
    }

    public ItemAddingThread(String steamItemURL, String enteredPrice, String enteredAmount, MainActivity mainActivity) {
        this.steamItemURL = steamItemURL;
        this.enteredPrice = enteredPrice;
        this.enteredAmount = enteredAmount;
        this.mainActivity = mainActivity;
    }

    private void getItemHtmlPage() {
        DownloadingPageHtmlCode downloadingPageHtmlCode = new DownloadingPageHtmlCode(steamItemURL);
        content = downloadingPageHtmlCode.call();
    }

    private void createSteamItem() {
        String[] urlArray = steamItemURL.split("/");
        String appid = urlArray[5];

        Scanner cin = new Scanner(content);
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
        }
        cin.close();
        char[] g_rgAssetsLineArray = g_rgAssetsLine.toCharArray();
        processRgAssets(g_rgAssetsLineArray);

        SteamItem addingItem = new SteamItem(market_hash_name, appid, icon_url, enteredPrice, enteredAmount);
        Log.d("SteamIvan", addingItem + "");
        addingItem.updateItemPrice();
        mainActivity.sendNewSteamItem(addingItem);
    }

    private void processRgAssets(char[] g_rgAssetsLineArray) {
        final int WRONG_WORD = 0, MARKET_HASH_NAME = 2, ICON_URL = 1;
        boolean isStillWord = false;
        int presentOperation = 0, howMuchWeFound = 0;
        String currentWord = "";
        for (char c : g_rgAssetsLineArray) {
            if (howMuchWeFound != 2) {
                if (presentOperation == WRONG_WORD) {
                    if (!isStillWord && c == '"') {
                        isStillWord = true;
                    } else if (isStillWord && c == '"') {
                        isStillWord = false;
                        switch (currentWord) {
                            case "market_hash_name" -> presentOperation = MARKET_HASH_NAME;
                            case "icon_url" -> presentOperation = ICON_URL;
                            default -> {
                            }
                        }
                        currentWord = "";
                    } else if (isStillWord) {
                        currentWord += Character.toString(c);
                    }
                } else {
                    if (!isStillWord && c == '"') {
                        isStillWord = true;
                    } else if (isStillWord && c == '"') {
                        isStillWord = false;
                        switch (presentOperation) {
                            case MARKET_HASH_NAME -> {
                                market_hash_name = currentWord;
                                howMuchWeFound++;
                            }
                            case ICON_URL -> {
                                icon_url += currentWord;
                                howMuchWeFound++;
                            }
                        }
                        presentOperation = WRONG_WORD;
                        currentWord = "";
                    } else if (isStillWord) {
                        currentWord += Character.toString(c);
                    }
                }
            } else {
                break;
            }
        }
    }
}
