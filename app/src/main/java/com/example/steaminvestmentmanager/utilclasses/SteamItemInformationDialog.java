package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.steaminvestmentmanager.MainActivity;
import com.example.steaminvestmentmanager.R;

import java.util.Locale;
import java.util.Objects;

public class SteamItemInformationDialog extends DialogFragment {
    private final SteamItem chosenSteamItem;
    private String text;

    public SteamItemInformationDialog(SteamItem chosenSteamItem) {
        this.chosenSteamItem = chosenSteamItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.steam_item_information_dialog, null);

        //настраиваю окно диалога
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //устанавливаю окно предмета
        ImageView itemIcon = rootView.findViewById(R.id.item_icon);
        itemIcon.setImageBitmap(chosenSteamItem.getItemIcon());

        //устанавливаю имя предмета
        TextView itemName = rootView.findViewById(R.id.item_name);
        itemName.setText(chosenSteamItem.getMarket_hash_name());

        //делаю всё для редактирования начальной цены
        ViewSwitcher buyingPriceViewSwitcher = rootView.findViewById(R.id.starter_price);
        buyingPriceViewSwitcher.showNext();
        ImageView pencilbuyingPrice = rootView.findViewById(R.id.change_view_pencil_price);
        pencilbuyingPrice.setImageDrawable(getResources().getDrawable(R.drawable.outline_create_24, null));
        TextView currencyText = rootView.findViewById(R.id.currency);
        currencyText.setText(CurrencyData.getCurrencyChar());
        pencilbuyingPrice.setOnClickListener(v -> {
            text = "";
            View currentSwitcherView = buyingPriceViewSwitcher.getCurrentView();
            if (currentSwitcherView instanceof TextView) {
                text = "" + ((TextView) buyingPriceViewSwitcher.getCurrentView()).getText();
                EditText priceEditText = buyingPriceViewSwitcher.findViewById(R.id.item_starter_priceEdit);
                priceEditText.setText(text);
                priceEditText.setOnKeyListener((v1, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        buyingPriceViewSwitcher.showNext();
                        TextView itemPriceCurrentTextView = (TextView) buyingPriceViewSwitcher.getCurrentView();
                        try {
                            itemPriceCurrentTextView.setText(priceEditText.getText());
                            chosenSteamItem.setBuyingPrice(Float.parseFloat(priceEditText.getText().toString()));
                            TextView profitView = rootView.findViewById(R.id.item_profit);
                            setProfit(profitView, chosenSteamItem);
                            return true;
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Неправельно введена цена", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    } else {
                        return false;
                    }
                });
            } else {
                EditText editText = (EditText) buyingPriceViewSwitcher.getCurrentView();
                try {
                    if (!editText.getText().toString().equals("")) {
                        float i = Float.parseFloat(editText.getText().toString().replace(",", "."));
                        ((TextView) buyingPriceViewSwitcher.findViewById(R.id.item_buy_price)).setText(String.format(Float.toString(i)));
                    }
                } catch (Exception e) {
                    text = (String) ((TextView) buyingPriceViewSwitcher.findViewById(R.id.item_amount)).getText();
                }
            }
            buyingPriceViewSwitcher.showNext();
            if (buyingPriceViewSwitcher.getCurrentView() instanceof EditText ed) {
                ed.requestFocus();
                ed.setSelection(ed.getText().length());
            }
        });

        //установка нынешней цены
        TextView itemCurrentPrice = rootView.findViewById(R.id.item_current_price);
        itemCurrentPrice.setText("Нынешняя цена:  " + chosenSteamItem.getCurrentPrice() + CurrencyData.getCurrencyChar());

