package com.appsinventiv.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.SingleChattingScreen;
import com.appsinventiv.ume.Activities.UserProfileScreen;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchedUserListAdapter extends RecyclerView.Adapter<SearchedUserListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    SearchUserCallbacks callbacks;

    public SearchedUserListAdapter(Context context, ArrayList<UserModel> itemList,SearchUserCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.search_user_item_layout,viewGroup,false);
        SearchedUserListAdapter.ViewHolder viewHolder=new SearchedUserListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UserModel model=itemList.get(i);
        holder.name.setText(model.getName());
        if(model.getPicUrl()!=null){
            Glide.with(context).load(model.getPicUrl()).into(holder.pic);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i1 =new Intent(context,UserProfileScreen.class);
            i1.putExtra("userId",model.getUsername());
            context.startActivity(i1);
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
    public interface SearchUserCallbacks{
        public void addAsFriend(UserModel model);
        public void removeFriend(UserModel model);
    }
}
