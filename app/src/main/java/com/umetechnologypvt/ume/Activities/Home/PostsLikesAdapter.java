package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostsLikesAdapter extends RecyclerView.Adapter<PostsLikesAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> itemList;
    LikesAdapterCallBacks callBacks;
    UserModel myUserModel;


    public PostsLikesAdapter(Context context, ArrayList<UserModel> itemList, LikesAdapterCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;

    }

    public void setMyUserModel(UserModel myUserModel) {
        this.myUserModel = myUserModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_likes_item_layout, parent, false);
        PostsLikesAdapter.ViewHolder viewHolder = new PostsLikesAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel model = itemList.get(position);
        int value = 0;

        if (model.getUsername().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
            holder.addAsFriend.setVisibility(View.GONE);
            holder.isFriend.setVisibility(View.GONE);
            holder.requestSent.setVisibility(View.GONE);
        } else {
            if (myUserModel != null) {
                if (myUserModel.getConfirmFriends().contains(model.getUsername())
                        && !myUserModel.getRequestSent().contains(model.getUsername()) &&
                        !myUserModel.getRequestReceived().contains(model.getUsername())) {
                    holder.isFriend.setVisibility(View.VISIBLE);
                    holder.addAsFriend.setVisibility(View.GONE);
                    holder.requestSent.setVisibility(View.GONE);
                } else if (!myUserModel.getConfirmFriends().contains(model.getUsername())
                        && myUserModel.getRequestSent().contains(model.getUsername()) &&
                        !myUserModel.getRequestReceived().contains(model.getUsername())) {
                    holder.addAsFriend.setVisibility(View.GONE);
                    holder.requestSent.setVisibility(View.VISIBLE);
                    holder.isFriend.setVisibility(View.GONE);

                } else {
                    holder.addAsFriend.setVisibility(View.VISIBLE);
                    holder.isFriend.setVisibility(View.GONE);
                    holder.requestSent.setVisibility(View.GONE);
                }
            }
        }

        holder.addAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.addAsFriend(model);
            }
        });


//        if (SharedPrefs.getUserModel().getConfirmFriends() != null && SharedPrefs.getUserModel().getConfirmFriends().size() > 0) {
//            if (SharedPrefs.getUserModel().getConfirmFriends().contains(model.getUsername())) {
//                value = 1;
//            }
//        } else if (SharedPrefs.getUserModel().getRequestSent() != null && SharedPrefs.getUserModel().getRequestSent().size() > 0) {
//            if (SharedPrefs.getUserModel().getRequestSent().contains(model.getUsername())) {
//                value = 2;
//            }
//        } else if (SharedPrefs.getUserModel().getRequestReceived() != null && SharedPrefs.getUserModel().getRequestReceived().size() > 0) {
//            if (SharedPrefs.getUserModel().getRequestReceived().contains(model.getUsername())) {
//                value = 3;
//            }
//        }
//
//        if (value == 1) {
//            holder.addAsFriend.setText("Friend");
//        } else if (value == 2) {
//            holder.addAsFriend.setText("Request sent");
//        } else if (value == 3) {
//            holder.addAsFriend.setText("Accept Request");
//        } else {
//            holder.addAsFriend.setText("Add As Friend");
//
//        }
//
//        if (model.getUsername().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
//            holder.addAsFriend.setVisibility(View.GONE);
//
//        } else {
//            holder.addAsFriend.setVisibility(View.VISIBLE);
//        }
//        int finalValue = value;
//        holder.addAsFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (finalValue == 1) {
//
//                } else if (finalValue == 2) {
//
//                } else if (finalValue == 3) {
//                    callBacks.acceptRequest(model);
//                } else {
//                    callBacks.addAsFriend(model);
//                    holder.addAsFriend.setText("Request Sent");
//                }
//            }
//        });


        if (model.getGender() != null) {
            holder.genderBg.setVisibility(View.VISIBLE);
            if (model.getGender().equalsIgnoreCase("female")) {
                Glide.with(context).load(R.drawable.ic_female).into(holder.gender);
                holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_pink));
            } else {
                Glide.with(context).load(R.drawable.ic_male).into(holder.gender);
                holder.genderBg.setBackground(context.getResources().getDrawable(R.drawable.custom_corners_blue));

            }

            if (model.getAge() != 0) {
                holder.age.setText("" + model.getAge());
            } else {

            }
        } else {
            holder.genderBg.setVisibility(View.GONE);
        }

        Glide.with(context).load(model.getThumbnailUrl()).into(holder.image);
        holder.name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToProfile(model.getUsername());
            }
        });

    }


    public void takeUserToProfile(String userId) {
        if (userId.equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
            callBacks.takeUserToMyUserProfile(userId);
        } else {
            callBacks.takeUserToOtherUserProfile(userId);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age;
        CircleImageView image;
        TextView addAsFriend, requestSent, isFriend;
        RelativeLayout genderBg;
        ImageView gender;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            addAsFriend = itemView.findViewById(R.id.addAsFriend);
            genderBg = itemView.findViewById(R.id.genderBg);
            gender = itemView.findViewById(R.id.gender);
            age = itemView.findViewById(R.id.age);


            requestSent = itemView.findViewById(R.id.requestSent);
            addAsFriend = itemView.findViewById(R.id.addAsFriend);
            isFriend = itemView.findViewById(R.id.isFriend);
        }
    }

    public interface LikesAdapterCallBacks {
        public void takeUserToMyUserProfile(String userId);

        public void takeUserToOtherUserProfile(String userId);

        public void addAsFriend(UserModel user);

        public void removeAsFriend(UserModel user);

        public void acceptRequest(UserModel user);


    }
}
