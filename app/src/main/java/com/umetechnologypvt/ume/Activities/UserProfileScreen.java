package com.umetechnologypvt.ume.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umetechnologypvt.ume.Adapters.InterestAdapter;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileScreen extends AppCompatActivity implements NotificationObserver {
    String userId;
    DatabaseReference mDatabase;
    private UserModel hisUserModel;
    Button addAsFriend;
    TextView titleName, userName;
    ImageView back;
    CircleImageView userPic;
    private UserModel myUserModel;
    int abc = 0;
    TextView name, username, country, language, learningLanguage, lastOnline, memberSince, currenLocation;
    RecyclerView recyclerview;
    private InterestAdapter adapter;

    FlexboxLayout container;
    TextView gender;
    ImageView flag, genderPic;
    Button startChat;
    RelativeLayout genderBg;

    TextView localTime;
    TextView about;
    TextView age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        container = findViewById(R.id.v_container);
        flag = findViewById(R.id.flag);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");


        genderBg = findViewById(R.id.genderBg);
        age = findViewById(R.id.age);
        about = findViewById(R.id.about);
        gender = findViewById(R.id.gender);
        genderPic = findViewById(R.id.genderPic);
        recyclerview = findViewById(R.id.recyclerview);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        country = findViewById(R.id.country);
        language = findViewById(R.id.language);
        learningLanguage = findViewById(R.id.learningLanguage);
        lastOnline = findViewById(R.id.lastOnline);
        memberSince = findViewById(R.id.memberSince);
        currenLocation = findViewById(R.id.currenLocation);
        addAsFriend = findViewById(R.id.addAsFriend);
        titleName = findViewById(R.id.titleName);
        userName = findViewById(R.id.userName);
        back = findViewById(R.id.back);
        userPic = findViewById(R.id.userPic);
        startChat = findViewById(R.id.startChat);
        localTime = findViewById(R.id.localTime);
        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileScreen.this, ViewPictures.class);
                i.putExtra("url", hisUserModel.getPicUrl());
                startActivity(i);
            }
        });

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserProfileScreen.this, SingleChattingScreen.class);
                i.putExtra("userId", userId);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        getDataFromDB();
        getMyDataFromDB();

    }


    private void removeAsFriend() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfileScreen.this);
        builder1.setTitle("Are you sure");
        builder1.setMessage("Remove as friend?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
        NotificationAsync notificationAsync = new NotificationAsync(UserProfileScreen.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = SharedPrefs.getUserModel().getName() + " accepted your friend request";
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", hisUserModel.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length()
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
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    myUserModel = dataSnapshot.getValue(UserModel.class);
                    if (myUserModel.getRequestSent().contains(userId)) {
                        addAsFriend.setText("Request sent");
                        addAsFriend.setEnabled(false);
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        abc = 1;
                    } else if (myUserModel.getRequestReceived().contains(userId)) {
                        abc = 2;
                        addAsFriend.setText("Accept request");
                        addAsFriend.setEnabled(true);
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    } else if (myUserModel.getConfirmFriends().contains(userId)) {
                        abc = 3;
                        addAsFriend.setText("Unfriend");
                        addAsFriend.setEnabled(true);
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    } else {
                        abc = 0;
                        addAsFriend.setText("Add as Friend");
                        addAsFriend.setEnabled(true);
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        sendNewFriendRequestNotification();
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
        if (hisUserModel != null) {
            if (hisUserModel.getRequestReceived().contains(SharedPrefs.getUserModel().getUsername())) {
//            CommonUtils.showToast("Already exits");
            } else {
                hisUserModel.getRequestReceived().add(SharedPrefs.getUserModel().getUsername());
                mDatabase.child("Users").child(hisUserModel.getUsername()).child("requestReceived").setValue(hisUserModel.getRequestReceived()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
    }

    private void sendNewFriendRequestNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(UserProfileScreen.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New friend request from " + SharedPrefs.getUserModel().getName();
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", hisUserModel.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length()
        );
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key, hisUserModel.getUsername(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getPicUrl(),
                SharedPrefs.getUserModel().getName() + " sent you friend request",
                "newRequest",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(hisUserModel.getUsername()).child(key).setValue(model);
    }


    private void getDataFromDB() {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    hisUserModel = dataSnapshot.getValue(UserModel.class);
                    if (hisUserModel != null) {
                        inflatelayout(hisUserModel.getInterests());
                        setUserProfileData(hisUserModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inflatelayout(List<String> interests) {
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(5, 5, 5, 5);
        for (int i = 0; i < interests.size(); i++) {
            final TextView tv = new TextView(getApplicationContext());
            tv.setText(interests.get(i));
            tv.setHeight(100);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setBackground(getResources().getDrawable(R.drawable.interest_bg));
            tv.setId(i + 1);
            tv.setLayoutParams(buttonLayoutParams);
            tv.setTag(i);
            tv.setPadding(20, 10, 20, 10);
            container.addView(tv);
        }


    }

    private void setUserProfileData(UserModel user) {
        name.setText(user.getName());
        username.setText(user.getUsername());
        age.setText("" + user.getAge());
        language.setText(user.getLanguage());
        learningLanguage.setText(user.getLearningLanguage() + "");
        memberSince.setText(CommonUtils.getFormattedDate(user.getTime()));
        currenLocation.setText(user.getCurrentLocation());
        if (user.getCountryNameCode() != null) {
            flag.setVisibility(View.VISIBLE);

            Glide.with(UserProfileScreen.this).load(CountryUtils.getFlagDrawableResId(user.getCountryNameCode())).into(flag);
        } else {
            flag.setVisibility(View.GONE);
        }
        if (user.getGender() != null) {
            gender.setText(user.getGender());
            if (user.getGender().equalsIgnoreCase("female")) {
                try {
                    Glide.with(this).load(R.drawable.ic_female).into(genderPic);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                genderBg.setBackground(UserProfileScreen.this.getResources().getDrawable(R.drawable.custom_corners_pink));
            } else {
                try {
                    Glide.with(this).load(R.drawable.ic_male).into(genderPic);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                genderBg.setBackground(UserProfileScreen.this.getResources().getDrawable(R.drawable.custom_corners_blue));

            }
        }
        lastOnline.setText(hisUserModel.getStatus()
                .equalsIgnoreCase("Online") ? "Online" : "Last seen " + CommonUtils.getFormattedDate(Long.parseLong(hisUserModel.getStatus()))
        );

        if (user.getCountryNameCode() != null) {
            localTime.setText(CountryUtils.getGMT(user.getCountryNameCode()) + " " + CommonUtils.getLocalTime(System.currentTimeMillis(), CountryUtils.getGMT(user.getCountryNameCode())));

        }
        country.setText(user.getCountry());
        if (user.getPicUrl() != null) {
            try {
                Glide.with(UserProfileScreen.this).load(user.getPicUrl()).into(userPic);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Glide.with(UserProfileScreen.this).load(R.drawable.ic_profile_plc).into(userPic);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        titleName.setText(user.getName());
        userName.setText(user.getName());
        about.setText(user.getAbout());
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}