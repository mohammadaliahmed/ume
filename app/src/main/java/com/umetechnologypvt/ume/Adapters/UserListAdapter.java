package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Interface.ContactListCallbacks;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    ContactListCallbacks callbacks;
    ArrayList<String> blockedUser;
    ArrayList<String> blockedMeUser;

    public UserListAdapter(Context context, ArrayList<UserModel> itemList, ArrayList<String> blockedUser, ArrayList<String> blockedMeUser, ContactListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
        this.blockedUser = blockedUser;
        this.blockedMeUser = blockedMeUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, viewGroup, false);
        UserListAdapter.ViewHolder viewHolder = new UserListAdapter.ViewHolder(view);
        return viewHolder;
    }

    public void setUserList(ArrayList<UserModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UserModel model = itemList.get(i);


        boolean flag = false;
        if (blockedUser.size() > 0) {
            if (blockedUser.contains(model.getUsername())) {
                flag = true;
            }

        }


        if (flag) {
            holder.clickToUnblock.setVisibility(View.VISIBLE);
        } else {
            holder.clickToUnblock.setVisibility(View.GONE);
        }
        boolean flag2 = false;
        if (blockedMeUser.size() > 0) {
            if (blockedMeUser.contains(model.getUsername())) {
                flag2 = true;
            }

        }


        if (flag2) {
            holder.youAreBlocked.setVisibility(View.VISIBLE);
        } else {
            holder.youAreBlocked.setVisibility(View.GONE);
        }

        holder.name.setText(model.getName());
        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.pic);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.pic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blockedMeUser.contains(model.getUsername())) {
                    CommonUtils.showToast("Cannot send message");
                } else {
                    Intent i = new Intent(context, SingleChattingScreen.class);
                    i.putExtra("userId", model.getUsername());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(blockedUser.contains(model.getUsername())){
                    callbacks.onUnBlock(model);
                }else if(blockedMeUser.contains(model.getUsername())){

                }else{
                    callbacks.onBlock(model);
                }

                return false;
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
        RelativeLayout clickToUnblock, youAreBlocked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            clickToUnblock = itemView.findViewById(R.id.clickToUnblock);
            youAreBlocked = itemView.findViewById(R.id.youAreBlocked);
        }
    }
}
