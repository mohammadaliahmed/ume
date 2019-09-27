package com.umetechnologypvt.ume.Stories;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CubeTransformer;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

public class StoryActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    CustomViewPager viewpager;
    StoriesSliderAdapter mViewPagerAdapter;
    public static Activity activity;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


        LocalBroadcastManager.getInstance(StoryActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter("storyPosition"));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.stories_fragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        viewpager = findViewById(R.id.viewpager);
        ArrayList<String> pics = new ArrayList<>();

        mViewPagerAdapter = new StoriesSliderAdapter(getSupportFragmentManager(), this);
        viewpager.setAdapter(mViewPagerAdapter);
//        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(Constants.STORY_POSITION);
        viewpager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
//                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mViewPagerAdapter.notifyDataSetChanged();


    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String position = intent.getStringExtra("storyPosition");
//            CommonUtils.showToast("" + position);
            if (pos < MainActivity.arrayLists.size()) {
                viewpager.setCurrentItem(pos + 1);
            }else{
                finish();
            }

        }
    };
}
