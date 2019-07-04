package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Models.Country;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChooseCountryAdapter extends RecyclerView.Adapter<ChooseCountryAdapter.ViewHolder> {
    Context context;
    List<Country> itemList;
    private ArrayList<Country> arrayList;
    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;

    onitemClick click;

    public ChooseCountryAdapter(Context context, List<Country> itemList, onitemClick click) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);
        this.click = click;

    }

    public void updateList(List<Country> itemList) {
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
            for (Country item : arrayList) {
                if (item.getCountryName().toLowerCase().contains(charText)) {

                    itemList.add(item);
                }

            }


        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.choose_country_item_layout, viewGroup, false);
//        ChooseCountryAdapter.ViewHolder viewHolder = new ChooseCountryAdapter.ViewHolder(view);
//        return viewHolder;
        ChooseCountryAdapter.ViewHolder viewHolder;

        if (viewType == SECTION_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header_title, parent, false);
            viewHolder = new ChooseCountryAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.choose_country_item_layout, parent, false);
            viewHolder = new ChooseCountryAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Country country = itemList.get(i);
        viewHolder.countryName.setText(country.getCountryName());
        viewHolder.headerTitleTextview.setText(String.valueOf(country.getCountryName().charAt(0)).toUpperCase());
        Glide.with(context).load(CountryUtils.getFlagDrawableResId(country.getCountryCode())).into(viewHolder.flag);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.country = country.getCountryName();
                EditProfile.country = country.getCountryName();
                EditProfile.countryCode = country.getCountryCode();
                Profile.countryCode = country.getCountryCode();
                EditProfile.currentLocation = country.getCountryName();
                Profile.currentLocation = country.getCountryName();
                Filters.country = country.getCountryName();
                Filters.currentLocation = country.getCountryName();
                click.onItemClicked(country);

            }
        });


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

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, headerTitleTextview;
        ImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
            headerTitleTextview = itemView.findViewById(R.id.headerTitleTextview);
        }
    }


    public interface onitemClick {
        public void onItemClicked(Country country);
    }

}
