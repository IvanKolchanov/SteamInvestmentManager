package com.example.steaminvestmentmanager;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.steaminvestmentmanager.utilclasses.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String content;
    private ArrayList<SteamItem> steamItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSteamItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public static void setContent(String content) {
        MainActivity.content = content;
        System.out.println(content);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSteamItems();
    }

    private void initializeSteamItems() {
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
        for (int i = 1; i <= steamItems.size(); i++) {
            String itemJson = gson.toJson(steamItems.get(i-1));
            preferenceEditor.putString(Integer.toString(i), itemJson);
        }
        preferenceEditor.putInt("0", steamItems.size());
        preferenceEditor.apply();
    }
}