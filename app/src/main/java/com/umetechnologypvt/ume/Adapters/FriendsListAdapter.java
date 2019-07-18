package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Activities.UserProfileScreen;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;

    public FriendsListAdapter(Context context, ArrayList<UserModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.user_item_layout,viewGroup,false);
        FriendsListAdapter.ViewHolder viewHolder=new FriendsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UserModel model=itemList.get(i);
        holder.name.setText(model.getName());
        if(model.getPicUrl()!=null){
            Glide.with(context).load(model.getPicUrl()).into(holder.pic);
        }else{
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.pic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,UserProfileScreen.class);
                i.putExtra("userId",model.getUsername());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.pic);
            name=itemView.findViewById(R.id.name);
        }
    }

}
