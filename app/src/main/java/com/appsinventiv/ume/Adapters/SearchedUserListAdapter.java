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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.SingleChattingScreen;
import com.appsinventiv.ume.Activities.UserProfileScreen;
import com.appsinventiv.ume.Activities.ViewPictures;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CountryUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchedUserListAdapter extends RecyclerView.Adapter<SearchedUserListAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    SearchUserCallbacks callbacks;

    public SearchedUserListAdapter(Context context, ArrayList<UserModel> itemList, SearchUserCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_item_layout, viewGroup, false);
        SearchedUserListAdapter.ViewHolder viewHolder = new SearchedUserListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UserModel model = itemList.get(i);
        holder.name.setText(model.getName());
        if (model.getLearningLanguage().contains("any")) {
            model.getLearningLanguage().remove(model.getLearningLanguage().indexOf("any"));
        }
        holder.learningLanguage.setText("" + model.getLearningLanguage());
        holder.speakingLanguage.setText("" + model.getLanguage().replace("any", ""));

        if (model.getCountryNameCode() != null) {
            holder.userFlag.setVisibility(View.VISIBLE);

            Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getCountryNameCode())).into(holder.userFlag);
        } else {
            holder.userFlag.setVisibility(View.GONE);
        }

        if (model.getGender().replace("Any", "").equalsIgnoreCase("female")) {
            Glide.with(context).load(R.drawable.ic_female).into(holder.gender);
            holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_pink));
        } else {
            Glide.with(context).load(R.drawable.ic_male).into(holder.gender);
            holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_blue));

        }

        if (model.getAge() != 0) {
            holder.age.setText("" + model.getAge());
        }

        if (model.getPicUrl() != null) {
            Glide.with(context).load(model.getPicUrl()).into(holder.pic);
//            if (model.getGender().equalsIgnoreCase("male")) {
//                holder.pic.setBorderColor(context.getResources().getColor(R.color.colorBlue));
//            } else if (model.getGender().equalsIgnoreCase("female")) {
//                holder.pic.setBorderColor(context.getResources().getColor(R.color.colorPink));
//            }
        }
        holder.userStatus.setText(
                model.getStatus()
                        .equalsIgnoreCase("Online") ? "Online" : "Last seen " + CommonUtils.getFormattedDate(Long.parseLong(model.getStatus()))
        );
        if(model.getStatus().equalsIgnoreCase("Online")){
            holder.onlineDot.setVisibility(View.VISIBLE);
        }else{
            holder.onlineDot.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(v -> {
            Intent i1 = new Intent(context, UserProfileScreen.class);
            i1.putExtra("userId", model.getUsername());
            context.startActivity(i1);
        });
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewPictures.class);
                i.putExtra("url", model.getPicUrl());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, userStatus, speakingLanguage, learningLanguage, age;
        ImageView pic, userFlag, gender,onlineDot;
        RelativeLayout genderBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            learningLanguage = itemView.findViewById(R.id.learningLanguage);
            speakingLanguage = itemView.findViewById(R.id.speakingLanguage);
            userStatus = itemView.findViewById(R.id.userStatus);
            userFlag = itemView.findViewById(R.id.userFlag);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.gender);
            genderBg = itemView.findViewById(R.id.genderBg);
            onlineDot = itemView.findViewById(R.id.onlineDot);
        }
    }

    public interface SearchUserCallbacks {
        public void addAsFriend(UserModel model);

        public void removeFriend(UserModel model);
    }
}
