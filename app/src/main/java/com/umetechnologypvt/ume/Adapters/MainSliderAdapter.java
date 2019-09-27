package com.umetechnologypvt.ume.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

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
    ClicksCallback callback;

    public MainSliderAdapter(Context context, List<String> picturesList, ClicksCallback callback) {
        super();
        this.context = context;
        this.picturesList = picturesList;
        this.callback = callback;
    }


    public void setPicturesList(List<String> picturesList) {
        this.picturesList = picturesList;
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.main_product_slider, container, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        Glide.with(context).load(picturesList.get(position)).into(imageView);
        container.addView(view);
        callback.onPicChanged(position);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(context));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
//                    CommonUtils.showToast("asfsdfsdf");
//                    likePost(finalLiked, viewHolders, model);
                    callback.onDoubleClick();
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
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

    public interface ClicksCallback {
        public void onDoubleClick();

        public void onPicChanged(int position);
    }

}
