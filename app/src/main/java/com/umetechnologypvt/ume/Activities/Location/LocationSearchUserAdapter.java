package com.umetechnologypvt.ume.Activities.Location;

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

import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.UserProfileScreen;
import com.umetechnologypvt.ume.Activities.ViewPictures;
import com.umetechnologypvt.ume.Models.LocationUserModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LocationSearchUserAdapter extends RecyclerView.Adapter<LocationSearchUserAdapter.ViewHolder> {
    Context context;
    ArrayList<LocationUserModel> itemList;
    LocationSearchAdapterCallbacks callbacks;
    private List<LocationUserModel> arrayList;
    UserModel myUserModel;


    public void updateList(ArrayList<LocationUserModel> itemList) {
        this.itemList = itemList;
        arrayList.clear();
        arrayList.addAll(itemList);
    }

    public void filter(int distance) {
        itemList.clear();

        for (LocationUserModel item : arrayList) {
            if (item.getDistance() <= distance) {
                itemList.add(item);
            }


        }

        notifyDataSetChanged();
    }


    public LocationSearchUserAdapter(Context context, ArrayList<LocationUserModel> itemList, UserModel myUserModel, LocationSearchAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);

        this.callbacks = callbacks;
        this.myUserModel = myUserModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_search_user_item_layout, viewGroup, false);
        LocationSearchUserAdapter.ViewHolder viewHolder = new LocationSearchUserAdapter.ViewHolder(view);
        return viewHolder;
    }

    public void setMyUserModel(UserModel myUserModel) {
        this.myUserModel = myUserModel;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        LocationUserModel model = itemList.get(i);
        holder.name.setText(model.getUserModel().getName());

        if (myUserModel != null) {
            if (myUserModel.getConfirmFriends().contains(model.getUserModel().getUsername())
                    && !myUserModel.getRequestSent().contains(model.getUserModel().getUsername()) &&
                    !myUserModel.getRequestReceived().contains(model.getUserModel().getUsername())) {
                holder.isFriend.setVisibility(View.VISIBLE);
                holder.addAsFriend.setVisibility(View.GONE);
                holder.requestSent.setVisibility(View.GONE);
            } else if (!myUserModel.getConfirmFriends().contains(model.getUserModel().getUsername())
                    && myUserModel.getRequestSent().contains(model.getUserModel().getUsername()) &&
                    !myUserModel.getRequestReceived().contains(model.getUserModel().getUsername())) {
                holder.addAsFriend.setVisibility(View.GONE);
                holder.requestSent.setVisibility(View.VISIBLE);
                holder.isFriend.setVisibility(View.GONE);

            } else {
                holder.addAsFriend.setVisibility(View.VISIBLE);
                holder.isFriend.setVisibility(View.GONE);
                holder.requestSent.setVisibility(View.GONE);
            }
        }

        holder.learningLanguage.setText("" + model.getUserModel().getLearningLanguage());
        holder.speakingLanguage.setText("" + model.getUserModel().getLanguage().replace("any", ""));

        if (model.getUserModel().getCountryNameCode() != null) {
            holder.userFlag.setVisibility(View.VISIBLE);

            Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getUserModel().getCountryNameCode())).into(holder.userFlag);
        } else {
            holder.userFlag.setVisibility(View.GONE);
        }
        if (model.getUserModel().getGender() != null) {
            if (model.getUserModel().getGender().replace("Any", "").equalsIgnoreCase("female")) {
                Glide.with(context).load(R.drawable.ic_female).into(holder.gender);
                holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_pink));
            } else {
                Glide.with(context).load(R.drawable.ic_male).into(holder.gender);
                holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_blue));

            }
        }

        holder.userStatus.setText(
                model.getUserModel().getStatus()
                        .equalsIgnoreCase("Online") ? "Online" : "Last seen " +
                        CommonUtils.getFormattedDate(Long.parseLong(model.getUserModel().getStatus()))
        );
        if (model.getUserModel().getStatus().equalsIgnoreCase("Online")) {
            holder.onlineDot.setVisibility(View.VISIBLE);
        } else {
            holder.onlineDot.setVisibility(View.GONE);
        }

        if (model.getUserModel().getAge() != 0) {
            holder.age.setText("" + model.getUserModel().getAge());
        }

        if (model.getUserModel().getPicUrl() != null) {
            Glide.with(context).load(model.getUserModel().getPicUrl()).into(holder.pic);

        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.pic);
        }
        holder.userDistance.setText("5km"
        );


        holder.itemView.setOnClickListener(v -> {
            Intent i1 = new Intent(context, MainActivity.class);
            Constants.USER_ID = model.getUserModel().getUsername();
            i1.putExtra("value", 2);
            context.startActivity(i1);

        });
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewPictures.class);
                i.putExtra("url", model.getUserModel().getPicUrl());
                context.startActivity(i);
            }
        });

        holder.userDistance.setText(String.format("%.1f", model.getDistance()) + " km");

        holder.addAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.addAsFriend(model.getUserModel());
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, userStatus, speakingLanguage, learningLanguage, age, userDistance;
        ImageView gender, onlineDot;
        RelativeLayout genderBg;
        CircleImageView pic, userFlag;

        TextView addAsFriend, requestSent, isFriend;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDistance = itemView.findViewById(R.id.userDistance);
            userStatus = itemView.findViewById(R.id.userStatus);
            onlineDot = itemView.findViewById(R.id.onlineDot);
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


            requestSent = itemView.findViewById(R.id.requestSent);
            addAsFriend = itemView.findViewById(R.id.addAsFriend);
            isFriend = itemView.findViewById(R.id.isFriend);

        }
    }

    public interface LocationSearchAdapterCallbacks {
        public void addAsFriend(UserModel model);

    }
}
