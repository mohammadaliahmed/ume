package com.umetechnologypvt.ume.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseException;
import com.umetechnologypvt.ume.Adapters.UserListAdapter;
import com.umetechnologypvt.ume.Models.PhoneContactModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
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
    UserListAdapter adapter;
    DatabaseReference mDatabase;
    TextView noContacts;
    ArrayList<PhoneContactModel> phoneContacts = new ArrayList<>();
    HashMap<String, UserModel> map = new HashMap<>();

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

        noContacts = findViewById(R.id.noContacts);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new UserListAdapter(this, itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getPermissions();
//        getDataFromDB();


    }

    private void getDataFromMobile() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneContacts.add(new PhoneContactModel(name, phoneNumber));

        }
        if (phoneContacts.size() > 0) {
            getDataFromDB();
            for (PhoneContactModel contact : phoneContacts) {
                String numer = contact.getNumber();
                if (numer.startsWith("03")) {
                    numer = numer.substring(1);
                    numer = "+92" + numer;
                }
                numer = numer.replace(" ", "").replace("-", "");

                getFriendsFromDB(numer);
            }
        }

        adapter.notifyDataSetChanged();
        phones.close();
    }


    private void getDataFromDB() {

        mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    noContacts.setVisibility(View.GONE);
                    itemList.clear();
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    if (model != null) {
                        if (model.getConfirmFriends().size() > 0) {
                            for (String userId : model.getConfirmFriends()) {
                                if (userId != null) {
                                    getFriendsFromDB(userId);
                                }
                            }
                        } else {
                            noContacts.setVisibility(View.VISIBLE);
                        }

                    }

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
                                if (listData.getName() != null) {
                                    return ob1.compareTo(ob2);

                                } else {
                                    return 0;

                                }

                            }
                        });
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
            getDataFromMobile();
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
