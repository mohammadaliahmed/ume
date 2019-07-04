package com.umetechnologypvt.ume.BottomDialogs.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.R;

import java.util.ArrayList;
import java.util.Locale;

public class ChooseSingleInterestListAdapter extends RecyclerView.Adapter<ChooseSingleInterestListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;
    private ArrayList<String> arrayList;
    onitemClick click;


    public ChooseSingleInterestListAdapter(Context context, ArrayList<String> itemList,onitemClick click) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);
        this.click=click;

    }

    public void updateList(ArrayList<String> itemList) {
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
            for (String item : arrayList) {
                if (item.toLowerCase().contains(charText)) {

                    itemList.add(item);
                }

            }


        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_single_interest_item_layout, viewGroup, false);
        ChooseSingleInterestListAdapter.ViewHolder viewHolder = new ChooseSingleInterestListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name = itemList.get(i);
        viewHolder.countryName.setText(name);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filters.interest=name;
                click.onItemClicked(name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }
    public  interface onitemClick{
        public void onItemClicked(String name);
    }

}
