package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.Constants;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShareMessageFriendsAdapter extends RecyclerView.Adapter<ShareMessageFriendsAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    ShareMessageFriendsAdapterCallbacks callbacks;
    ArrayList<UserModel> arrayList;
    int selected = -1;

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arrayList);
        } else {
            for (UserModel item : arrayList) {
                if (item.getName().toLowerCase().contains(charText)) {

                    itemList.add(item);
                }

            }


        }

        notifyDataSetChanged();
    }

    public void updateList(ArrayList<UserModel> itemList) {
        this.itemList = itemList;
        arrayList.clear();
        arrayList.addAll(itemList);
    }


    public ShareMessageFriendsAdapter(Context context, ArrayList<UserModel> itemList, ShareMessageFriendsAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
        this.arrayList = new ArrayList<>(itemList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_share_item_layout, viewGroup, false);
        ShareMessageFriendsAdapter.ViewHolder viewHolder = new ShareMessageFriendsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UserModel model = itemList.get(i);

        if (selected == i) {
            holder.send.setBackground(context.getResources().getDrawable(R.drawable.edit_text_background));
            holder.send.setText("Sent");
            holder.send.setTextColor(context.getResources().getColor(R.color.greyColor));
        } else {
            holder.send.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_purple));
            holder.send.setText("Send");
            holder.send.setTextColor(context.getResources().getColor(R.color.white));

        }

        holder.name.setText(model.getName());
        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.pic);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.pic);
        }

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(i);
                callbacks.onSend(model);
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
        Button send;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            send = itemView.findViewById(R.id.send);
        }
    }

    public interface ShareMessageFriendsAdapterCallbacks {
        public void onSend(UserModel model);
    }

}
