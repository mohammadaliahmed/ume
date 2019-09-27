package com.umetechnologypvt.ume.Activities.Comments;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {
    Context context;
    ArrayList<CommentsModel> itemList;
    CommentsCallback callback;

    public CommentsListAdapter(Context context, ArrayList<CommentsModel> itemList,CommentsCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
        CommentsListAdapter.ViewHolder viewHolder = new CommentsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        CommentsModel model = itemList.get(i);
        String sourceString = "<b>" + model.getCommentByName() + "</b> " + model.getCommentText();
        holder.comment.setText(Html.fromHtml(sourceString));
        if (model.getCommentByPicUrl() != null) {
            Glide.with(context).load(model.getCommentByPicUrl()).into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.image);
        }
        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));
        if (model.getCountryCode() != null) {
            holder.flag.setVisibility(View.VISIBLE);
            Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getCountryCode())).into(holder.flag);
        } else {
            holder.flag.setVisibility(View.GONE);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getCommentBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())){

                    callback.takeUserWhere(1);
                }else{
                    Constants.USER_ID=model.getCommentBy();

                    callback.takeUserWhere(2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment, time;
        CircleImageView image;
        CircleImageView flag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
            flag = itemView.findViewById(R.id.flag);
        }
    }
    public  interface CommentsCallback{
        public void takeUserWhere(int value);
    }

}
