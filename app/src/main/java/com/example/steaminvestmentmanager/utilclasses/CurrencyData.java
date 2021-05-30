package com.example.steaminvestmentmanager.utilclasses;

public class CurrencyData {
    private static int currency = 5;

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

    public static void setCurrency(int currency) {
        CurrencyData.currency = currency;
    }
}
