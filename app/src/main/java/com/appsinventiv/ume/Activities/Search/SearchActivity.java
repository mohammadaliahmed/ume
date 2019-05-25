package com.appsinventiv.ume.Activities.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appsinventiv.ume.Activities.ContactSelectionScreen;
import com.appsinventiv.ume.Activities.EditProfile;
import com.appsinventiv.ume.Adapters.ChatListAdapter;
import com.appsinventiv.ume.Adapters.SearchedUserListAdapter;
import com.appsinventiv.ume.Adapters.UserListAdapter;
import com.appsinventiv.ume.Models.ChatListModel;
import com.appsinventiv.ume.Models.ChatModel;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserModel> itemList = new ArrayList<>();
    SearchedUserListAdapter adapter;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase=FirebaseDatabase.getInstance().getReference();
        this.setTitle("List of users");

        recyclerView = findViewById(R.id.recyclerview);

        adapter = new SearchedUserListAdapter(this, itemList, new SearchedUserListAdapter.SearchUserCallbacks() {
            @Override
            public void addAsFriend(UserModel model) {
            }

            @Override
            public void removeFriend(UserModel model) {
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getDataFromDB();


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
                                    itemList.add(model);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

}
