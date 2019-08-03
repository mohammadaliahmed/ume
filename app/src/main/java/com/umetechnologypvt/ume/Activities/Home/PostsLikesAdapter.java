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


    public PostsLikesAdapter(Context context, ArrayList<UserModel> itemList, LikesAdapterCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;

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

//        boolean liked = false;
//        if (likeList.size() > 0) {
//            if (likeList.contains(model.getId())) {
//                liked = true;
//            }
//        }
//        if (liked) {
//            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_fill));
//        } else {
//            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_empty));
//
//        }

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
        TextView name;
        CircleImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

        }
    }

    public interface LikesAdapterCallBacks {
        public void takeUserToMyUserProfile(String userId);

        public void takeUserToOtherUserProfile(String userId);


    }
}
