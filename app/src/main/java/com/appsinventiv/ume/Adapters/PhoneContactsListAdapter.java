package com.appsinventiv.ume.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Models.PhoneContactModel;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhoneContactsListAdapter extends RecyclerView.Adapter<PhoneContactsListAdapter.ViewHolder> {
    Context context;
    ArrayList<PhoneContactModel> itemList;
    PhoneContactsListAdapterCallbacks callbacks;

    public PhoneContactsListAdapter(Context context, ArrayList<PhoneContactModel> itemList,PhoneContactsListAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.phone_contact_item_layout,viewGroup,false);
        PhoneContactsListAdapter.ViewHolder viewHolder=new PhoneContactsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        PhoneContactModel model=itemList.get(i);
        holder.name.setText(model.getName());
        holder.number.setText(model.getNumber());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onSelected(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);
        }
    }

    public interface PhoneContactsListAdapterCallbacks{
        public void onSelected(PhoneContactModel model);
    }
}
