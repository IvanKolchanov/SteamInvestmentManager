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

public class SteamItemAdapter extends ArrayAdapter<SteamItem> {

    public SteamItemAdapter(Context context, SteamItem[] steamItems) {
        super(context, R.layout.list_item, steamItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final SteamItem steamItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }
        if (steamItem.getFirstInitializationCurrencyLowestPrice() != null) {
            ImageView itemIcon = convertView.findViewById(R.id.itemIcon);
            itemIcon.setImageBitmap(steamItem.getItemIcon());
            TextView itemName = convertView.findViewById(R.id.itemName);
            itemName.setText(steamItem.getMarket_hash_name());
            TextView itemPercent = convertView.findViewById(R.id.itemPercent);
            float starterPrice = Float.parseFloat(steamItem.getStarterPrice().replace(",", "."));
            String profitString;
            if (steamItem.getFirstInitializationCurrency() == CurrencyData.getCurrency()) {
                String lowest_price = steamItem.getLowest_price().replace(",", ".");
                String a = "\\$";
                lowest_price = lowest_price.replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
                float currentPrice = Float.parseFloat(lowest_price);
                float percentage = currentPrice/starterPrice;
                if (percentage == (long) percentage) {
                    profitString = String.format("%s", (long) percentage);
                }else {
                    profitString = String.format("%.2f", percentage);
                }
            }else {
                String lowest_price = steamItem.getLowest_price().replace(",", ".");
                String a = "\\$";
                lowest_price = lowest_price.replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
                float currentPrice = Float.parseFloat(lowest_price);
                String lowest_rightCurrencyPrice;
                lowest_rightCurrencyPrice = lowest_price.replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
                float currentPriceRight = Float.parseFloat(lowest_rightCurrencyPrice);
                float divided = currentPriceRight/currentPrice;
                starterPrice = starterPrice / divided;
                float percentage = currentPrice / starterPrice;
                if (percentage == (long) percentage) {
                    profitString = String.format("%s", (long) percentage);
                }else {
                    profitString = String.format("%.2f", percentage);
                }
            }
            int finalPercentage = Math.round(Float.parseFloat(profitString.replace(",", "."))  * 100);
            itemPercent.setText(finalPercentage + "%");
            if (finalPercentage > 100) {
                itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            }else {
                itemPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }
        }

        return convertView;
    }
}
