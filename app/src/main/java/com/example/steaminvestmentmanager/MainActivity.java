package com.example.steaminvestmentmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
//import com.fasterxml.jackson.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.steaminvestmentmanager.utilclasses.*;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final ArrayList<SteamItem> steamItems = new ArrayList<>();

    private static final Gson gson = new Gson();

    public static SteamItemAdapter steamItemAdapter;
    private ListView steamItemsListView;
    private final MainActivity mainActivity = this;
    private final String preferenceName = "savedDataSIM";

    private final String TAG = "SteamIvan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        steamItemsListView = findViewById(R.id.itemListView);
        steamItemAdapter = new SteamItemAdapter(getApplicationContext(), new ArrayList<>());
        setSteamItemsAdapter(steamItemAdapter);

        try {
            getSteamItemsFromPreference();
        }catch (Exception e) {
            Log.d(TAG, "onCreate: " + e);
        }

        ItemListUpdater.setMainActivity(this);
        ItemsUpdatingThread itemsUpdatingThread = new ItemsUpdatingThread();
        itemsUpdatingThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        saveSteamItems();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setSteamItemsAdapter(SteamItemAdapter steamItemAdapter) {
        runOnUiThread(() -> {
            try {
                steamItemsListView.setAdapter(steamItemAdapter);
            } catch (Exception ignored) {
            }
            steamItemsListView.setOnItemClickListener((parent, view, position, id) -> {
                SteamItem steamItem = Objects.requireNonNull(getSteamItems())[position];
                SteamItemInformationDialog steamItemInformationDialog = new SteamItemInformationDialog(steamItem);
                steamItemInformationDialog.show(getSupportFragmentManager(), null);
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.open_menu) {
            showPopupMenu(findViewById(R.id.open_menu));
        }
        return true;
    }

    private void showPopupMenu(View v) {
        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.inflate(R.menu.user_popup_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_steamItem) {
                EnteringURLDialog enteringURLDialog = new EnteringURLDialog(this);
                enteringURLDialog.show(getSupportFragmentManager(), null);
                return true;
            }
            if (item.getItemId() == R.id.open_settings) {
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getSupportFragmentManager(), null);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public void sendNewSteamItem(SteamItem addingSteamItem) {
        steamItems.add(addingSteamItem);
        runOnUiThread(() -> steamItemAdapter.add(addingSteamItem));
    }

    public static void deleteSelectedItem(SteamItem chosenSteamItem) {
        steamItems.remove(chosenSteamItem);
    }

    public static SteamItem[] getSteamItems() {
        SteamItem[] steamItemsArray;
        steamItemsArray = MainActivity.steamItems.toArray(new SteamItem[0]);
        return steamItemsArray;
    }

    private void clearSP() {
        getSharedPreferences(preferenceName, MODE_PRIVATE).edit().clear().apply();
    }

    private void getSteamItemsFromPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
        int steamItemLength = sharedPreferences.getInt("NUMBER_OF_STEAM_ITEMS", 0);
        if (steamItemLength != 0) {
            for (int i = 1; i <= steamItemLength; i++) {
                String jsonSteamItem = sharedPreferences.getString("item" + i, "");
                SteamItem currentSteamItem = gson.fromJson(jsonSteamItem, SteamItem.class);
                if (currentSteamItem != null && currentSteamItem.checkForBeingFull()) {
                    new Thread(currentSteamItem::updateIcon).start();
                    steamItems.add(currentSteamItem);
                    steamItemAdapter.add(currentSteamItem);
                }
            }
        }
        int userCurrency = sharedPreferences.getInt("USER_CURRENCY", 5);
        CurrencyData.setInitialCurrency(userCurrency);
    }

    private void saveSteamItems() {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.clear();
        if (steamItems != null) {
            for (int i = 1; i <= steamItems.size(); i++) {
                steamItems.get(i - 1).setItemIcon(null);
                String itemJson = gson.toJson(steamItems.get(i - 1));
                preferenceEditor.putString("item" + i, itemJson);
            }
            preferenceEditor.putInt("NUMBER_OF_STEAM_ITEMS", steamItems.size());
        }
        preferenceEditor.putInt("USER_CURRENCY", CurrencyData.getCurrency());
        preferenceEditor.apply();
    }
}