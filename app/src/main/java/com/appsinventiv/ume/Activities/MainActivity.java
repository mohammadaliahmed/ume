package com.appsinventiv.ume.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.Search.SearchActivity;
import com.appsinventiv.ume.Adapters.ChatListAdapter;
import com.appsinventiv.ume.Models.ChatListModel;
import com.appsinventiv.ume.Models.ChatModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CountryUtils;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newMessage;

    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    ArrayList<ChatListModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    TextView noMsgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.recyclerview);
        noMsgs = findViewById(R.id.noMsgs);
        newMessage = findViewById(R.id.newMessage);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setTitle("U ME");

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactSelectionScreen.class));
            }
        });


        adapter = new ChatListAdapter(this, itemList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);


        getMessagesFromDB();
        if(SharedPrefs.getUserModel().getUsername()!=null){
            if(SharedPrefs.getFcmKey()!=null){
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(SharedPrefs.getFcmKey());
            }
        }

    }

    private void getMessagesFromDB() {
        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    noMsgs.setVisibility(View.GONE);

                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String abc = snapshot.getKey();
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(snapshot.getKey()).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {

                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        if (snapshot1 != null) {
                                            ChatModel model = snapshot1.getValue(ChatModel.class);
                                            if (model != null) {
                                                itemList.add(new ChatListModel(abc, model));
                                            }
                                        }
                                    }

                                    Collections.sort(itemList, new Comparator<ChatListModel>() {
                                        @Override
                                        public int compare(ChatListModel listData, ChatListModel t1) {
                                            Long ob1 = listData.getMessage().getTime();
                                            Long ob2 = t1.getMessage().getTime();

                                            return ob2.compareTo(ob1);

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
                }else{
                    noMsgs.setVisibility(View.VISIBLE);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, EditProfile.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
