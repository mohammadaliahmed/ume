package com.umetechnologypvt.ume.Stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeStoriesAdapter extends RecyclerView.Adapter<HomeStoriesAdapter.ViewHolder> {
    Context context;
    public ArrayList<ArrayList<StoryModel>> itemList = new ArrayList<>();
    HomeStoriesAdapterCallbacks callbacks;

    public HomeStoriesAdapter(Context context, ArrayList<ArrayList<StoryModel>> itemList, HomeStoriesAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;

    }

    @NonNull
    @Override
    public HomeStoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_story_item_layout, parent, false);
        HomeStoriesAdapter.ViewHolder viewHolder = new HomeStoriesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeStoriesAdapter.ViewHolder holder, int position) {
        ArrayList<StoryModel> model = itemList.get(position);
        holder.storyName.setText(model.get(0).getStoryByName());
        Glide.with(context).load(model.get(0).storyByPicUrl).into(holder.storyImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    callbacks.onStoryClicked(model.get(0), position);

                }catch (Exception e) {
                    CommonUtils.showToast("Something wrong");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView storyName;
        CircleImageView storyImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyName = itemView.findViewById(R.id.storyName);
            storyImg = itemView.findViewById(R.id.storyImg);
        }
    }

    public interface HomeStoriesAdapterCallbacks {


        public void onStoryClicked(StoryModel model,int position);
    }
}
