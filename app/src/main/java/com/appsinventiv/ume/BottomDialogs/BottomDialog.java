package com.appsinventiv.ume.BottomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.appsinventiv.ume.Activities.Search.Filters;
import com.appsinventiv.ume.Adapters.ChooseCountryAdapter;
import com.appsinventiv.ume.BottomDialogs.Adapters.ChooseInterestListAdapter;
import com.appsinventiv.ume.BottomDialogs.Adapters.ChooseSingleInterestListAdapter;
import com.appsinventiv.ume.BottomDialogs.Adapters.LearningLanguageListAdapter;
import com.appsinventiv.ume.BottomDialogs.Adapters.SpokenLanguageListAdapter;
import com.appsinventiv.ume.Models.Country;
import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class BottomDialog {
    private BottomDialog() {
    }

    public static void showSpokenDialog(Context context, List<LangaugeModel> languageList, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.languages_bottom_sheet_option, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        SpokenLanguageListAdapter adapter = new SpokenLanguageListAdapter(context, languageList, new SpokenLanguageListAdapter.onitemClick() {
            @Override
            public void onItemClicked(LangaugeModel langaugeModel) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        recyclerview.setAdapter(adapter);
        adapter.updateList(languageList);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }

    public static void showAgeRangePicker(Context context, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.age_range_bottom_sheet_option, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        NumberPicker numberPicker1 = customView.findViewById(R.id.numberPicker1);
        NumberPicker numberPicker2 = customView.findViewById(R.id.numberPicker2);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);


        String[] data = new String[]{"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70"};
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(data.length - 1);
        numberPicker1.setDisplayedValues(data);

        String[] data1 = new String[]{"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70"};
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(data1.length - 1);
        numberPicker2.setDisplayedValues(data1);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filters.startAge = data[numberPicker1.getValue()];
                Filters.endAge = data1[numberPicker2.getValue()];
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }

    public static void showLanguagesDialog(Context context, List<LangaugeModel> languageList, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.languages_bottom_sheet_option, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        LearningLanguageListAdapter adapter = new LearningLanguageListAdapter(context, languageList);
        recyclerview.setAdapter(adapter);
        adapter.updateList(languageList);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }

    public static void showCountiesDialog(Context context, List<Country> countyList, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.country_bottom_sheet_option, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ChooseCountryAdapter adapter = new ChooseCountryAdapter(context, countyList, new ChooseCountryAdapter.onitemClick() {
            @Override
            public void onItemClicked(Country country) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        recyclerview.setAdapter(adapter);
        adapter.updateList(countyList);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }

    public static void showInterestDialog(Context context, ArrayList<String> interestList, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.interest_bottom_sheet_option, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ChooseInterestListAdapter adapter = new ChooseInterestListAdapter(context, interestList);
        recyclerview.setAdapter(adapter);
        adapter.updateList(interestList);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }

    public static void showSingleInterestDialog(Context context, ArrayList<String> interestList, DialogCallbacks callbacks) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.interest_bottom_sheet_option, null);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        Button cancel = customView.findViewById(R.id.cancel);
        Button ok = customView.findViewById(R.id.ok);

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ChooseSingleInterestListAdapter adapter = new ChooseSingleInterestListAdapter(context, interestList, new ChooseSingleInterestListAdapter.onitemClick() {
            @Override
            public void onItemClicked(String name) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        recyclerview.setAdapter(adapter);
        adapter.updateList(interestList);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(search.getText().toString());
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callbacks.onCancelled();
            }
        });
        dialog.show();


    }
}
