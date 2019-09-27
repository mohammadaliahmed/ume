package com.umetechnologypvt.ume.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseException;
import com.umetechnologypvt.ume.Adapters.ForwardUserListAdapter;
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

public class ForwardContactSelectionScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserModel> itemList = new ArrayList<>();
    ForwardUserListAdapter adapter;
    DatabaseReference mDatabase;
    TextView noContacts;
    String msg;
    public static boolean fromForward;
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
        this.setTitle("Select Contact to forward");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        msg = getIntent().getStringExtra("msg");
        fromForward = true;

        noContacts = findViewById(R.id.noContacts);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new ForwardUserListAdapter(this, itemList, new ForwardUserListAdapter.ForwardUserListAdapterCallback() {
            @Override
            public void contactSelected(String username) {
                Intent i = new Intent(ForwardContactSelectionScreen.this, SingleChattingScreen.class);
                i.putExtra("msg", msg);
                i.putExtra("userId", username);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        for (String abc : SharedPrefs.getUserModel().getConfirmFriends()) {
            getFriendsFromDB(abc);
        }


    }




    private void getFriendsFromDB(String userId) {
        try {
            mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        map.put(userId, userModel);
                        itemList.clear();
                        for (Map.Entry<String, UserModel> entry : map.entrySet()) {
                            if (!entry.getKey().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                itemList.add(entry.getValue());
                            }
                        }
//                    if (userModel != null) {
//                        itemList.add(userModel);
//                    }
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, "UME\n Download Now\n" + "http://play.google.com/store/apps/details?id=" + ForwardContactSelectionScreen.this.getPackageName());
            startActivity(Intent.createChooser(shareIntent, "Share App via.."));

        }

        return super.onOptionsItemSelected(item);
    }



}
