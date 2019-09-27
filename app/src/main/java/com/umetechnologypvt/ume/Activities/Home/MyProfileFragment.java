package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Splash;
import com.umetechnologypvt.ume.Activities.UserFriends;
import com.umetechnologypvt.ume.Activities.UserManagement.AccountSettings;
import com.umetechnologypvt.ume.Adapters.SimpleFragmentPagerAdapter;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    TextView editProfile;
    TextView about, name, friendsCount, postCount;
    CircleImageView picture;
    TextView toolbarName;
//    RecyclerView recyclerview;

    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> postIdsList = new ArrayList<>();
    //    UserPostsAdapter adapter;
    LinearLayout frnds;

    ImageView menu;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_my_profile_fragment, container, false);
        editProfile = rootView.findViewById(R.id.editProfile);
        about = rootView.findViewById(R.id.about);
        name = rootView.findViewById(R.id.name);
        friendsCount = rootView.findViewById(R.id.friendsCount);
        postCount = rootView.findViewById(R.id.postCount);
        picture = rootView.findViewById(R.id.picture);
        toolbarName = rootView.findViewById(R.id.toolbarName);
//        recyclerview = rootView.findViewById(R.id.recyclerview);
        frnds = rootView.findViewById(R.id.frnds);
        menu = rootView.findViewById(R.id.menu);
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("postsCount"));


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        setUI();

        frnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserFriends.class);
                i.putExtra("userId", SharedPrefs.getUserModel().getUsername());
                context.startActivity(i);
            }
        });
        Constants.USER_ID = SharedPrefs.getUserModel().getUsername();
        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(context, getChildFragmentManager());

        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_grid));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_save_post));
        getUserDataFromDB();
//        if(SharedPrefs.getPosts()!=null) {
//            postCount.setText("" + SharedPrefs.getPosts().size());
//        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Constants.SAVED_POST = true;
                } else {
                    Constants.SAVED_POST = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return rootView;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("postsCount");
            postCount.setText(message);


        }
    };


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_my_profile, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.accountSettings:
//                        SharedPrefs.logout();
                        Intent i = new Intent(context, AccountSettings.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
//                        MainActivity.activity.finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    private void setUI() {

        about.setText(SharedPrefs.getUserModel().getAbout());
        toolbarName.setText(SharedPrefs.getUserModel().getName());
        name.setText(SharedPrefs.getUserModel().getName());
        friendsCount.setText("" + SharedPrefs.getUserModel().getConfirmFriends().size());
        try {
            Glide.with(context).load(SharedPrefs.getUserModel().getPicUrl()).into(picture);
        } catch (Exception e) {

        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class));
            }
        });

        friendsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserFriends.class);
                i.putExtra("userId", SharedPrefs.getUserModel().getUsername());
                context.startActivity(i);
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private void getUserDataFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    SharedPrefs.setUserModel(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);

        super.onDestroy();

    }
}
