package com.example.steaminvestmentmanager.utilclasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.steaminvestmentmanager.MainActivity;
import com.example.steaminvestmentmanager.R;

import org.w3c.dom.EntityReference;
import org.w3c.dom.ls.LSOutput;

public class EnteringURLDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.entering_url_dialog, null);
        ((TextView) rootView.findViewById(R.id.currencyText)).setText(CurrencyData.getCurrencyChar());
        ((EditText) rootView.findViewById(R.id.entered_url)).requestFocus();
        EditText enteredURL = ((EditText) rootView.findViewById(R.id.entered_url));
        EditText enteredPrice = ((EditText) rootView.findViewById(R.id.entered_price));
        EditText enteredAmount = ((EditText) rootView.findViewById(R.id.entered_amount));
        rootView.findViewById(R.id.accept_dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredURLText = enteredURL.getText().toString();
                String enteredPriceText = enteredPrice.getText().toString();
                String enteredAmountText = enteredAmount.getText().toString();
                try {
                    Float.parseFloat(enteredPriceText);
                    Integer.parseInt(enteredAmountText);
                    MainActivity.sendEnteredURL(enteredURLText, enteredPriceText, enteredAmountText);
                    dismiss();
                }catch (Exception e) {
                    Toast.makeText(getContext(), "Неправильно введены данные", Toast.LENGTH_LONG).show();
                }
            }
        });
        rootView.findViewById(R.id.cancel_dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return rootView;
    }
}
