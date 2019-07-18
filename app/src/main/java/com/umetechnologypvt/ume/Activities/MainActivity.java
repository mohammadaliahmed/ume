package com.umetechnologypvt.ume.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.umetechnologypvt.ume.Activities.Location.NearbyPeople;
import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Adapters.ChatListAdapter;
import com.umetechnologypvt.ume.FloatingChatButton.FloatingButton;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.ConnectivityManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static boolean active;
    FloatingActionButton newMessage;
    public static Activity myDialog;

    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    ArrayList<ChatListModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    //    TextView noMsgs;
    private TextView textCartItemCount;
    HashMap<String, Integer> unreadCount = new HashMap<>();
    private int chatCount;
    Toolbar toolBar;
    HashMap<Integer, ChatListModel> map = new HashMap<Integer, ChatListModel>();
    int count = 0;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
            }
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        active = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        active = false;
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.recyclerview);
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
//        noMsgs = findViewById(R.id.noMsgs);
        newMessage = findViewById(R.id.newMessage);
        myDialog = MainActivity.this;

        checkPermission();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setTitle("");

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startService(new Intent(getApplication(), FloatingButton.class));

                startActivity(new Intent(MainActivity.this, ContactSelectionScreen.class));
            }
        });


        adapter = new ChatListAdapter(this, itemList, new ChatListAdapter.ChatCallbacks() {
            @Override
            public void onChatDelete(String username) {
                showDeleteAlert(username);
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        if (ConnectivityManager.isNetworkConnected(this)) {
            getMessagesFromDB();

        } else {
            itemList = SharedPrefs.getChatList();
            Collections.sort(itemList, new Comparator<ChatListModel>() {
                @Override
                public int compare(ChatListModel listData, ChatListModel t1) {
                    Long ob1 = listData.getMessage().getTime();
                    Long ob2 = t1.getMessage().getTime();

                    return ob2.compareTo(ob1);

                }
            });
            adapter.setNewList(itemList);
//            adapter.notifyDataSetChanged();
        }

//        getLastmsgsFromDB();
        if (SharedPrefs.getUserModel().getUsername() != null) {
            if (SharedPrefs.getFcmKey() != null) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());
            }
        }

    }

    public void checkPermission() {
        System.out.println("CHECK PERMISSIONS:");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void showDeleteAlert(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this chat? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(username)
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Deleted");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        active = true;

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
        if (SharedPrefs.getUserModel().getUsername() != null) {
            mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
//                    getUnreadCount(dataSnapshot.getKey());

                        getMsgsFromDB(dataSnapshot.getKey(), count);
                        count++;
                    } else {
//                        noMsgs.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    itemList.clear();
                    map.clear();
                    getMessagesFromDB();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


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
                                SharedPrefs.setChatList(itemList);

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

                } else {
//                    noMsgs.setVisibility(View.VISIBLE);
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
        }
        if (id == R.id.action_nearby) {
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
