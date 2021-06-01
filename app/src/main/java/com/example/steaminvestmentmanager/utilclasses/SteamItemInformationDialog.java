package com.example.steaminvestmentmanager.utilclasses;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.steaminvestmentmanager.MainActivity;
import com.example.steaminvestmentmanager.R;

import org.w3c.dom.Text;

public class SteamItemInformationDialog extends DialogFragment {
    private SteamItem chosenSteamItem;
    private String text;

    public SteamItemInformationDialog (SteamItem chosenSteamItem) {
        this.chosenSteamItem = chosenSteamItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.steam_item_information_dialog, null);

        //настраиваю окно диалога
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //устанавливаю окно предмета
        ImageView itemIcon = (ImageView) rootView.findViewById(R.id.item_icon);
        itemIcon.setImageBitmap(chosenSteamItem.getItemIcon());

        //устанавливаю имя предмета
        TextView itemName = (TextView) rootView.findViewById(R.id.item_name);
        itemName.setText(chosenSteamItem.getMarket_hash_name());

        //делаю всё для редактирования начальной цены
        ViewSwitcher starterPriceViewSwitcher = rootView.findViewById(R.id.starter_price);
        starterPriceViewSwitcher.showNext();
        ImageView pencilStarterPrice = rootView.findViewById(R.id.change_view_pencil_price);
        pencilStarterPrice.setImageDrawable(getResources().getDrawable(R.drawable.outline_create_24, null));
        pencilStarterPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //вывожу нацальную цену, профит, нынешнюю цену
        TextView starterPriceText = (TextView) starterPriceViewSwitcher.findViewById(R.id.item_buy_price);
        if (chosenSteamItem.getFirstInitializationCurrency() == CurrencyData.getCurrency()) {
            //устанавливаю цену покупки
            String itemText = chosenSteamItem.getStarterPrice() + " " + CurrencyData.getCurrencyChar();
            starterPriceText.setText(itemText);
            //устанавливаю прибыль
            TextView itemProfit = (TextView) rootView.findViewById(R.id.item_profit);
            float starterPrice = Float.parseFloat(chosenSteamItem.getStarterPrice().replace(",", ".")); //получение начальной цены
            String lowestRightCurrencyPrice = chosenSteamItem.getFirstInitializationCurrencyLowestPrice(); //получение начальной цены в правильных единицах
            lowestRightCurrencyPrice = lowestRightCurrencyPrice.replaceAll("pуб.", "").replaceAll("\\$", "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
            float rightCurrencyPrice = Float.parseFloat(lowestRightCurrencyPrice);
            String profit = Float.toString((rightCurrencyPrice - starterPrice) * chosenSteamItem.getAmount());
            if (Float.parseFloat(profit) == (long) Float.parseFloat(profit)) {
                profit = String.format("%s", (long) Float.parseFloat(profit));
            }else {
                profit = String.format("%.2f", Float.parseFloat(profit));
            }
            itemProfit.setText("Прибыль:  " + profit + " " + CurrencyData.getCurrencyChar());
        }else {
            String a = "\\$";
            float rightCurrencyPrice = Float.parseFloat(chosenSteamItem.getFirstInitializationCurrencyLowestPrice().replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", ""));
            float userCurrencyPrice = Float.parseFloat(chosenSteamItem.getLowest_price().replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", ""));
            float divided = rightCurrencyPrice/userCurrencyPrice;
            float starterPrice = Float.parseFloat(chosenSteamItem.getStarterPrice().replace(",", "."));
            starterPrice = starterPrice / divided;
            String starterPriceTextItem = "";
            if (starterPrice == (long) starterPrice) {
                 starterPriceTextItem = String.format("%s", (long) starterPrice);
            }else {
                starterPriceTextItem = String.format("%.2f", starterPrice);
            }
            starterPriceText.setText(starterPriceTextItem + " " + CurrencyData.getCurrencyChar());
            float profit = (userCurrencyPrice - starterPrice) * chosenSteamItem.getAmount();
            String profitString = Float.toString(profit);
            if (profit == (long) profit) {
                profitString = String.format("%s", (long) profit);
            }else {
                profitString = String.format("%.2f", profit);
            }
            TextView itemProfit = (TextView) rootView.findViewById(R.id.item_profit);
            itemProfit.setText("Прибыль:  " + profitString + " " + CurrencyData.getCurrencyChar());
        }

        //установка нынешней цены
        TextView itemCurrentPrice = (TextView) rootView.findViewById(R.id.item_current_price);
        itemCurrentPrice.setText("Нынешняя цена:  " + chosenSteamItem.getLowest_price());

        //установка и настройка редактирования amount
        ViewSwitcher viewSwitcher = rootView.findViewById(R.id.amount);
        viewSwitcher.showNext();
        TextView itemAmount = (TextView) viewSwitcher.getCurrentView();
        itemAmount.setText(Integer.toString(chosenSteamItem.getAmount()));
        ImageView pencilIcon = rootView.findViewById(R.id.change_view_pencil);
        pencilIcon.setImageDrawable(getResources().getDrawable(R.drawable.outline_create_24, null));
        pencilIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = "";
                View currentSwitcherView = viewSwitcher.getCurrentView();
                if (currentSwitcherView instanceof TextView) {
                    text = "" + ((TextView) viewSwitcher.getCurrentView()).getText();
                    EditText amountEditText = (EditText) viewSwitcher.findViewById(R.id.item_amountEdit);
                    amountEditText.setText(text);
                    amountEditText.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == event.KEYCODE_ENTER) {
                                viewSwitcher.showNext();
                                TextView itemAmountCurrentTextView = (TextView) viewSwitcher.getCurrentView();
                                try {
                                    chosenSteamItem.setAmount(Integer.parseInt("" + amountEditText.getText()));
                                    itemAmountCurrentTextView.setText(amountEditText.getText());
                                    if (chosenSteamItem.getFirstInitializationCurrency() == CurrencyData.getCurrency()) {
                                        //устанавливаю прибыль
                                        TextView itemProfit = (TextView) rootView.findViewById(R.id.item_profit);
                                        float starterPrice = Float.parseFloat(chosenSteamItem.getStarterPrice().replace(",", ".")); //получение начальной цены
                                        String lowestRightCurrencyPrice = chosenSteamItem.getFirstInitializationCurrencyLowestPrice(); //получение начальной цены в правильных единицах
                                        lowestRightCurrencyPrice = lowestRightCurrencyPrice.replaceAll("pуб.", "").replaceAll("\\$", "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", "");
                                        float rightCurrencyPrice = Float.parseFloat(lowestRightCurrencyPrice);
                                        String profit = Float.toString((rightCurrencyPrice - starterPrice) * chosenSteamItem.getAmount());
                                        if (Float.parseFloat(profit) == (long) Float.parseFloat(profit)) {
                                            profit = String.format("%s", (long) Float.parseFloat(profit));
                                        }else {
                                            profit = String.format("%.2f", Float.parseFloat(profit));
                                        }
                                        itemProfit.setText("Прибыль:  " + profit + " " + CurrencyData.getCurrencyChar());
                                    }else {
                                        String a = "\\$";
                                        float rightCurrencyPrice = Float.parseFloat(chosenSteamItem.getFirstInitializationCurrencyLowestPrice().replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", ""));
                                        float userCurrencyPrice = Float.parseFloat(chosenSteamItem.getLowest_price().replaceAll("pуб.", "").replaceAll(a, "").replaceAll(",", ".").replaceAll("€", "").replaceAll("£", ""));
                                        float divided = rightCurrencyPrice/userCurrencyPrice;
                                        float starterPrice = Float.parseFloat(chosenSteamItem.getStarterPrice().replace(",", "."));
                                        starterPrice = starterPrice / divided;
                                        float profit = (userCurrencyPrice - starterPrice) * chosenSteamItem.getAmount();
                                        String profitString = Float.toString(profit);
                                        if (profit == (long) profit) {
                                            profitString = String.format("%s", (long) profit);
                                        }else {
                                            profitString = String.format("%.2f", profit);
                                        }
                                        TextView itemProfit = (TextView) rootView.findViewById(R.id.item_profit);
                                        itemProfit.setText("Прибыль:  " + profitString + " " + CurrencyData.getCurrencyChar());
                                    }
                                }catch (NumberFormatException e) {

                                }
                                return true;
                            }else {
                                return false;
                            }
                        }
                    });
                }else {
                    EditText editText = (EditText) viewSwitcher.getCurrentView();
                    try {
                        if (editText.getText().toString() != "") {
                            int i = Integer.parseInt(editText.getText().toString());
                            ((TextView) viewSwitcher.findViewById(R.id.item_amount)).setText(Integer.toString(i));
                        }
                    }catch (Exception e) {
                        text = (String) ((TextView) viewSwitcher.findViewById(R.id.item_amount)).getText();
                    }
                }
                viewSwitcher.showNext();
                if (viewSwitcher.getCurrentView() instanceof EditText) {
                    EditText ed = ((EditText) viewSwitcher.getCurrentView());
                    ed.requestFocus();
                    ed.setSelection(ed.getText().length());
                }
            }
        });
        Button steamItemDeleteButton = (Button) rootView.findViewById(R.id.item_delete_button);
        steamItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.deleteSelectedItem(chosenSteamItem);
                dismiss();
            }
        });
        return rootView;
    }
}
