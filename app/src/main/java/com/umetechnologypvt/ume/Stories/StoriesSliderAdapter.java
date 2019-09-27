package com.umetechnologypvt.ume.Stories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.umetechnologypvt.ume.Activities.Home.MainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by AliAh on 21/02/2018.
 */

public class StoriesSliderAdapter extends FragmentPagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int size;

    FragmentManager fm;

    @SuppressLint("WrongConstant")
    public StoriesSliderAdapter(FragmentManager fm, Context context) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new StoryFragment(context, position, MainActivity.arrayLists.get(position).size());
    }


    @Override
    public int getCount() {

        return MainActivity.arrayLists.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove((Fragment) object);
        ft.commit();
    }
}
