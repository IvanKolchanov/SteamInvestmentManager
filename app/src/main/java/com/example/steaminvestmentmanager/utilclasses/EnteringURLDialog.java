package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.steaminvestmentmanager.ItemAddingThread;
import com.example.steaminvestmentmanager.MainActivity;
import com.example.steaminvestmentmanager.R;

import java.util.Objects;

public class EnteringURLDialog extends DialogFragment {

    private final MainActivity mainActivity;
    public EnteringURLDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.entering_url_dialog, null);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) rootView.findViewById(R.id.currencyText)).setText(CurrencyData.getCurrencyChar());
        rootView.findViewById(R.id.entered_url).requestFocus();
        EditText enteredURL = rootView.findViewById(R.id.entered_url);
        EditText enteredPrice = rootView.findViewById(R.id.entered_price);
        EditText enteredAmount = rootView.findViewById(R.id.entered_amount);
        rootView.findViewById(R.id.accept_dialog_button).setOnClickListener(v -> {
            String enteredURLText = enteredURL.getText().toString();
            String enteredPriceText = enteredPrice.getText().toString();
            String enteredAmountText = enteredAmount.getText().toString();
            try {
                ItemAddingThread itemAddingThread = new ItemAddingThread(enteredURLText, enteredPriceText, enteredAmountText, mainActivity);
                itemAddingThread.start();
                dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Неправильно введены данные", Toast.LENGTH_LONG).show();
            }
        });
        rootView.findViewById(R.id.cancel_dialog_button).setOnClickListener(v -> dismiss());
        return rootView;
    }
}
