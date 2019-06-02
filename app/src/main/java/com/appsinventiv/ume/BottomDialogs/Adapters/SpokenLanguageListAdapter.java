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
import com.appsinventiv.ume.Activities.Search.Filters;
import com.appsinventiv.ume.Adapters.ChooseCountryAdapter;
import com.appsinventiv.ume.Models.Country;
import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpokenLanguageListAdapter extends RecyclerView.Adapter<SpokenLanguageListAdapter.ViewHolder> {
    Context context;
    List<LangaugeModel> itemList;
    private List<LangaugeModel> arrayList;

    onitemClick click;

    public SpokenLanguageListAdapter(Context context, List<LangaugeModel> itemList, onitemClick click) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);
        this.click=click;

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
        View view = LayoutInflater.from(context).inflate(R.layout.choose_country_item_layout, viewGroup, false);
        SpokenLanguageListAdapter.ViewHolder viewHolder = new SpokenLanguageListAdapter.ViewHolder(view);
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
                EditProfile.language = langaugeModel.getLanguageName();
                Profile.language = langaugeModel.getLanguageName();
                Filters.language = langaugeModel.getLanguageName();
                click.onItemClicked(langaugeModel);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        ImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }

    public interface onitemClick {
        public void onItemClicked(LangaugeModel langaugeModel);
    }


}
