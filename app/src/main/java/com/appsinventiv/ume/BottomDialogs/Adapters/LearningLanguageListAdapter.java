package com.appsinventiv.ume.BottomDialogs.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.EditProfile;
import com.appsinventiv.ume.Activities.Profile;
import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LearningLanguageListAdapter extends RecyclerView.Adapter<LearningLanguageListAdapter.ViewHolder> {
    Context context;
    List<LangaugeModel> itemList;
    private List<LangaugeModel> arrayList;


    public LearningLanguageListAdapter(Context context, List<LangaugeModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);

    }

    public void updateList(List<LangaugeModel> itemList) {
        this.itemList = itemList;
        arrayList.clear();
        arrayList.addAll(itemList);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arrayList);
        } else {
            for (LangaugeModel item : arrayList) {
                if (item.getLanguageName().toLowerCase().contains(charText)) {

                    itemList.add(item);
                }

            }


        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_language_item_layout, viewGroup, false);
        LearningLanguageListAdapter.ViewHolder viewHolder = new LearningLanguageListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LangaugeModel langaugeModel = itemList.get(i);
        viewHolder.countryName.setText(langaugeModel.getLanguageName());
        Glide.with(context).load(langaugeModel.getPicUrl()).into(viewHolder.flag);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        if (Profile.learningLanguages.contains(langaugeModel.getLanguageName())) {

                        } else {
                            Profile.learningLanguages.add(langaugeModel.getLanguageName());
                        }
                        if (EditProfile.learningLanguages.contains(langaugeModel.getLanguageName())) {

                        } else {
                            EditProfile.learningLanguages.add(langaugeModel.getLanguageName());
                        }
                    } else {
                        if (Profile.learningLanguages.contains(langaugeModel.getLanguageName())) {
                            Profile.learningLanguages.remove(EditProfile.learningLanguages.indexOf(langaugeModel.getLanguageName()));
                        } else {

                        }
                        if (EditProfile.learningLanguages.contains(langaugeModel.getLanguageName())) {
                            EditProfile.learningLanguages.remove(EditProfile.learningLanguages.indexOf(langaugeModel.getLanguageName()));
                        } else {

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        CheckBox checkbox;
        ImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}