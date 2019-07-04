package com.appsinventiv.ume.BottomDialogs.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.EditProfile;
import com.appsinventiv.ume.Activities.Profile;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChooseInterestListAdapter extends RecyclerView.Adapter<ChooseInterestListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;
    private ArrayList<String> arrayList;
    List<String> userInterest;

    public ChooseInterestListAdapter(Context context, ArrayList<String> itemList, List<String> userInterest) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);
        this.userInterest = userInterest;

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
        View view = LayoutInflater.from(context).inflate(R.layout.choose_interest_item_layout, viewGroup, false);
        ChooseInterestListAdapter.ViewHolder viewHolder = new ChooseInterestListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name = itemList.get(i);
        viewHolder.countryName.setText(name);
        boolean flag=false;
        for (int j = 0; j < userInterest.size(); j++) {
            if (name.equalsIgnoreCase(userInterest.get(j))) {
                flag=true;
            }
        }

        if(flag){
            viewHolder.checkbox.setChecked(true);
        }else{
            viewHolder.checkbox.setChecked(false);
        }
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

                        if (Profile.interestList.contains(name)) {

                        } else {
                            Profile.interestList.add(name);
                        }
                        if (EditProfile.interestList.contains(name)) {

                        } else {
                            EditProfile.interestList.add(name);
                        }
                    } else {
                        if (Profile.interestList.contains(name)) {
                            Profile.interestList.remove(EditProfile.interestList.indexOf(name));
                        } else {

                        }
                        if (EditProfile.interestList.contains(name)) {
                            EditProfile.interestList.remove(EditProfile.interestList.indexOf(name));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}
