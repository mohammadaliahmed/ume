package com.appsinventiv.ume.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChooseLangaugeFromDialogAdapter extends RecyclerView.Adapter<ChooseLangaugeFromDialogAdapter.ViewHolder> {
    Context context;
    List<LangaugeModel> itemList;
    Callbacks callbacks;
    public ChooseLangaugeFromDialogAdapter(Context context, List<LangaugeModel> itemList,Callbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks=callbacks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.langu, viewGroup, false);
        ChooseLangaugeFromDialogAdapter.ViewHolder viewHolder = new ChooseLangaugeFromDialogAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LangaugeModel langaugeModel = itemList.get(i);
        viewHolder.countryName.setText(langaugeModel.getLanguageName());
        Glide.with(context).load(langaugeModel.getPicDrawable()
        ).into(viewHolder.flag);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.callback(langaugeModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        ImageView flag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }
    public interface Callbacks{
        public void callback(LangaugeModel model );
    }

}
