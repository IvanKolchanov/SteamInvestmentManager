package com.example.steaminvestmentmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private static ArrayList<SteamItem> steamItems = new ArrayList<>();
    private ListView steamItemsListView;
    private final MainActivity mainActivity = this;
    private final String preferenceName = "savedData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSteamItemsFromPreference();
        steamItemsListView = findViewById(R.id.itemListView);
        ItemsUpdatingThread itemsUpdatingThread = new ItemsUpdatingThread(mainActivity);
        SteamItemsListViewData.setMainActivityContext(getApplicationContext());
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
        super.onStop();
        saveSteamItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setSteamItemsAdapter(SteamItemAdapter steamItemAdapter) {
        runOnUiThread(() -> {
            try {
                steamItemsListView.setAdapter(steamItemAdapter);
            }catch  (Exception ignored) { }
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
        switch (item.getItemId()) {
            case R.id.open_menu:
                showPopupMenu(findViewById(R.id.open_menu));
                break;
        }
        return true;
    }

    private void showPopupMenu(View v) {
        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        popupMenu.inflate(R.menu.user_popup_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.add_steamItem:
                    EnteringURLDialog enteringURLDialog = new EnteringURLDialog();
                    enteringURLDialog.show(getSupportFragmentManager(), null);
                    break;
                case R.id.open_settings:
                    SettingsDialog settingsDialog = new SettingsDialog();
                    settingsDialog.show(getSupportFragmentManager(), null);
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    public static void sendEnteredURL(String enteredURL, String enteredPrice, String enteredAmount) {
        ItemAddingThread itemAddingThread = new ItemAddingThread(enteredURL, enteredPrice, enteredAmount);
        itemAddingThread.start();
    }

    public static void sendNewSteamItem(SteamItem addingSteamItem) {
        steamItems.add(addingSteamItem);
    }

    public static void deleteSelectedItem(SteamItem chosenSteamItem) {
        steamItems.remove(chosenSteamItem);
    }

    public static void sendSteamItems(SteamItem[] steamItemArray) {
        if (steamItemArray != null) {
            for (int i = 0; i < steamItemArray.length; i++) {
                steamItems.get(i).setcurrentCurrencyLowestPrice(steamItemArray[i].getcurrentCurrencyLowestPrice());
                steamItems.get(i).setcurrentPrice(steamItemArray[i].getcurrentPrice());
            }
        }
    }

    public static SteamItem[] getSteamItems() {
        if (MainActivity.steamItems != null) {
            SteamItem[] steamItemsArray;
            steamItemsArray = MainActivity.steamItems.toArray(new SteamItem[0]);
            return steamItemsArray;
        }else return null;
    }

    private void getSteamItemsFromPreference() {
        if (false) {
            SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
            Gson gson = new Gson();
            int steamItemLength = sharedPreferences.getInt("0", 0);
            if (steamItemLength != 0) {
                for (int i = 1; i <= steamItemLength; i++) {
                    String jsonSteamItem = sharedPreferences.getString(Integer.toString(i), "");
                    SteamItem currentSteamItem = gson.fromJson(jsonSteamItem, SteamItem.class);
                    if (currentSteamItem != null) {
                        if (currentSteamItem.checkForBeingFull()) {
                            steamItems.add(currentSteamItem);
                        }
                    }
                }
            }
            int userCurrency = sharedPreferences.getInt("currency", 5);
            CurrencyData.setCurrency(userCurrency);
        }
    }

    private void saveSteamItems() {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        if (steamItems != null) {
            for (int i = 1; i <= steamItems.size(); i++) {
                steamItems.get(i - 1).setItemIcon(null);
                String itemJson = gson.toJson(steamItems.get(i - 1));
                preferenceEditor.putString(Integer.toString(i), itemJson);
            }
            preferenceEditor.putInt("0", steamItems.size());
        }
        preferenceEditor.putInt("currency", CurrencyData.getCurrency());
        preferenceEditor.apply();
    }
}