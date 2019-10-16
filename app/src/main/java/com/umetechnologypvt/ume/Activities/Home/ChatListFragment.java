package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.umetechnologypvt.ume.Adapters.ChatListAdapter;
import com.umetechnologypvt.ume.Models.ChatListModel;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.ConnectivityManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatListFragment extends Fragment {
    Context context;
    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    ArrayList<ChatListModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    HashMap<String, Integer> unreadCount = new HashMap<>();
    private int chatCount;
    HashMap<String, ChatListModel> map = new HashMap<>();
    int count = 0;
    ImageView newChat;
    SearchView searchView;
    TextView heading;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_chat_list_fragment, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        searchView = rootView.findViewById(R.id.searchView);
        heading = rootView.findViewById(R.id.heading);
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


        itemList = SharedPrefs.getChatList();
        if(itemList!=null) {
            Collections.sort(itemList, new Comparator<ChatListModel>() {
                @Override
                public int compare(ChatListModel listData, ChatListModel t1) {
                    Long ob1 = listData.getMessage().getTime();
                    Long ob2 = t1.getMessage().getTime();

                    return ob2.compareTo(ob1);

                }
            });
        }else{
            itemList=new ArrayList<>();
        }
        adapter.setNewList(itemList);


        if (SharedPrefs.getUserModel().getUsername() != null) {
            if (SharedPrefs.getFcmKey() != null) {
                mDatabase.child("Users").child(SharedPrefs.getUserModel().getUsername()).child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());
            }
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                adapter.filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                heading.setVisibility(View.VISIBLE);
                return false;
            }

        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heading.setVisibility(View.GONE);
            }
        });


        getMessagesFromDB();
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
                                                SharedPrefs.setChatCount("0");
                                                int cou = 0;
                                                for (Map.Entry<String, Integer> entry : unreadCount.entrySet()) {
                                                    cou = cou + entry.getValue();
                                                }
                                                SharedPrefs.setChatCount("" + (cou - 1));


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
                    sendMessage();

                    adapter.updateList(itemList);
                    adapter.notifyDataSetChanged();
                    SharedPrefs.setChatList(itemList);

                } else {
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("chatCount");
        // You can also include some extra data.
        intent.putExtra("message", "asda");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
