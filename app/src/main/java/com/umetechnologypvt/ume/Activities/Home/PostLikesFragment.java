package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Activities.UserFriends;
import com.umetechnologypvt.ume.Activities.ViewPictures;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class PostLikesFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    RecyclerView recyclerview;
    PostsLikesAdapter adapter;
    ArrayList<UserModel> itemList = new ArrayList<>();
    String postId;
    private long likesCount;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_post_likes_fragment, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new PostsLikesAdapter(context, itemList, new PostsLikesAdapter.LikesAdapterCallBacks() {
            @Override
            public void takeUserToMyUserProfile(String userId) {
                Fragment fragment = new MyProfileFragment();
                loadFragment(fragment);
            }

            @Override
            public void takeUserToOtherUserProfile(String userId) {
                Constants.USER_ID = userId;
                Fragment fragment = new UserProfileFragment();
                loadFragment(fragment);
            }

            @Override
            public void addAsFriend(UserModel user) {
                sendFriendRequest(user);
            }

            @Override
            public void removeAsFriend(UserModel user) {

            }

            @Override
            public void acceptRequest(UserModel user) {
                acceptFriendRequest(user);
            }
        });
        recyclerview.setAdapter(adapter);
        postId = Constants.POST_ID;

        getDataFromDB();


        return rootView;
    }
    private void acceptFriendRequest(UserModel user) {
        CommonUtils.showToast("Accepted");
        SharedPrefs.getUserModel().getConfirmFriends().add(user.getUsername());
//        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("confirmFriends").setValue(user.getConfirmFriends());

//        user.getConfirmFriends().add(SharedPrefs.getUserModel().getUsername());
//        mDatabase.child("Users").child(user.getUsername()).child("confirmFriends").setValue(user.getConfirmFriends());
//        try {
//            myUserModel.getRequestReceived().remove(myUserModel.getRequestReceived().indexOf(hisUserModel.getUsername()));
//
//        } catch (Exception e) {
//
//        }
//
//        mDatabase.child("Users").child(myUserModel.getUsername()).child("requestReceived").setValue(myUserModel.getRequestReceived());
//
//        try {
//            hisUserModel.getRequestSent().remove(hisUserModel.getRequestSent().indexOf(SharedPrefs.getUserModel().getUsername()));
//
//        } catch (Exception e) {
//
//        }
//        mDatabase.child("Users").child(hisUserModel.getUsername()).child("requestSent").setValue(hisUserModel.getRequestSent());
//
//        sendAcceptRequestNotification();


    }

    private void sendFriendRequest(UserModel user) {
        if (user != null) {
            List<String> list = SharedPrefs.getUserModel().getRequestSent();
            list.add(user.getUsername());
            SharedPrefs.getUserModel().setRequestSent(list);
            if (SharedPrefs.getUserModel().getRequestSent().contains(user.getUsername())) {
//            CommonUtils.showToast("Already sent");
            } else {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("requestSent")
                        .setValue(SharedPrefs.getUserModel().getRequestSent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Request Sent");
                        sendNewFriendRequestNotification(user);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
        if (user != null) {
            if (SharedPrefs.getUserModel().getRequestReceived().contains(user.getUsername())) {
            } else {
                user.getRequestReceived().add(SharedPrefs.getUserModel().getUsername());
                mDatabase.child("Users").child(user.getUsername()).child("requestReceived").setValue(user.getRequestReceived()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

            }
        } else {
            CommonUtils.showToast("No internet");
        }
    }

    private void sendNewFriendRequestNotification(UserModel user) {
        NotificationAsync notificationAsync = new NotificationAsync(getActivity());
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New friend request from " + SharedPrefs.getUserModel().getName();
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", user.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
        );
        String key = mDatabase.push().getKey();

        NotificationModel model = new NotificationModel(
                key, user.getUsername(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getPicUrl(),
                SharedPrefs.getUserModel().getName() == null ? " " : SharedPrefs.getUserModel().getName() + " sent you friend request",
                "newRequest",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(user.getUsername()).child(key).setValue(model);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getDataFromDB() {
        mDatabase.child("Posts").child("Likes").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    likesCount = dataSnapshot.getChildrenCount();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        getUsersFromDB(snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUsersFromDB(String key) {
        mDatabase.child("Users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        itemList.add(userModel);
                        if (itemList.size() == likesCount) {
                            updateLikesCountToDb(likesCount);
                        }
                    }
                    Collections.sort(itemList, new Comparator<UserModel>() {
                        @Override
                        public int compare(UserModel listData, UserModel t1) {
                            String ob1 = listData.getName();
                            String ob2 = t1.getName();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLikesCountToDb(long likesCount) {
        mDatabase.child("Posts").child("Posts").child(postId).child("likesCount").setValue(likesCount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
