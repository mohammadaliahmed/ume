package com.umetechnologypvt.ume.Activities.Location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.GPSTrackerActivity;
import com.umetechnologypvt.ume.Models.LocationUserModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
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
        adapter = new LocationSearchUserAdapter(this, itemList, new LocationSearchUserAdapter.LocationSearchAdapterCallbacks() {
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
