package com.umetechnologypvt.ume.Stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoryViewsAdapter extends RecyclerView.Adapter<StoryViewsAdapter.ViewHolder> {
    Context context;
    ArrayList<StoryViewsModel> itemList;

    public StoryViewsAdapter(Context context, ArrayList<StoryViewsModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    public void updateList( ArrayList<StoryViewsModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_view_item_layout, viewGroup, false);
        StoryViewsAdapter.ViewHolder viewHolder = new StoryViewsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        StoryViewsModel model = itemList.get(i);
        holder.name.setText(model.getSeenByName());
        if (model.getSeenByPicUrl() != null) {
            Glide.with(context).load(model.getSeenByPicUrl()).into(holder.pic);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.pic);
        }

        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView pic;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
        }
    }


}
