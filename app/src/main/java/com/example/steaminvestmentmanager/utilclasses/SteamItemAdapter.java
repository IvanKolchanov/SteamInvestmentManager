package com.example.steaminvestmentmanager.utilclasses;

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

import java.util.ArrayList;
import java.util.Locale;

public class SteamItemAdapter extends ArrayAdapter<SteamItem> {

    public SteamItemAdapter(Context context, ArrayList<SteamItem> steamItems) {
        super(context, R.layout.list_item, steamItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final SteamItem steamItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }
        if (steamItem.getCurrentCurrencyLowestPrice() != null) {
            ImageView itemIcon = convertView.findViewById(R.id.itemIcon);
            if (steamItem.getItemIcon() != null) itemIcon.setImageBitmap(steamItem.getItemIcon());

            TextView itemName = convertView.findViewById(R.id.itemName);
            itemName.setText(steamItem.getMarket_hash_name());
            TextView itemPercent = convertView.findViewById(R.id.itemPercent);
            float buyingPrice = steamItem.getBuyingPrice(), currentPrice = steamItem.getCurrentPrice();
            String profitString;
            float percentage = currentPrice / buyingPrice;
            if (percentage == (long) percentage) {
                profitString = String.format("%s", (long) percentage);
            } else {
                profitString = String.format(Locale.US, "%.2f", percentage);
            }

            int finalPercentage = Math.round(Float.parseFloat(profitString.replace(",", ".")) * 100);
            itemPercent.setText(finalPercentage + "%");
            if (finalPercentage > 100) {
                itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else {
                itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }
        }

        return convertView;
    }
}