        //установка и настройка редактирования amount
        ViewSwitcher viewSwitcher = rootView.findViewById(R.id.amount);
        viewSwitcher.showNext();
        TextView itemAmount = (TextView) viewSwitcher.getCurrentView();
        itemAmount.setText(String.format(Integer.toString(chosenSteamItem.getAmount())));
        ImageView pencilIcon = rootView.findViewById(R.id.change_view_pencil);
        pencilIcon.setImageDrawable(getResources().getDrawable(R.drawable.outline_create_24, null));
        pencilIcon.setOnClickListener(v -> {
            text = "";
            View currentSwitcherView = viewSwitcher.getCurrentView();
            if (currentSwitcherView instanceof TextView) {
                text = "" + ((TextView) viewSwitcher.getCurrentView()).getText();
                EditText amountEditText = viewSwitcher.findViewById(R.id.item_amountEdit);
                amountEditText.setText(text);
                amountEditText.setOnKeyListener((v12, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        viewSwitcher.showNext();
                        TextView itemAmountCurrentTextView = (TextView) viewSwitcher.getCurrentView();
                        try {
                            chosenSteamItem.setAmount(Integer.parseInt("" + amountEditText.getText()));
                            itemAmountCurrentTextView.setText(amountEditText.getText());
                            float buyingPrice = chosenSteamItem.getBuyingPrice();
                            float currentPrice = chosenSteamItem.getCurrentPrice();
                            TextView itemProfit = rootView.findViewById(R.id.item_profit);
                            String profit = Float.toString((currentPrice - buyingPrice) * chosenSteamItem.getAmount());
                            if (Float.parseFloat(profit) == (long) Float.parseFloat(profit)) {
                                profit = String.format("%s", (long) Float.parseFloat(profit));
                            } else {
                                profit = String.format(Locale.US, "%.2f", Float.parseFloat(profit));
                            }
                            itemProfit.setText("Прибыль:  " + profit + " " + CurrencyData.getCurrencyChar());
                        } catch (NumberFormatException e) {

                        }
                        return true;
                    } else {
                        return false;
                    }
                });
            } else {
                EditText editText = (EditText) viewSwitcher.getCurrentView();
                try {
                    if (!editText.getText().toString().equals("")) {
                        int i = Integer.parseInt(editText.getText().toString());
                        ((TextView) viewSwitcher.findViewById(R.id.item_amount)).setText(String.format(Integer.toString(i)));
                    }
                } catch (Exception e) {
                    text = (String) ((TextView) viewSwitcher.findViewById(R.id.item_amount)).getText();
                }
            }
            viewSwitcher.showNext();
            if (viewSwitcher.getCurrentView() instanceof EditText ed) {
                ed.requestFocus();
                ed.setSelection(ed.getText().length());
            }
        });

        //delete button
        Button steamItemDeleteButton = rootView.findViewById(R.id.item_delete_button);
        steamItemDeleteButton.setOnClickListener(v -> {
            MainActivity.deleteSelectedItem(chosenSteamItem);
            ItemListUpdater.getMainActivity().runOnUiThread(() -> MainActivity.steamItemAdapter.remove(chosenSteamItem));
            dismiss();
        });

        //устанавливаю цену покупки
        TextView buyingPriceText = buyingPriceViewSwitcher.findViewById(R.id.item_buy_price);
        String itemText = chosenSteamItem.getBuyingPrice().toString();
        buyingPriceText.setText(itemText);
        //устанавливаю прибыль
        TextView itemProfit = rootView.findViewById(R.id.item_profit);
        setProfit(itemProfit, chosenSteamItem);

        return rootView;
    }

    private void setProfit(TextView itemProfit, SteamItem chosenSteamItem) {
        float buyingPrice = chosenSteamItem.getBuyingPrice();
        float currentPrice = chosenSteamItem.getCurrentPrice();
        String profit = Float.toString((currentPrice - buyingPrice) * chosenSteamItem.getAmount());
        if (Float.parseFloat(profit) == (long) Float.parseFloat(profit)) {
            profit = String.format("%s", (long) Float.parseFloat(profit));
        } else {
            profit = String.format(Locale.US, "%.2f", Float.parseFloat(profit));
        }
        itemProfit.setText("Прибыль:  " + profit + CurrencyData.getCurrencyChar());
    }
}
