package com.example.steaminvestmentmanager.utilclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.steaminvestmentmanager.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SteamItemAdapter extends ArrayAdapter<SteamItem> {

    public SteamItemAdapter(Context context, SteamItem[] steamItems) {
        super(context, R.layout.list_item, steamItems);
    }

    private void deleteCurrencySimbols(String lowestPrice) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final SteamItem steamItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
        itemIcon.setImageBitmap(steamItem.getItemIcon());
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(steamItem.getMarket_hash_name());
        TextView itemPercent = (TextView) convertView.findViewById(R.id.itemPercent);
        float starterPrice = Float.parseFloat(steamItem.getStarterPrice().replace(",", "."));
        String lowest_price = steamItem.getFirstInitializationCurrencyLowestPrice().replace(",", ".");
        String a = "\\$";
        lowest_price = lowest_price.replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
        float currentPrice = Float.parseFloat(lowest_price);
        float percentage = currentPrice/starterPrice;
        char[] percentageString = Float.toString(percentage).toCharArray();
        String percentageStringWithTwoNumbers = "";
        int counter = 0, haveSeenDot = 0;
        for (int i = 0; i < percentageString.length; i++) {
            if (percentageString[i] != '.') {
                if (haveSeenDot == 0) {
                    percentageStringWithTwoNumbers += Character.toString(percentageString[i]);
                }else if (counter < 3) {
                    percentageStringWithTwoNumbers += Character.toString(percentageString[i]);
                    counter++;
                }else {
                    break;
                }
            }else {
                percentageStringWithTwoNumbers += ".";
                haveSeenDot = 1;
            }
        }
        int finalPercentage = Math.round(Float.parseFloat(percentageStringWithTwoNumbers)  * 100);
        itemPercent.setText(Integer.toString(finalPercentage) + "%");
        if (finalPercentage > 100) {
            itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }else {
            itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }
        return convertView;
    }
}
