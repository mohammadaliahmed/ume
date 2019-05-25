package com.appsinventiv.ume.BottomDialogs.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;

import java.util.ArrayList;

public class LearningLanguageListAdapter extends RecyclerView.Adapter<LearningLanguageListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;

    public LearningLanguageListAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.choose_language_item_layout,viewGroup,false);
        LearningLanguageListAdapter.ViewHolder viewHolder=new LearningLanguageListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name=itemList.get(i);
        viewHolder.countryName.setText(name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(name);
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
            countryName=itemView.findViewById(R.id.countryName);
        }
    }
}
