package com.example.steaminvestmentmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.steaminvestmentmanager.utilclasses.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<SteamItem> steamItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSteamItemsFromPreference();
        //steamItems.add(new SteamItem("Snakebite Case", "730", "https://community.cloudflare.steamstatic.com/economy/image/-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXU5A1PIYQNqhpOSV-fRPasw8rsUFJ5KBFZv668FFU4naLOJzgUuYqyzIaIxa6jMOLXxGkHvcMjibmU99Sg3Qaw-hA_ZWrzLISLMlhpgJJUhGE"));
        ItemAddingThread itemAddingThread = new ItemAddingThread("https://steamcommunity.com/market/listings/730/SG%20553%20%7C%20Heavy%20Metal%20%28Battle-Scarred%29?filter=-%20Link");
        ItemsUpdatingThread itemsUpdatingThread = new ItemsUpdatingThread();
        itemAddingThread.start();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSteamItems();
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_steamItem:
                        EnteringURLDialog enteringURLDialog = new EnteringURLDialog();
                        enteringURLDialog.show(getSupportFragmentManager(), null);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public static void sendEnteredURL(String enteredURL) {
        ItemAddingThread itemAddingThread = new ItemAddingThread(enteredURL);
    }

    public static void sendSteamItem(SteamItem addingSteamItem) {
        steamItems.add(addingSteamItem);
    }

    public static SteamItem[] getSteamItems() {
        if (MainActivity.steamItems != null) {
            SteamItem[] steamItemsArray = null;
            steamItemsArray = MainActivity.steamItems.toArray(new SteamItem[0]);
            return steamItemsArray;
        }else return null;
    }

    private void getSteamItemsFromPreference() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        int steamItemLength = sharedPreferences.getInt("0", 0);
        if (steamItemLength != 0) {
            for (int i = 1; i <= steamItemLength; i++) {
                String jsonSteamItem = sharedPreferences.getString(Integer.toString(i), "");
                steamItems.add(gson.fromJson(jsonSteamItem, SteamItem.class));
            }
        }
    }

    private void saveSteamItems() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
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
        preferenceEditor.apply();
    }
}