package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.ConnectivityManager;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    RecyclerView recycler;
    HomePostsAdapter adapter;
    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> userLikedList = new ArrayList<>();


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_home_fragment, container, false);
        recycler = rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HomePostsAdapter(context, itemList, SharedPrefs.getLikesList() == null ? (new ArrayList<>()) : SharedPrefs.getLikesList(), new HomePostsAdapter.HomePostsAdapterCallBacks() {
            @Override
            public void takeUserToMyUserProfile(String userId) {
                Fragment fragment = new MyProfileFragment();
                loadFragment(fragment);
            }

            @Override
            public void takeUserToOtherUserProfile(String userId) {
                Constants.USER_ID = userId;
                Fragment fragment = new UserProfileFragment();
                loadFragment(fragment);
            }

            @Override
            public void onLikedPost(PostsModel model) {
                ArrayList list = SharedPrefs.getLikesList();
                if (list == null || list.size() < 1) {
                    list = new ArrayList();
                }
                list.add(model.getId());
                SharedPrefs.setLikesList(list);
                adapter.setLikeList(SharedPrefs.getLikesList());
                mDatabase.child("Posts").child("Posts").child(model.getId())
                        .child("likesCount").setValue(model.getLikesCount() );
                mDatabase.child("Posts").child("Likes").child(model.getId())
                        .child(SharedPrefs.getUserModel().getUsername()).setValue(SharedPrefs.getUserModel().getUsername());
            }

            @Override
            public void onUnlikedPost(PostsModel model) {
                ArrayList list = SharedPrefs.getLikesList();
                try {
                    list.remove(list.indexOf(model.getId()));
                    SharedPrefs.setLikesList(list);
                    adapter.setLikeList(SharedPrefs.getLikesList());
                    mDatabase.child("Posts").child("Posts").child(model.getId())
                            .child("likesCount").setValue(model.getLikesCount() );
                    mDatabase.child("Posts").child("Likes").child(model.getId())
                            .child(SharedPrefs.getUserModel().getUsername()).removeValue();
                } catch (Exception e) {

                }

            }

        });
        recycler.setAdapter(adapter);
        getDateFromDB();

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getDateFromDB() {
        mDatabase.child("Posts").child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PostsModel model = snapshot.getValue(PostsModel.class);
                        if (model != null) {
                            if (SharedPrefs.getUserModel().getConfirmFriends().contains(model.getPostBy())) {
                                itemList.add(model);
                            } else if (model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                itemList.add(model);
                            }
                        }
                    }
                    Collections.sort(itemList, new Comparator<PostsModel>() {
                        @Override
                        public int compare(PostsModel listData, PostsModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
