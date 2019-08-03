package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by AliAh on 21/02/2018.
 */

public class MainSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> picturesList;

    public MainSliderAdapter( Context context, List<String> picturesList) {
        super();
        this.context = context;
        this.picturesList = picturesList;
    }

    public void setPicturesList(List<String> picturesList) {
        this.picturesList = picturesList;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.main_product_slider, container, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        Glide.with(context).load(picturesList.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {

        if (picturesList == null) {
            return 0;
        } else if (picturesList.size() > 0) {
            return picturesList.size();
        } else {
            return picturesList.size();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);

    }

}
