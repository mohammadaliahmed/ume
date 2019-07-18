package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatListModel> itemList;
    HashMap<String, Integer> unreadCount = new HashMap<>();
    HashMap<Integer, ChatListModel> map;

    ChatCallbacks callbacks;

//    public ChatListAdapter(Context context, HashMap<Integer, ChatListModel> map) {
//        this.context = context;
//        this.map = map;
//    }


        public ChatListAdapter(Context context, ArrayList<ChatListModel> itemList,ChatCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;

    }

    public void setUnreadCount(HashMap<String, Integer> unreadCount) {
        this.unreadCount = unreadCount;
        notifyDataSetChanged();
    }

    public void setNewList(ArrayList<ChatListModel> itemList){
            this.itemList=itemList;
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatListModel model = itemList.get(position);


        if (unreadCount != null && unreadCount.size() > 0) {
            if (unreadCount.get(model.getMessage().getMessageBy()) != null) {
                if (unreadCount.get(model.getMessage().getMessageBy()) == 0) {
                    holder.count.setVisibility(View.GONE);
                } else {
                    holder.count.setVisibility(View.VISIBLE);
                    holder.count.setText("" + unreadCount.get(model.getMessage().getMessageBy()));

                }

            } else {
                holder.count.setVisibility(View.GONE);
            }
        } else {
            holder.count.setVisibility(View.GONE);
        }

        holder.username.setText(model.getMessage().getName());
        if (model.getMessage().getPicUrl() == null) {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.image);
        } else {
            Glide.with(context).load(model.getMessage().getPicUrl()).into(holder.image);

        }
        if (model.getMessage().getMessageType() != null) {
            if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE)) {
                holder.message.setText("" + "\uD83D\uDCF7  Image");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_STICKER)) {
                holder.message.setText("" + "\uD83D\uDD37 Sticker");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_VIDEO)) {
                holder.message.setText("" + "\uD83D\uDCFD  Video");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_AUDIO)) {
                holder.message.setText("" + "\uD83C\uDFB5 Audio");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_DOCUMENT)) {
                holder.message.setText("" + "\uD83D\uDCC4 Document");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_TEXT)) {
                holder.message.setText(model.getMessage().getMessageText());
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_DELETED)) {
                holder.message.setText("Message deleted");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_TRANSLATED)) {
                holder.message.setText("\uD83C\uDE02 Translation");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_LOCATION)) {
                holder.message.setText("\uD83D\uDCCD Location");
            } else if (model.getMessage().getMessageType().equals(Constants.MESSAGE_TYPE_CONTACT)) {
                holder.message.setText("☎ Contact");
            }


        }

        holder.time.setText(CommonUtils.getFormattedDate(model.getMessage().getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SingleChattingScreen.class);
                i.putExtra("userId", model.getMessage().getUsername());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        if (model.getMessage().getCountryCode() != null) {
            holder.flag.setVisibility(View.VISIBLE);
            Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getMessage().getCountryCode())).into(holder.flag);
        } else {
            holder.flag.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callbacks.onChatDelete(model.getUsername());
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, time, count;
        ImageView image;
        CircleImageView flag;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            count = itemView.findViewById(R.id.count);
            image = itemView.findViewById(R.id.image);
            flag = itemView.findViewById(R.id.flag);

        }
    }
    public interface ChatCallbacks{
            public void onChatDelete(String username);
    }
}
