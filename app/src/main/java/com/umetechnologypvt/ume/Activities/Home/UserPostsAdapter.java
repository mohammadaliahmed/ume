package com.umetechnologypvt.ume.Activities.Home;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {
    Context context;
    ArrayList<PostsModel> itemList;

    HomePostsAdapterCallBacks callBacks;

    public void setCallBacks(HomePostsAdapterCallBacks callBacks) {
        this.callBacks = callBacks;
    }

    public UserPostsAdapter(Context context, ArrayList<PostsModel> itemList) {
        this.context = context;
        this.itemList = itemList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_post_item_layout, parent, false);
        UserPostsAdapter.ViewHolder viewHolder = new UserPostsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostsModel model = itemList.get(position);
        if (model.getType().equalsIgnoreCase("image")) {
            holder.videoIcon.setVisibility(View.GONE);
            holder.multiIcon.setVisibility(View.GONE);

            Glide.with(context).load(model.getPictureUrl()).into(holder.image);
        } else if (model.getType().equalsIgnoreCase("video")) {
            holder.videoIcon.setVisibility(View.VISIBLE);
            holder.multiIcon.setVisibility(View.GONE);


            Glide.with(context).load(model.getVideoThumbnailUrl()).into(holder.image);
        } else if (model.getType().equalsIgnoreCase("multi")) {
            holder.videoIcon.setVisibility(View.GONE);
            holder.multiIcon.setVisibility(View.VISIBLE);

            Glide.with(context).load(model.getPictureUrl()).into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBacks != null) {
                    callBacks.onPictureSelected(position);
                }
            }
        });


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

        ImageView image, multiIcon, videoIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            videoIcon = itemView.findViewById(R.id.videoIcon);
            multiIcon = itemView.findViewById(R.id.multiIcon);

        }
    }

    public interface HomePostsAdapterCallBacks {
        public void onPictureSelected(int position);
    }
}
