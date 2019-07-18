package com.umetechnologypvt.ume.BottomDialogs.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Models.LangaugeModel;
import com.umetechnologypvt.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LearningLanguageListAdapter extends RecyclerView.Adapter<LearningLanguageListAdapter.ViewHolder> {
    Context context;
    List<LangaugeModel> itemList;
    private List<LangaugeModel> arrayList;
    List<String> learningLanguage;

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    public LearningLanguageListAdapter(Context context, List<LangaugeModel> itemList, List<String> learningLanguage) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);
        this.learningLanguage = learningLanguage;

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
    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if (itemList.get(position).isSection()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.choose_language_item_layout, viewGroup, false);
//        LearningLanguageListAdapter.ViewHolder viewHolder = new LearningLanguageListAdapter.ViewHolder(view);
//        return viewHolder;
        LearningLanguageListAdapter.ViewHolder viewHolder;
        if (viewType == SECTION_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.lang_header_title, parent, false);
            viewHolder = new LearningLanguageListAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.choose_language_item_layout, parent, false);
            viewHolder = new LearningLanguageListAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LangaugeModel langaugeModel = itemList.get(i);
        viewHolder.headerTitleTextview.setText(String.valueOf(langaugeModel.getLanguageName().charAt(0)).toUpperCase());

        boolean flag = false;
        for (int j = 0; j < learningLanguage.size(); j++) {
            if (langaugeModel.getLanguageName().equalsIgnoreCase(learningLanguage.get(j))) {
                flag = true;
            }
        }

        if (flag) {
            viewHolder.checkbox.setChecked(true);
        } else {
            viewHolder.checkbox.setChecked(false);
        }


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
        TextView countryName, headerTitleTextview;
        CheckBox checkbox;
        CircleImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
            checkbox = itemView.findViewById(R.id.checkbox);
            headerTitleTextview = itemView.findViewById(R.id.headerTitleTextview);

        }
    }

}
