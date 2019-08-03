package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.umetechnologypvt.ume.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultiImagesPickedAdapter extends RecyclerView.Adapter<MultiImagesPickedAdapter.ViewHolder> {
    Context context;
    List<String> itemList;
    AdapterCallbacks callbacks;

    public MultiImagesPickedAdapter(Context context, List<String> itemList,AdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.img_item_layout,viewGroup,false);
        MultiImagesPickedAdapter.ViewHolder viewHolder=new MultiImagesPickedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String model=itemList.get(i);

        Glide.with(context).load(model).into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete);
            image=itemView.findViewById(R.id.image);
        }
    }

    public interface AdapterCallbacks{
        public void onDelete(String id,int position);
    }

}
