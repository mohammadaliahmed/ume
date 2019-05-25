package com.appsinventiv.ume.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CompressImage;
import com.appsinventiv.ume.Utils.Constants;
import com.appsinventiv.ume.Utils.GifSizeFilter;
import com.appsinventiv.ume.Utils.NotificationAsync;
import com.appsinventiv.ume.Utils.NotificationObserver;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");

        addAsFriend = findViewById(R.id.addAsFriend);
        titleName = findViewById(R.id.titleName);
        userName = findViewById(R.id.userName);
        back = findViewById(R.id.back);
        userPic = findViewById(R.id.userPic);

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

        myUserModel.getConfirmFriends().remove(myUserModel.getConfirmFriends().indexOf(userId));
        hisUserModel.getConfirmFriends().remove(hisUserModel.getConfirmFriends().indexOf(SharedPrefs.getUserModel().getUsername()));


        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("confirmFriends").setValue(myUserModel.getConfirmFriends());
        mDatabase.child("Users").child(hisUserModel.getUsername()).child("confirmFriends").setValue(hisUserModel.getConfirmFriends());
    }

    private void acceptRequest() {
        CommonUtils.showToast("Accepted");
        myUserModel.getConfirmFriends().add(userId);
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("confirmFriends").setValue(myUserModel.getConfirmFriends());

        hisUserModel.getConfirmFriends().add(SharedPrefs.getUserModel().getUsername());
        mDatabase.child("Users").child(hisUserModel.getUsername()).child("confirmFriends").setValue(hisUserModel.getConfirmFriends());


        myUserModel.getRequestReceived().remove(myUserModel.getRequestReceived().indexOf(hisUserModel.getUsername()));
        mDatabase.child("Users").child(myUserModel.getUsername()).child("requestReceived").setValue(myUserModel.getRequestReceived());

        hisUserModel.getRequestSent().remove(hisUserModel.getRequestSent().indexOf(SharedPrefs.getUserModel().getUsername()));
        mDatabase.child("Users").child(hisUserModel.getUsername()).child("requestSent").setValue(hisUserModel.getRequestSent());


    }

    private void getMyDataFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        CommonUtils.showToast("");
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

    }

    private void getDataFromDB() {
        mDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    hisUserModel = dataSnapshot.getValue(UserModel.class);
                    if (hisUserModel != null) {
                        setUserProfileData(hisUserModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUserProfileData(UserModel user) {
        if (user.getPicUrl() != null) {
            try {
                Glide.with(UserProfileScreen.this).load(user.getPicUrl()).into(userPic);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        titleName.setText(user.getName());
        userName.setText(user.getName());
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