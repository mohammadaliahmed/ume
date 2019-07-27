package com.umetechnologypvt.ume.Activities.Search;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umetechnologypvt.ume.Adapters.SearchedUserListAdapter;
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
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserModel> itemList = new ArrayList<>();
    SearchedUserListAdapter adapter;
    DatabaseReference mDatabase;
    String country, gender, currentLocation, language, interest, learnLanguage, word;
    int startAge = 0, endAge = 99;
    ProgressBar progress;
    TextView noResults;

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

        noResults = findViewById(R.id.noResults);
        progress = findViewById(R.id.progress);
        gender = getIntent().getStringExtra("gender");

        country = getIntent().getStringExtra("country");
        currentLocation = getIntent().getStringExtra("currentLocation");
        language = getIntent().getStringExtra("language");
        learnLanguage = getIntent().getStringExtra("learnLanguage");
        interest = getIntent().getStringExtra("interest");
        word = getIntent().getStringExtra("word");
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
                    noResults.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    try {
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
                                                    model.getName().contains(word) &&
                                                    model.getLearningLanguage().contains(learnLanguage) &&
                                                    model.getAge() >= startAge && model.getAge() <= endAge &&
                                                    !model.getUsername().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())
                                            ) {

                                        itemList.add(model);
                                    }
                                    if (itemList.size() > 0) {
                                        noResults.setVisibility(View.GONE);
                                    } else {
                                        noResults.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        }
                        Collections.sort(itemList, new Comparator<UserModel>() {
                            @Override
                            public int compare(UserModel listData, UserModel t1) {
                                String ob1 = "" + listData.getStatus();
                                String ob2 = "" + t1.getStatus();

                                return ob2.compareTo(ob1);

                            }
                        });
                        progress.setVisibility(View.GONE);

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                } else {
                    progress.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);
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
//            Intent i = new Intent(SearchActivity.this, Filters.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
