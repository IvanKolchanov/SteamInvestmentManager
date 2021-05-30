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
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
        itemIcon.setImageBitmap(steamItem.getItemIcon());
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(steamItem.getMarket_hash_name());
        TextView itemPercent = (TextView) convertView.findViewById(R.id.itemPercent);

        return convertView;
    }
}
