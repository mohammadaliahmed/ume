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
import com.appsinventiv.ume.Activities.MainActivity;
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
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserModel> itemList = new ArrayList<>();
    SearchedUserListAdapter adapter;
    DatabaseReference mDatabase;
    String country, gender, currentLocation, language, interest,learnLanguage;
    int startAge = 0, endAge = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.setTitle("List of users");

        gender = getIntent().getStringExtra("gender");

        country = getIntent().getStringExtra("country");
        currentLocation = getIntent().getStringExtra("currentLocation");
        language = getIntent().getStringExtra("language");
        learnLanguage = getIntent().getStringExtra("learnLanguage");
        interest = getIntent().getStringExtra("interest");
        startAge = getIntent().getIntExtra("startAge", 0);
        endAge = getIntent().getIntExtra("endAge", 99);

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

        if (gender == null) {
            getDataFromDB();
        } else {
            getFilteredDataDrmDB();
        }


    }

    private void getFilteredDataDrmDB() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        if (model != null) {
                            if (model.getName() != null) {
//                                itemList.add(model);
                                List<String> inter = model.getInterests();
                                inter.add("any");
                                List<String> larn = model.getLearningLanguage();
                                larn.add("any");
                                model.setGender(model.getGender() + "Any");
                                model.setLanguage(model.getLanguage() + "any");
                                model.setCountry(model.getCountry() + "any");
                                model.setInterests(inter);
                                model.setLearningLanguage(larn);
                                model.setCurrentLocation(model.getCurrentLocation() + "any");

                                if (
                                        model.getCurrentLocation().contains(currentLocation) &&
                                                model.getCountry().contains(country) &&
                                                model.getGender().contains(gender) &&
                                                model.getLanguage().contains(language) &&
                                                model.getInterests().contains(interest) &&
                                                model.getLearningLanguage().contains(learnLanguage) &&
                                                model.getAge() >= startAge && model.getAge() <= endAge&&
                                                !model.getUsername().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())
                                        ) {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            Intent i = new Intent(SearchActivity.this, Filters.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

}
