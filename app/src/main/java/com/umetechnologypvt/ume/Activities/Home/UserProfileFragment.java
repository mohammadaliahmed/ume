package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Fragments.SimpleFragmentPagerAdapter2;
import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Activities.UserFriends;
import com.umetechnologypvt.ume.Activities.UserProfileScreen;
import com.umetechnologypvt.ume.Activities.ViewPictures;
import com.umetechnologypvt.ume.Adapters.SimpleFragmentPagerAdapter;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileFragment extends Fragment {
    Context context;

    LinearLayout frnds;
    DatabaseReference mDatabase;
    //    TextView editProfile;
    TextView about, name, friendsCount, postCount;
    CircleImageView picture;
    TextView toolbarName;


    String userId;
    ImageView back;
    TextView startChat;
    TextView addAsFriend, viewProfile;
    int abc = 0;
    UserModel myUserModel, hisUserModel;
    private long totalPostCount;
    ImageView menu;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_user_profile_fragment, container, false);
//        editProfile = rootView.findViewById(R.id.editProfile);
        about = rootView.findViewById(R.id.about);
        name = rootView.findViewById(R.id.name);
        friendsCount = rootView.findViewById(R.id.friendsCount);
        frnds = rootView.findViewById(R.id.frnds);
        menu = rootView.findViewById(R.id.menu);
        postCount = rootView.findViewById(R.id.postCount);
        picture = rootView.findViewById(R.id.picture);
        toolbarName = rootView.findViewById(R.id.toolbarName);
        back = rootView.findViewById(R.id.back);
        startChat = rootView.findViewById(R.id.startChat);
        addAsFriend = rootView.findViewById(R.id.addAsFriend);
        viewProfile = rootView.findViewById(R.id.viewProfile);

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("postsCount"));

        userId = Constants.USER_ID;


        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter2 adapter = new SimpleFragmentPagerAdapter2(context, getChildFragmentManager());

        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_grid));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_save_post));
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
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileScreen.class);
                i.putExtra("userId", userId);
                context.startActivity(i);
            }

        });

        getUserDataFromDB();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        if (userId != null) {
            Constants.POST_ID = userId;
            getUserDataFromDB();
        }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewPictures.class);
                i.putExtra("url", hisUserModel.getPicUrl());
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        friendsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserFriends.class);
                i.putExtra("userId", userId);
                context.startActivity(i);
            }
        });
        frnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserFriends.class);
                i.putExtra("userId", userId);
                context.startActivity(i);
            }
        });


        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SingleChattingScreen.class);
                i.putExtra("userId", userId);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
        addAsFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (abc == 0) {
                    sendFriendRequest();
                } else if (abc == 1) {

                } else if (abc == 2) {
                    acceptRequest();
                } else if (abc == 3) {
                    removeAsFriend();
                }

            }
        });

