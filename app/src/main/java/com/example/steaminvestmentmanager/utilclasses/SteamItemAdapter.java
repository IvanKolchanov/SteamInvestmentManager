package com.example.steaminvestmentmanager.utilclasses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        return super.getView(position, convertView, parent);
    }
}
