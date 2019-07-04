package com.appsinventiv.ume.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appsinventiv.ume.Activities.Location.NearbyPeople;
import com.appsinventiv.ume.Activities.Search.Filters;
import com.appsinventiv.ume.Adapters.ChatListAdapter;
import com.appsinventiv.ume.Models.ChatListModel;
import com.appsinventiv.ume.Models.ChatModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newMessage;

    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    ArrayList<ChatListModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    TextView noMsgs;
    private TextView textCartItemCount;
    HashMap<String, Integer> unreadCount = new HashMap<>();
    private int chatCount;
    Toolbar toolBar;
    HashMap<Integer, ChatListModel> map = new HashMap<Integer, ChatListModel>();
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.recyclerview);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        noMsgs = findViewById(R.id.noMsgs);
        newMessage = findViewById(R.id.newMessage);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setTitle("");

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

//        getLastmsgsFromDB();
        if (SharedPrefs.getUserModel().getUsername() != null) {
            if (SharedPrefs.getFcmKey() != null) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());
            }
        }

    }

    private void getLastmsgsFromDB() {
        mDatabase.child("LastMessages").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel model = snapshot.getValue(ChatModel.class);

                        itemList.add(new ChatListModel(snapshot.getKey(), model));
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

    @Override
    protected void onResume() {
        super.onResume();
        if (textCartItemCount != null) {
            if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {
                if (Integer.parseInt(SharedPrefs.getNotificationCount()) > 0) {
                    textCartItemCount.setText(SharedPrefs.getNotificationCount());
                } else {
                    textCartItemCount.setVisibility(View.GONE);
                }

            } else {
                textCartItemCount.setVisibility(View.GONE);
            }
        }

    }


    private void getMessagesFromDB() {
        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
//                    getUnreadCount(dataSnapshot.getKey());

                    getMsgsFromDB(dataSnapshot.getKey(), count);
                    count++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    noMsgs.setVisibility(View.GONE);
//
//                    itemList.clear();
//                    chatCount = 0;
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        getUnreadCount(snapshot.getKey());
//                        getMsgsFromDB(snapshot.getKey());
//                        final String abc = snapshot.getKey();
//                    }
//                } else {
//                    noMsgs.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void getMsgsFromDB(String key, int count) {
        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(key).limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int counts = 0;
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                        if (snapshot1 != null) {
                            ChatModel model = snapshot1.getValue(ChatModel.class);
                            if (model != null) {


                                map.put(count, new ChatListModel(key, model));
                                itemList.clear();
                                for (Map.Entry<Integer, ChatListModel> entry : map.entrySet()) {
                                    itemList.add(entry.getValue());
                                }
                                Collections.sort(itemList, new Comparator<ChatListModel>() {
                                    @Override
                                    public int compare(ChatListModel listData, ChatListModel t1) {
                                        Long ob1 = listData.getMessage().getTime();
                                        Long ob2 = t1.getMessage().getTime();

                                        return ob2.compareTo(ob1);

                                    }
                                });

                                if (model.getMessageBy() != null)

                                {
                                    if (!model.getMessageBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                        if (model.getMessageStatus() != null) {
                                            if (!model.getMessageStatus().equalsIgnoreCase("read")) {
                                                counts = counts + 1;
                                                unreadCount.put(model.getMessageBy(), counts);
                                                adapter.setUnreadCount(unreadCount);
                                                chatCount = chatCount + 1;

                                            } else {
                                                unreadCount.put(model.getMessageBy(), 0);
                                                adapter.setUnreadCount(unreadCount);
                                            }
                                        }
                                    }


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


    private void getUnreadCount(String s) {

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int counts = 0;
                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                        ChatModel model = abc.getValue(ChatModel.class);
                        if (model != null) {
                            if (model.getMessageBy() != null) {
                                if (!model.getMessageBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                    if (model.getMessageStatus() != null) {
                                        if (!model.getMessageStatus().equalsIgnoreCase("read")) {
                                            counts = counts + 1;
                                            unreadCount.put(model.getMessageBy(), counts);
                                            adapter.setUnreadCount(unreadCount);
                                            chatCount = chatCount + 1;

                                        } else {
                                            unreadCount.put(model.getMessageBy(), 0);
                                            adapter.setUnreadCount(unreadCount);
                                        }
                                    }
                                }

                            }

                        }
                    }


                } else {
                    unreadCount.clear();
                    adapter.setUnreadCount(unreadCount);
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
        final MenuItem menuItem = menu.findItem(R.id.action_notifications);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {
            if (Integer.parseInt(SharedPrefs.getNotificationCount()) > 0) {
                textCartItemCount.setText(SharedPrefs.getNotificationCount());
            } else {
                textCartItemCount.setVisibility(View.GONE);
            }

        } else {
            textCartItemCount.setVisibility(View.GONE);
        }
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

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
            Intent i = new Intent(MainActivity.this, Filters.class);
            startActivity(i);
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, EditProfile.class);
            startActivity(i);
        }
        if (id == R.id.action_notifications) {
            Intent i = new Intent(MainActivity.this, NotificationsList.class);
            startActivity(i);
        }  if (id == R.id.action_nearby) {
            Intent i = new Intent(MainActivity.this, NearbyPeople.class);
            startActivity(i);
        }
        if (id == R.id.action_logout) {
            SharedPrefs.logout();
            Intent i = new Intent(MainActivity.this, Splash.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    class ValueComparator implements Comparator<Long> {

        HashMap<Integer, ChatListModel> base;

        public ValueComparator(HashMap<Integer, ChatListModel> base) {
            this.base = base;
        }

        @Override
        public int compare(Long o1, Long o2) {
            return ((ChatListModel) base.get(o1)).compareTo(base.get(o2));

        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
//        public int compare(Long a, Long b) {
//            return ((ChatListModel)base.get(a)).compareTo(base.get(b));
//        }

    }


}
