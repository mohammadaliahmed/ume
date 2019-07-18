package com.umetechnologypvt.ume.BottomDialogs.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Models.LangaugeModel;
import com.umetechnologypvt.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpokenLanguageListAdapter extends RecyclerView.Adapter<SpokenLanguageListAdapter.ViewHolder> {
    Context context;
    List<LangaugeModel> itemList;
    private List<LangaugeModel> arrayList;

    onitemClick click;
    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

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
//        View view = LayoutInflater.from(context).inflate(R.layout.choose_country_item_layout, viewGroup, false);
//        SpokenLanguageListAdapter.ViewHolder viewHolder = new SpokenLanguageListAdapter.ViewHolder(view);
//        return viewHolder;
        SpokenLanguageListAdapter.ViewHolder viewHolder;
        if (viewType == SECTION_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.spok_lang_header_title, parent, false);
            viewHolder = new SpokenLanguageListAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.choose_country_item_layout, parent, false);
            viewHolder = new SpokenLanguageListAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LangaugeModel langaugeModel = itemList.get(i);
        viewHolder.countryName.setText(langaugeModel.getLanguageName());
        Glide.with(context).load(langaugeModel.getPicUrl()).into(viewHolder.flag);
        viewHolder.headerTitleTextview.setText(String.valueOf(langaugeModel.getLanguageName().charAt(0)).toUpperCase());

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
        TextView countryName,headerTitleTextview;
        CircleImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
            headerTitleTextview = itemView.findViewById(R.id.headerTitleTextview);
        }
    }

    public interface onitemClick {
        public void onItemClicked(LangaugeModel langaugeModel);
    }


}
