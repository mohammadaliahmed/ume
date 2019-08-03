package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.umetechnologypvt.ume.Activities.ContactSelectionScreen;
import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Location.NearbyPeople;
import com.umetechnologypvt.ume.Activities.NotificationsList;
import com.umetechnologypvt.ume.Activities.Profile;
import com.umetechnologypvt.ume.Activities.Search.Filters;
import com.umetechnologypvt.ume.Activities.Splash;
import com.umetechnologypvt.ume.Activities.UserManagement.Login;
import com.umetechnologypvt.ume.Adapters.ChatListAdapter;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.ConnectivityManager;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatListFragment extends Fragment {
    Context context;
    FloatingActionButton newMessage;
    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    ArrayList<ChatListModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    HashMap<String, Integer> unreadCount = new HashMap<>();
    private int chatCount;
    HashMap<String, ChatListModel> map = new HashMap<>();
    int count = 0;
    ImageView newChat;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_chat_list_fragment, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        newMessage = rootView.findViewById(R.id.newMessage);
        newChat = rootView.findViewById(R.id.newChat);

        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ContactSelectionScreen.class));
            }
        });
        adapter = new ChatListAdapter(context, itemList, new ChatListAdapter.ChatCallbacks() {
            @Override
            public void onChatDelete(String username) {
                showDeleteAlert(username);
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        if (ConnectivityManager.isNetworkConnected(context)) {
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
        }

        if (SharedPrefs.getUserModel().getUsername() != null) {
            if (SharedPrefs.getFcmKey() != null) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());
            }
        }
        return rootView;
    }


    private void showDeleteAlert(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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


    private void getMessagesFromDB() {

        if (SharedPrefs.getUserModel().getUsername() != null) {
            itemList.clear();
            map.clear();
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
                    count = 0;
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


                                map.put(key, new ChatListModel(key, model));
                                itemList.clear();
                                for (Map.Entry<String, ChatListModel> entry : map.entrySet()) {
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
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
