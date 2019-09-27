package com.umetechnologypvt.ume.Stories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class StoriesFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    CustomViewPager viewpager;
    StoriesSliderAdapter mViewPagerAdapter;
//    public static ArrayList<ArrayList<StoriesData>> arrayLists = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        ArrayList<StoriesData> mStoriesList = new ArrayList<>();
//
//        mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Videos%2F3fedc157f727fb7c?alt=media&token=a4228c71-18f8-4b3c-8456-77ea26813ef9", "video/mp4"));
//        mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Photos%2F3fe83b335638ec87?alt=media&token=85f05030-9fe5-4b7f-9521-74e7dbfae111", "image/png"));
//        mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Videos%2F3fed33910c5eea35?alt=media&token=8f7b97b7-64ea-4365-963b-7bfe0582413e", "video/mp4"));
//        mStoriesList.add(new StoriesData("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "video/mp4"));
//        mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Photos%2F3fed7d5743c26aa4?alt=media&token=7cba67ff-f0f7-4b66-a614-47918bcf9e2f", "image/png"));
//        mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Videos%2F3fe660983245c618?alt=media&token=ede22cb2-3858-44e0-bd08-65d39c0286d6", "video/mp4"));
//
//        arrayLists.add(mStoriesList);
//        ArrayList<StoriesData> mStoriesList2 = new ArrayList<>();
//
//        mStoriesList2.add(new StoriesData("http://images.unsplash.com/photo-1518568814500-bf0f8d125f46?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", "image/png"));
//        mStoriesList2.add(new StoriesData("https://image.shutterstock.com/image-photo/beautiful-water-drop-on-dandelion-260nw-789676552.jpg", "image/png"));
//        mStoriesList2.add(new StoriesData("https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/tcl-4_reno-50-iotd.jpg", "image/png"));
//        mStoriesList2.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Videos%2F3fe660983245c618?alt=media&token=ede22cb2-3858-44e0-bd08-65d39c0286d6", "video/mp4"));
//
//        arrayLists.add(mStoriesList2);


//        }else if(position==1){
//            mStoriesList.add(new StoriesData("http://images.unsplash.com/photo-1518568814500-bf0f8d125f46?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEyMDd9", "image/png"));
//            mStoriesList.add(new StoriesData("https://image.shutterstock.com/image-photo/beautiful-water-drop-on-dandelion-260nw-789676552.jpg", "image/png"));
//            mStoriesList.add(new StoriesData("https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/tcl-4_reno-50-iotd.jpg", "image/png"));
//            mStoriesList.add(new StoriesData("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Videos%2F3fe660983245c618?alt=media&token=ede22cb2-3858-44e0-bd08-65d39c0286d6", "video/mp4"));
//
//        }


        View rootView = inflater.inflate(R.layout.stories_fragment, container, false);
        viewpager = rootView.findViewById(R.id.viewpager);
        ArrayList<String> pics = new ArrayList<>();
        pics.add("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Photos%2F3fe83b335638ec87?alt=media&token=85f05030-9fe5-4b7f-9521-74e7dbfae111");
        pics.add("https://firebasestorage.googleapis.com/v0/b/umefirebaseproject-a95c9.appspot.com/o/Photos%2F3fed7d5743c26aa4?alt=media&token=7cba67ff-f0f7-4b66-a614-47918bcf9e2f");

        mViewPagerAdapter = new StoriesSliderAdapter(getFragmentManager(), context);
        viewpager.setAdapter(mViewPagerAdapter);
        viewpager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mViewPagerAdapter.notifyDataSetChanged();


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
