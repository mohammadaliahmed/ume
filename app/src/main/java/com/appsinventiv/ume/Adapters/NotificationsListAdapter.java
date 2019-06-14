package com.appsinventiv.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.UserProfileScreen;
import com.appsinventiv.ume.Models.NotificationModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {
    private Callbacks callbacks;
    Context context;
    ArrayList<NotificationModel> itemList;

    public NotificationsListAdapter(Context context, ArrayList<NotificationModel> itemList, Callbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, viewGroup, false);
        NotificationsListAdapter.ViewHolder viewHolder = new NotificationsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = itemList.get(position);
        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.image);
        }
        holder.title.setText(model.getTitle());
        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getType().equalsIgnoreCase("newRequest") || model.getType().equalsIgnoreCase("requestAccept")) {
                    Intent i = new Intent(context, UserProfileScreen.class);
                    i.putExtra("userId", model.getHisusername());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cancel;
        CircleImageView image;
        TextView time, title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cancel = itemView.findViewById(R.id.cancel);
            image = itemView.findViewById(R.id.image);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
        }
    }

    public interface Callbacks {
        public void onDelete(String id);
    }
}
