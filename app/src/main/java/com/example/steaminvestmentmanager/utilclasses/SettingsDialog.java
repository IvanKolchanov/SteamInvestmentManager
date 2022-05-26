package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.steaminvestmentmanager.MainActivity;
import com.example.steaminvestmentmanager.R;

import java.util.Objects;

public class SettingsDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_dialog, null);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner currencySpinner = rootView.findViewById(R.id.currencySpinner);
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(getContext(), R.layout.my_simple_spinner_item, CurrencyData.getCurrencyArray());
        currencyAdapter.setDropDownViewResource(R.layout.my_simple_drop_down_item);
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int newCurrency = CurrencyData.getCurrencyFromChar(CurrencyData.getCurrencyArray()[position]);
                CurrencyData.setCurrency(newCurrency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }
}
