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

import com.umetechnologypvt.ume.Activities.UserProfileScreen;
import com.umetechnologypvt.ume.Activities.ViewPictures;
import com.umetechnologypvt.ume.Models.LocationUserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
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


    public LocationSearchUserAdapter(Context context, ArrayList<LocationUserModel> itemList, LocationSearchAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.arrayList = new ArrayList<>(itemList);

        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_search_user_item_layout, viewGroup, false);
        LocationSearchUserAdapter.ViewHolder viewHolder = new LocationSearchUserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        LocationUserModel model = itemList.get(i);
        holder.name.setText(model.getUserModel().getName());

        if (model.getUserModel().getCountryNameCode() != null) {
            holder.userFlag.setVisibility(View.VISIBLE);

            Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getUserModel().getCountryNameCode())).into(holder.userFlag);
        } else {
            holder.userFlag.setVisibility(View.GONE);
        }

        if (model.getUserModel().getGender().replace("Any", "").equalsIgnoreCase("female")) {
            Glide.with(context).load(R.drawable.ic_female).into(holder.gender);
            holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_pink));
        } else {
            Glide.with(context).load(R.drawable.ic_male).into(holder.gender);
            holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_blue));

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
            Intent i1 = new Intent(context, UserProfileScreen.class);
            i1.putExtra("userId", model.getUserModel().getUsername());
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

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, userDistance, userStatus, learningLanguage, age;
        ImageView gender, onlineDot;
        RelativeLayout genderBg;
        CircleImageView pic, userFlag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            userDistance = itemView.findViewById(R.id.userDistance);
            userFlag = itemView.findViewById(R.id.userFlag);
            age = itemView.findViewById(R.id.age);
            userStatus = itemView.findViewById(R.id.userStatus);

            gender = itemView.findViewById(R.id.gender);
            genderBg = itemView.findViewById(R.id.genderBg);
            onlineDot = itemView.findViewById(R.id.onlineDot);

        }
    }

    public interface LocationSearchAdapterCallbacks {

    }
}
