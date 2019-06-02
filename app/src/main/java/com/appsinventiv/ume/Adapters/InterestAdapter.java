package com.appsinventiv.ume.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.EditProfile;
import com.appsinventiv.ume.Activities.Search.Filters;
import com.appsinventiv.ume.Models.Country;
import com.appsinventiv.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {
    Context context;
    List<String> itemList;

    public InterestAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.interest_item_layout, viewGroup, false);
        InterestAdapter.ViewHolder viewHolder = new InterestAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String interest = itemList.get(i);
        viewHolder.text.setText(interest);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

}
