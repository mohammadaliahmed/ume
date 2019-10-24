package com.umetechnologypvt.ume.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.umetechnologypvt.ume.Adapters.NotificationsListAdapter;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Utils.SwipeControllerActions;
import com.umetechnologypvt.ume.Utils.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NotificationsList extends AppCompatActivity implements NotificationObserver {
    RecyclerView recyclerview;
    NotificationsListAdapter adapter;
    private ArrayList<NotificationModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    TextView notiText;
    private SwipeToDeleteCallback swipeController;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Notifications");
        recyclerview = findViewById(R.id.recyclerview);
        notiText = findViewById(R.id.notiText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new NotificationsListAdapter(this, itemList, new NotificationsListAdapter.NotificationCallbacks() {
            @Override
            public void onAccept(String userId, NotificationModel model) {
                acceptRequest(userId);
                mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).child(model.getNotifcationId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getDataFromDB();
                    }
                });
            }

            @Override
            public void onDelete(String userId, NotificationModel model) {
                deleteRequest(userId);
                mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).child(model.getNotifcationId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getDataFromDB();
                    }
                });
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

                showAlert(itemList.get(position).getNotifcationId());

            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerview);

        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        getDataFromDB();
        SharedPrefs.setNotificationCount("0");

    }

    private void deleteRequest(String userId) {
        getUserDataDelete(userId);
    }

    private void acceptRequest(String userId) {
        getUserData(userId);
    }

    private void getUserDataDelete(String userId) {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if (userModel != null) {
                    CommonUtils.showToast("Deleted");
                    try {
                        SharedPrefs.getUserModel().getRequestReceived().remove(SharedPrefs.getUserModel().getRequestReceived().indexOf(userId));
                        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("requestReceived").setValue(SharedPrefs.getUserModel().getRequestReceived());
                        userModel.getRequestSent().remove(userModel.getRequestSent().indexOf(SharedPrefs.getUserModel()));
                        mDatabase.child("Users").child(userModel.getUsername()).child("requestSent").setValue(userModel.getRequestSent());

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserData(String userId) {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel hisUserModel = dataSnapshot.getValue(UserModel.class);
                if (hisUserModel != null) {
                    CommonUtils.showToast("Accepted");
                    UserModel myUserModel = SharedPrefs.getUserModel();
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

                    sendAcceptRequestNotification(hisUserModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendAcceptRequestNotification(UserModel userModel) {
        NotificationAsync notificationAsync = new NotificationAsync(NotificationsList.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = SharedPrefs.getUserModel().getName() + " accepted your friend request";
        String NotificationMessage = "Click to view ";

        notificationAsync.execute("ali", userModel.getFcmKey(), NotificationTitle, NotificationMessage, "friend", "friendRequest",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
        );
        String key = mDatabase.push().getKey();
        NotificationModel model = new NotificationModel(
                key, userModel.getUsername(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getPicUrl(),
                SharedPrefs.getUserModel().getName() + " accepted your friend request",
                "requestAccept",
                System.currentTimeMillis()
        );


        mDatabase.child("Notifications").child(userModel.getUsername()).child(key).setValue(model);
    }

    private void getDataFromDB() {
        mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    notiText.setVisibility(View.GONE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NotificationModel model = snapshot.getValue(NotificationModel.class);
                        if (model != null) {
                            if (!SharedPrefs.getUserModel().getConfirmFriends().contains(model.getId())) {
                                itemList.add(model);
                            }

                        }
                    }
                    Collections.sort(itemList, new Comparator<NotificationModel>() {
                        @Override
                        public int compare(NotificationModel listData, NotificationModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob2.compareTo(ob1);

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    notiText.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAlert(String notiId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NotificationsList.this);
        builder1.setMessage("Delete notification?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).child(notiId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Removed");
                            }
                        });
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
        AlertDialog.Builder builder;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
