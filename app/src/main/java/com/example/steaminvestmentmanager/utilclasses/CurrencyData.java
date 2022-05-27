package com.example.steaminvestmentmanager.utilclasses;

import java.util.HashMap;

public class CurrencyData {
    private static int currency;
    private static String[] currencies = new String[]{"$", "$", "£", "€", "", "руб."};

    public static String getCurrencyChar() {
        try {
            return currencies[currency];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "$";
        }
    }

    public static String getSpecificCurrencyChar(int currency) {
        try {
            return currencies[currency];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "$";
        }
    }

    public static int getCurrency() {
        return currency;
    }

    public static String[] getCurrencyArray() {
        return new String[] {"$", "£", "€", "руб."};
    }

    public static int getCurrencyFromChar(String currencyString) {
        for (int i = 0; i < currencies.length; i++) {
            if (currencyString.equals(currencies[i])) return i;
        }
        return 0;
    }

    public static void setCurrency(int currency) {
        CurrencyData.currency = currency;
    }
}
