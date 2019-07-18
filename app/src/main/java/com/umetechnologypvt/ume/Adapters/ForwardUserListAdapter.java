package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ForwardUserListAdapter extends RecyclerView.Adapter<ForwardUserListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    ForwardUserListAdapterCallback callback;

    public ForwardUserListAdapter(Context context, ArrayList<UserModel> itemList,ForwardUserListAdapterCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback=callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.user_item_layout,viewGroup,false);
        ForwardUserListAdapter.ViewHolder viewHolder=new ForwardUserListAdapter.ViewHolder(view);
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
                callback.contactSelected(model.getUsername());
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
    public interface  ForwardUserListAdapterCallback{
        public void contactSelected(String username);
    }
}
