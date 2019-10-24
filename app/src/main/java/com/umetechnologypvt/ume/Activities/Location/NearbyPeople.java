package com.umetechnologypvt.ume.Activities.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.umetechnologypvt.ume.Activities.GPSTrackerActivity;
import com.umetechnologypvt.ume.Models.LocationUserModel;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class NearbyPeople extends AppCompatActivity {

    double lat, lon;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    ArrayList<LocationUserModel> itemList = new ArrayList<>();
    LocationSearchUserAdapter adapter;
    SeekBar seekBar;
    private int distance;
    TextView distanceChosen;
    private UserModel myUserModel;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = new Intent(NearbyPeople.this, GPSTrackerActivity.class);
        startActivityForResult(intent, 1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Nearby people");

        distanceChosen = findViewById(R.id.distanceChosen);
        seekBar = findViewById(R.id.seekBar);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new LocationSearchUserAdapter(this, itemList, SharedPrefs.getUserModel(), new LocationSearchUserAdapter.LocationSearchAdapterCallbacks() {
            @Override
            public void addAsFriend(UserModel model) {
                sendFriendRequest(model);
            }
        });
        recyclerView.setAdapter(adapter);
//        getDataFromDB();
        getPermissions();
        seekBar.setProgress(50);
        distance = seekBar.getProgress();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                CommonUtils.showToast(""+seekBar.getProgress());
                distanceChosen.setText(seekBar.getProgress() + " km");
                adapter.filter(seekBar.getProgress());
            }
        });
        getUserDataFromDB();
    }

    private void getUserDataFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    myUserModel = dataSnapshot.getValue(UserModel.class);
                    SharedPrefs.setUserModel(myUserModel);
                    adapter.setMyUserModel(myUserModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendFriendRequest(UserModel hisUserModel) {


        if (myUserModel.getRequestSent().contains(hisUserModel.getUsername())) {
//            CommonUtils.showToast("Already sent");
        } else {
            myUserModel.getRequestSent().add(hisUserModel.getUsername());
            mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("requestSent")
                    .setValue(myUserModel.getRequestSent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CommonUtils.showToast("Request Sent");
//                        addAsFriend.setText("Request sent");
//                        addAsFriend.setEnabled(false);
//                        addAsFriend.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    sendNewFriendRequestNotification(hisUserModel);
                }
            });

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

    private void sendNewFriendRequestNotification(UserModel hisUserModel) {
        NotificationAsync notificationAsync = new NotificationAsync(this);
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


    private void getDataFromDB() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        if (model != null) {
                            if (model.getName() != null) {
                                if (!model.getUsername().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                    if (model.getLatitude() != 0 && model.getLongitude() != 0) {
                                        itemList.add(new LocationUserModel(model,
                                                CommonUtils.distance(
                                                        model.getLatitude(),
                                                        model.getLongitude(),
                                                        SharedPrefs.getUserModel().getLatitude(),
                                                        SharedPrefs.getUserModel().getLongitude())));
                                    }

                                }
                            }
                        }
                    }
                    Collections.sort(itemList, new Comparator<LocationUserModel>() {
                        @Override
                        public int compare(LocationUserModel listData, LocationUserModel t1) {
                            String ob1 = "" + listData.getUserModel().getStatus();
                            String ob2 = "" + t1.getUserModel().getStatus();

                            return ob2.compareTo(ob1);

                        }
                    });
//                    Collections.sort(itemList, new Comparator<LocationUserModel>() {
//                        @Override
//                        public int compare(LocationUserModel listData, LocationUserModel t1) {
//                            Double ob1 = listData.getDistance();
//                            Double ob2 = t1.getDistance();
//
//                            return ob1.compareTo(ob2);
//
//                        }
//                    });
                    adapter.updateList(itemList);
                    adapter.filter(50);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lon = extras.getDouble("Longitude");
                lat = extras.getDouble("Latitude");

                updateLocationToDb(lat, lon);


            }

        }
    }

    private void updateLocationToDb(double lat, double lon) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("latitude", lat);
        map.put("longitude", lon);
        UserModel user = SharedPrefs.getUserModel();
        user.setLatitude(lat);
        user.setLongitude(lon);
        SharedPrefs.setUserModel(user);
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).updateChildren(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,


        };

        if (!hasPermissions(NearbyPeople.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
//            getDataFromMobile();
            getDataFromDB();
        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }
}
