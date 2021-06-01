package com.example.steaminvestmentmanager.utilclasses;

import java.util.ArrayList;

public class CurrencyData {
    private static int currency;

    public static String getCurrencyChar() {
        switch (currency) {
            case 3:
                return "€";
            case 2:
                return "£";
            case 1:
            case 0:
                return "$";
            case 5:
            default:
                return "руб.";
        }
    }

    public static String getSpecificCurrencyChar(int currency) {
        switch (currency) {
            case 3:
                return "€";
            case 2:
                return "£";
            case 1:
            case 0:
                return "$";
            case 5:
            default:
                return "руб.";
        }
    }

    public static int getCurrency() {
        return currency;
    }

    public static String[] getCurrencyArray() {
        ArrayList<String> currencyArrayList = new ArrayList<>();
        Boolean check = false;
        currencyArrayList.add(getCurrencyChar());
        for (int i = 0; i < 6; i++) {
            if (!currencyArrayList.contains(getSpecificCurrencyChar(i))) {
                currencyArrayList.add(getSpecificCurrencyChar(i));
            }
        }
        String[] currencyArray = currencyArrayList.toArray(new String[0]);
        return currencyArray;
    }

    public static int getCurrencyFromChar(String currencyChar) {
        switch (currencyChar) {
            case ".руб":
                return 5;
            case "$":
                return 1;
            case "£":
                return 2;
            case "€":
                return 3;
            default:
                return 5;
        }
    }

    public static void setCurrency(int currency) {
        CurrencyData.currency = currency;
    }
}
