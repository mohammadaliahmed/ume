package com.umetechnologypvt.ume.Stories.StoriesEditing;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.StoriesPickedModel;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultiStoriesPickedAdapter extends RecyclerView.Adapter<MultiStoriesPickedAdapter.ViewHolder> {
    Context context;
    List<StoriesPickedModel> itemList;
    AdapterCallbacks callbacks;
    int position = 0;

    public MultiStoriesPickedAdapter(Context context, List<StoriesPickedModel> itemList, AdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }


    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_img_item_layout, viewGroup, false);
        MultiStoriesPickedAdapter.ViewHolder viewHolder = new MultiStoriesPickedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        StoriesPickedModel model = itemList.get(i);
        if (position == i) {
            holder.selected.setVisibility(View.VISIBLE);
        } else {
            holder.selected.setVisibility(View.GONE);
        }
        if (model.getType().equalsIgnoreCase("image")) {
            Glide.with(context).load(model.getUri()).into(holder.image);

        } else {
            try {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(CommonUtils.getRealPathFromURI(Uri.parse(model.getUri())), 120);
                holder.image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onSelected(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.selected);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface AdapterCallbacks {
        public void onSelected(int position);
    }

}
