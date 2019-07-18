package com.umetechnologypvt.ume.BottomDialogs;

import android.app.Dialog;
import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Adapters.ChooseCountryAdapter;
import com.umetechnologypvt.ume.BottomDialogs.Adapters.ChooseInterestListAdapter;
import com.umetechnologypvt.ume.BottomDialogs.Adapters.ChooseSingleInterestListAdapter;
import com.umetechnologypvt.ume.BottomDialogs.Adapters.LearningLanguageListAdapter;
import com.umetechnologypvt.ume.BottomDialogs.Adapters.SpokenLanguageListAdapter;
import com.umetechnologypvt.ume.Models.Country;
import com.umetechnologypvt.ume.Models.LangaugeModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        ArrayList<LangaugeModel> langaugeModelList=new ArrayList<>();



        Collections.sort(languageList, new Comparator<LangaugeModel>() {
            @Override
            public int compare(LangaugeModel user1, LangaugeModel user2) {
                return String.valueOf(user1.getLanguageName().charAt(0)).toUpperCase().compareTo(String.valueOf(user2.getLanguageName().charAt(0)).toUpperCase());
            }
        });
        String lastHeader = "";
        for (int i = 0; i < CommonUtils.languageList().size(); i++) {

            LangaugeModel langaugeModel = languageList.get(i);
            String header = String.valueOf(langaugeModel.getLanguageName().charAt(0)).toUpperCase();

            if (!lastHeader.equalsIgnoreCase(header)) {
                lastHeader = header;
                langaugeModelList.add(new LangaugeModel(
                        langaugeModel.getLanguageName(),
                        langaugeModel.getLangCode(),
                        langaugeModel.getCountryCode(),
                        langaugeModel.getPicDrawable(), true));
            }


            langaugeModelList.add(langaugeModel);
        }


        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        SpokenLanguageListAdapter adapter = new SpokenLanguageListAdapter(context,langaugeModelList, new SpokenLanguageListAdapter.onitemClick() {
            @Override
            public void onItemClicked(LangaugeModel langaugeModel) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        recyclerview.setAdapter(adapter);
        adapter.updateList(langaugeModelList);

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
        List<LangaugeModel> langaugeModelList = new ArrayList<>();

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


        Collections.sort(languageList, new Comparator<LangaugeModel>() {
            @Override
            public int compare(LangaugeModel user1, LangaugeModel user2) {
                return String.valueOf(user1.getLanguageName().charAt(0)).toUpperCase().compareTo(String.valueOf(user2.getLanguageName().charAt(0)).toUpperCase());
            }
        });
        String lastHeader = "";
        for (int i = 0; i < languageList.size(); i++) {

            LangaugeModel langaugeModel = languageList.get(i);
            String header = String.valueOf(langaugeModel.getLanguageName().charAt(0)).toUpperCase();

            if (!lastHeader.equalsIgnoreCase(header)) {
                lastHeader = header;
                langaugeModelList.add(new LangaugeModel(
                        langaugeModel.getLanguageName(),
                        langaugeModel.getLangCode(),
                        langaugeModel.getCountryCode(),
                        langaugeModel.getPicDrawable(), true));
            }


            langaugeModelList.add(langaugeModel);
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        LearningLanguageListAdapter adapter = new LearningLanguageListAdapter(context, langaugeModelList, SharedPrefs.getUserModel().getLearningLanguage());
        recyclerview.setAdapter(adapter);
        adapter.updateList(langaugeModelList);

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
        List<Country> newCountryList = new ArrayList<>();
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

        Collections.sort(countyList, new Comparator<Country>() {
            @Override
            public int compare(Country user1, Country user2) {
                return String.valueOf(user1.getCountryName().charAt(0)).toUpperCase().compareTo(String.valueOf(user2.getCountryName().charAt(0)).toUpperCase());
            }
        });
        String lastHeader = "";
        for (int i = 0; i < countyList.size(); i++) {

            Country country = countyList.get(i);
            String header = String.valueOf(country.getCountryName().charAt(0)).toUpperCase();

            if (!lastHeader.equalsIgnoreCase(header)) {
                lastHeader = header;
                newCountryList.add(new Country(country.getCountryName(), country.getPicUrl(), country.getCountryCode(), true));
            }


            newCountryList.add(country);
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ChooseCountryAdapter adapter = new ChooseCountryAdapter(context, newCountryList, new ChooseCountryAdapter.onitemClick() {
            @Override
            public void onItemClicked(Country country) {
                dialog.dismiss();
                callbacks.onOkPressed();
            }
        });
        recyclerview.setAdapter(adapter);
        adapter.updateList(newCountryList);

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

    public static void showInterestDialog(Context context, ArrayList<String> interestList, List<String> userInterest, DialogCallbacks callbacks) {

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
        ChooseInterestListAdapter adapter = new ChooseInterestListAdapter(context, interestList, userInterest);
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
