package com.appsinventiv.ume.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.appsinventiv.ume.Adapters.NotificationsListAdapter;
import com.appsinventiv.ume.Models.NotificationModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationsList extends AppCompatActivity {
    RecyclerView recyclerview;
    NotificationsListAdapter adapter;
    private ArrayList<NotificationModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    TextView notiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Notifications");
        recyclerview = findViewById(R.id.recyclerview);
        notiText = findViewById(R.id.notiText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new NotificationsListAdapter(this, itemList, new NotificationsListAdapter.Callbacks() {
            @Override
            public void onDelete(String id) {
                showAlert(id);
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        getDataFromDB();

    }

    private void getDataFromDB() {
        mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    notiText.setVisibility(View.GONE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NotificationModel model = snapshot.getValue(NotificationModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    notiText.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAlert(String notiId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NotificationsList.this);
        builder1.setMessage("Delete notification?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabase.child("Notifications").child(SharedPrefs.getUserModel().getUsername()).child(notiId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Removed");
                            }
                        });
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        AlertDialog.Builder builder;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
