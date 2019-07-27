package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.UserFriends;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    TextView editProfile;
    TextView about, name, friendsCount, postCount;
    CircleImageView picture;
    TextView toolbarName;
    RecyclerView recyclerview;

    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> postIdsList = new ArrayList<>();
    UserPostsAdapter adapter;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_my_profile_fragment, container, false);
        editProfile = rootView.findViewById(R.id.editProfile);
        about = rootView.findViewById(R.id.about);
        name = rootView.findViewById(R.id.name);
        friendsCount = rootView.findViewById(R.id.friendsCount);
        postCount = rootView.findViewById(R.id.postCount);
        picture = rootView.findViewById(R.id.picture);
        toolbarName = rootView.findViewById(R.id.toolbarName);
        recyclerview = rootView.findViewById(R.id.recyclerview);

        setUI();

        return rootView;
    }

    private void setUI() {

        about.setText(SharedPrefs.getUserModel().getAbout());
        toolbarName.setText(SharedPrefs.getUserModel().getName());
        name.setText(SharedPrefs.getUserModel().getName());
        friendsCount.setText(""+SharedPrefs.getUserModel().getConfirmFriends().size());
        try {
            Glide.with(context).load(SharedPrefs.getUserModel().getPicUrl()).into(picture);
        } catch (Exception e) {

        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfile.class));
            }
        });

        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new UserPostsAdapter(context, itemList);
        adapter.setCallBacks(new UserPostsAdapter.HomePostsAdapterCallBacks() {
            @Override
            public void onPictureSelected(int position) {
                Constants.PICTURE_POSITION=position;
                Fragment fragment = new UserPostsFragment();
                loadFragment(fragment);
            }
        });
        friendsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserFriends.class);
                i.putExtra("userId", SharedPrefs.getUserModel().getUsername());
                context.startActivity(i);
            }
        });
        recyclerview.setAdapter(adapter);


        getPostIdsFromDB();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void getPostIdsFromDB() {
        mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    postIdsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        postIdsList.add(key);
                    }
                    itemList.clear();
                    if (postIdsList.size() > 0) {
                        for (String id : postIdsList) {
                            getPostsFromDB(id);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostsFromDB(String id) {
        mDatabase.child("Posts").child("Posts").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PostsModel model = dataSnapshot.getValue(PostsModel.class);
                    if (model != null && model.getId() != null) {
                        itemList.add(model);
                        SharedPrefs.setPosts(itemList);
                        postCount.setText(""+itemList.size());

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
