package com.umetechnologypvt.ume.Activities.Comments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Adapters.FriendsListAdapter;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentsActivity extends AppCompatActivity implements NotificationObserver {
    RecyclerView recycler;
    CommentsListAdapter adapter;
    ArrayList<CommentsModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    private String postId, postBy;
    TextView postComment;
    EditText comment;
    CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        recycler = findViewById(R.id.recycler);
        this.setTitle("Comments");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        postId = getIntent().getStringExtra("postId");
        postBy = getIntent().getStringExtra("postBy");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        postComment = findViewById(R.id.postComment);
        comment = findViewById(R.id.comment);
        image = findViewById(R.id.image);
        Glide.with(this).load(SharedPrefs.getUserModel().getPicUrl()).into(image);

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    postComment.setTextColor(getResources().getColor(R.color.colorPurple));
                } else {
                    postComment.setTextColor(getResources().getColor(R.color.colorGrey));

                }
            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().length() == 0) {
                    comment.setError("Empty comment");
                } else {
                    sendCommentToDB();
                }
            }
        });

        adapter = new CommentsListAdapter(this, itemList, new CommentsListAdapter.CommentsCallback() {
            @Override
            public void takeUserWhere(int value) {
                Intent i = new Intent(CommentsActivity.this, MainActivity.class);
                i.putExtra("value", value);
                startActivity(i);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler.setAdapter(adapter);

        getDataFromDB();
    }

    private void sendCommentToDB() {
        String key = mDatabase.push().getKey();
        CommentsModel model = new CommentsModel(
                key, comment.getText().toString(),
                SharedPrefs.getUserModel().getUsername(), SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
        );
        mDatabase.child("Posts").child("Comments").child(postId).child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                comment.setText("");
                CommonUtils.showToast("Comment added");
                HashMap<String, Object> map = new HashMap<>();
                map.put("comment", model.getCommentText());
                map.put("commentBy", model.getCommentBy());
                map.put("commentByName", model.getCommentByName());
                map.put("commentByPicUrl", model.getCommentByPicUrl());
                map.put("commentsCount", itemList.size());
                mDatabase.child("Posts").child("Posts").child(postId).updateChildren(map);

                if (!postBy.equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                    getUserDetailsFromDB();
                }

            }
        });
    }

    private void getUserDetailsFromDB() {
        mDatabase.child("Users").child(postBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        sendLikeNotification(userModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendLikeNotification(UserModel userModel) {
        if (userModel.getFcmKey() != null && !userModel.getFcmKey().equalsIgnoreCase("")) {
            NotificationAsync notificationAsync = new NotificationAsync(CommentsActivity.this);
//                        String NotificationTitle = "New message in " + groupName;
            String NotificationTitle = SharedPrefs.getUserModel().getName() + " commented on your post";
            String NotificationMessage = "Click to view ";

            notificationAsync.execute("ali", userModel.getFcmKey(), NotificationTitle, NotificationMessage, "commentPost", "commentPost",
                    postId,
                    "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
            );
            String key = mDatabase.push().getKey();

            NotificationModel notificationModel = new NotificationModel(
                    key,
                    userModel.getUsername(),
                    postId,
                    SharedPrefs.getUserModel().getThumbnailUrl(),
                    SharedPrefs.getUserModel().getName() + " commented on your post",
                    "commentPost",
                    System.currentTimeMillis()
            );


            mDatabase.child("Notifications").child(userModel.getUsername()).child(key).setValue(notificationModel);
        }
    }

    private void getDataFromDB() {
        mDatabase.child("Posts").child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CommentsModel m = snapshot.getValue(CommentsModel.class);
                        if (m != null) {
                            itemList.add(m);
                        }
                    }
                    Collections.sort(itemList, (listData, t1) -> {
                        Long ob1 = listData.getTime();
                        Long ob2 = t1.getTime();
                        return ob2.compareTo(ob1);

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
