package com.umetechnologypvt.ume.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.umetechnologypvt.ume.Adapters.UserListAdapter;
import com.umetechnologypvt.ume.Interface.ContactListCallbacks;
import com.umetechnologypvt.ume.Models.PhoneContactModel;
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
import java.util.Map;

public class ContactSelectionScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserModel> itemList = new ArrayList<>();
    ArrayList<String> blockedList = new ArrayList<>();
    UserListAdapter adapter;
    DatabaseReference mDatabase;
    //    TextView noContacts;
    HashMap<String, UserModel> map = new HashMap<>();
    private ArrayList<String> blockedMeList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Select Contact");
        mDatabase = FirebaseDatabase.getInstance().getReference();

//        noContacts = findViewById(R.id.noContacts);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new UserListAdapter(this, itemList, blockedList, blockedMeList, new ContactListCallbacks() {
            @Override
            public void onBlock(UserModel model) {
                showBlockAlert(model);
            }

            @Override
            public void onUnBlock(UserModel model) {
                showUnBlockAlert(model);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getPermissions();
//        getDataFromDB();

        getBlockListFromDB();
        getMeBlockListFromDB();

        for (String abc : SharedPrefs.getUserModel().getConfirmFriends()) {
            getFriendsFromDB(abc);
        }
    }

    private void showUnBlockAlert(UserModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactSelectionScreen.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to unblock this user?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername())
                        .child("blockedUsers").child(model.getUsername()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("UnBlocked");
                    }
                });
                mDatabase.child("Users").child(model.getUsername()).child("blockedMe")
                        .child(SharedPrefs.getUserModel().getUsername()).removeValue().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showBlockAlert(UserModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactSelectionScreen.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to block this user?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername())
                        .child("blockedUsers").child(model.getUsername()).setValue(model.getUsername()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Blocked");
                    }
                });
                mDatabase.child("Users").child(model.getUsername()).child("blockedMe")
                        .child(SharedPrefs.getUserModel().getUsername()).setValue(SharedPrefs.getUserModel().getUsername()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getBlockListFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("blockedUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    blockedList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = snapshot.getKey();
                        blockedList.add(username);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    blockedList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMeBlockListFromDB() {
        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("blockedMe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    blockedMeList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = snapshot.getKey();
                        blockedMeList.add(username);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    blockedMeList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFriendsFromDB(String userId) {
        try {
            mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            map.put(userId, userModel);
                            itemList.clear();
                            for (Map.Entry<String, UserModel> entry : map.entrySet()) {
                                if (!entry.getKey().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                    itemList.add(entry.getValue());
                                }
                            }

                        }
                        getSupportActionBar().setSubtitle(itemList.size() + " Contacts");

                        Collections.sort(itemList, new Comparator<UserModel>() {
                            @Override
                            public int compare(UserModel listData, UserModel t1) {
                                String ob1 = listData.getName();
                                String ob2 = t1.getName();
                                try {
                                    return ob1.compareTo(ob2);


                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                return 0;
                            }
                        });
//                        adapter.setUserList(itemList);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        if (id == R.id.action_invite) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "UME\n Download Now\n" + "http://play.google.com/store/apps/details?id=" + ContactSelectionScreen.this.getPackageName());
            startActivity(Intent.createChooser(shareIntent, "Share App via.."));

        }

        return super.onOptionsItemSelected(item);
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,


        };

        if (!hasPermissions(ContactSelectionScreen.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
//            getDataFromMobile();
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