//        getDataFromDB();
        getMyDataFromDB();


        return rootView;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_example, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shareprofile:
                        CommonUtils.shareUrl(context, "profile", userId);
                        return true;
                    case R.id.copyprofile:
                        CommonUtils.copyUrl(context, "profile", userId);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("postsCount");
            postCount.setText(message);


        }
    };

    private void sendFriendRequest() {
        if (myUserModel != null) {

            if (myUserModel.getRequestSent().contains(userId)) {
//            CommonUtils.showToast("Already sent");
            } else {
                myUserModel.getRequestSent().add(userId);
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("requestSent")
                        .setValue(myUserModel.getRequestSent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Request Sent");
                        addAsFriend.setText("Request sent");
                        addAsFriend.setEnabled(false);
//                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        sendNewFriendRequestNotification();
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
        if (hisUserModel != null) {

            hisUserModel.getRequestReceived().add(SharedPrefs.getUserModel().getUsername());
            mDatabase.child("Users").child(hisUserModel.getUsername()).child("requestReceived").setValue(hisUserModel.getRequestReceived()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });

        }

    }

    private void removeAsFriend() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure");
        builder1.setMessage("Remove as friend?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {


                            myUserModel.getConfirmFriends().remove(myUserModel.getConfirmFriends().indexOf(userId));
                            hisUserModel.getConfirmFriends().remove(hisUserModel.getConfirmFriends().indexOf(SharedPrefs.getUserModel().getUsername()));


                            mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("confirmFriends").setValue(myUserModel.getConfirmFriends());
                            mDatabase.child("Users").child(hisUserModel.getUsername()).child("confirmFriends").setValue(hisUserModel.getConfirmFriends()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    CommonUtils.showToast("Removed from friends");
                                }
                            });
                            dialog.cancel();
                        } catch (Exception e) {
                            CommonUtils.showToast(e.getMessage());
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void acceptRequest() {
        CommonUtils.showToast("Accepted");
        myUserModel.getConfirmFriends().add(userId);
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("confirmFriends").setValue(myUserModel.getConfirmFriends());

        hisUserModel.getConfirmFriends().add(SharedPrefs.getUserModel().getUsername());
        mDatabase.child("Users").child(hisUserModel.getUsername()).child("confirmFriends").setValue(hisUserModel.getConfirmFriends());
        try {
            myUserModel.getRequestReceived().remove(myUserModel.getRequestReceived().indexOf(hisUserModel.getUsername()));

        } catch (Exception e) {

        }

        mDatabase.child("Users").child(myUserModel.getUsername()).child("requestReceived").setValue(myUserModel.getRequestReceived());

        try {
            hisUserModel.getRequestSent().remove(hisUserModel.getRequestSent().indexOf(SharedPrefs.getUserModel().getUsername()));

        } catch (Exception e) {

        }
        mDatabase.child("Users").child(hisUserModel.getUsername()).child("requestSent").setValue(hisUserModel.getRequestSent());

        sendAcceptRequestNotification();


    }


    private void sendAcceptRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(getActivity());
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = SharedPrefs.getUserModel().getName() + " accepted your friend request";
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", hisUserModel.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
        );
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key, hisUserModel.getUsername(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getPicUrl(),
                SharedPrefs.getUserModel().getName() + " accepted your friend request",
                "requestAccept",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(hisUserModel.getUsername()).child(key).setValue(model);

    }

    private void getMyDataFromDB() {
        try {


            mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        myUserModel = dataSnapshot.getValue(UserModel.class);
                        if (myUserModel.getRequestSent().contains(userId)) {
                            addAsFriend.setText("Request sent");
                            addAsFriend.setEnabled(false);
                            addAsFriend.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                            addAsFriend.setTextColor(getResources().getColor(R.color.colorGreyDark));
                            abc = 1;
                        } else if (myUserModel.getRequestReceived().contains(userId)) {
                            abc = 2;
                            addAsFriend.setText("Accept request");
                            addAsFriend.setEnabled(true);
                            try {
                                addAsFriend.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                                addAsFriend.setTextColor(getResources().getColor(R.color.colorGreyDark));
                            }catch (Exception e){

                            }

                        } else if (myUserModel.getConfirmFriends().contains(userId)) {
                            abc = 3;
                            addAsFriend.setText("Friend");
                            addAsFriend.setEnabled(true);
                            addAsFriend.setBackground(getResources().getDrawable(R.drawable.btn_bg_grey));
                            addAsFriend.setTextColor(getResources().getColor(R.color.colorGreyDark));
                        } else {
                            abc = 0;
                            addAsFriend.setText("Add as Friend");
                            addAsFriend.setEnabled(true);
//                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            addAsFriend.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_grey));
                            addAsFriend.setTextColor(context.getResources().getColor(R.color.colorGreyDark));
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    private void sendNewFriendRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(getActivity());
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New friend request from " + SharedPrefs.getUserModel().getName();
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", hisUserModel.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
        );
        String key = mDatabase.push().getKey();

        NotificationModel model = new NotificationModel(
                key, hisUserModel.getUsername(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getPicUrl(),
                SharedPrefs.getUserModel().getName() == null ? " " : SharedPrefs.getUserModel().getName() + " sent you friend request",
                "newRequest",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(hisUserModel.getUsername()).child(key).setValue(model);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //
    private void getUserDataFromDB() {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    hisUserModel = dataSnapshot.getValue(UserModel.class);
                    if (hisUserModel != null) {
                        about.setText(hisUserModel.getAbout());
                        toolbarName.setText(hisUserModel.getName());
                        name.setText(hisUserModel.getName());
                        friendsCount.setText("" + hisUserModel.getConfirmFriends().size());
                        try {
                            Glide.with(context).load(hisUserModel.getPicUrl()).into(picture);
                        } catch (Exception e) {

                        }


//                        getPostIdsFromDB(hisUserModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

//    private void getPostIdsFromDB(UserModel userModel) {
//        mDatabase.child("PostsBy").child(userModel.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    postIdsList.clear();
//                    totalPostCount = dataSnapshot.getChildrenCount();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String key = snapshot.getKey();
//                        postIdsList.add(key);
//                    }
//
//                    if (postIdsList.size() > 0) {
//                        itemList.clear();
//                        for (String id : postIdsList) {
//                            getPostsFromDB(id);
//                        }
//                    } else {
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.shareprofile:
//                CommonUtils.shareUrl(context, "profile", userId);
//                return true;
//            case R.id.copyprofile:
//                CommonUtils.copyUrl(context,"profile",userId);
//                return true;
//            default:
//                return false;
//        }
//    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);

        super.onDestroy();

    }

}
