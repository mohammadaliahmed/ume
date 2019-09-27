package com.umetechnologypvt.ume.Activities.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.EditProfile;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.Home.UserPostsAdapter;
import com.umetechnologypvt.ume.Activities.Home.UserPostsFragment;
import com.umetechnologypvt.ume.Activities.Splash;
import com.umetechnologypvt.ume.Activities.UserFriends;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyPostFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;

    RecyclerView recyclerview;

    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> postIdsList = new ArrayList<>();
    UserPostsAdapter adapter;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_post_fragment, container, false);

        recyclerview = rootView.findViewById(R.id.recyclerview);
//
        setUI();
        getPostIdsFromDB();
        return rootView;
    }

    private void setUI() {

        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new UserPostsAdapter(context, itemList);
        adapter.setCallBacks(new UserPostsAdapter.HomePostsAdapterCallBacks() {
            @Override
            public void onPictureSelected(int position) {
                Constants.PICTURE_POSITION = position;
                Fragment fragment = new UserPostsFragment();
                loadFragment(fragment);
            }
        });

        recyclerview.setAdapter(adapter);

        if (Constants.LIKE_COMMENT == 1) {
            ArrayList<PostsModel> abc = SharedPrefs.getPosts();
            Collections.sort(abc, new Comparator<PostsModel>() {
                @Override
                public int compare(PostsModel listData, PostsModel t1) {
                    Long ob1 = listData.getTime();
                    Long ob2 = t1.getTime();
                    return ob2.compareTo(ob1);

                }
            });

            if (SharedPrefs.getPosts().size() > 0) {
                Constants.PICTURE_POSITION = 0;

                int count = 0;
                for (PostsModel m : abc) {
                    if (m.getId().equalsIgnoreCase(Constants.POST_ID)) {
                        Constants.PICTURE_POSITION = count;

                        break;
                    }
                    count++;
                }
                Fragment fragment = new UserPostsFragment();
                loadFragment(fragment);
                Constants.LIKE_COMMENT = 0;

            }
        } else {

        }


    }

    //
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //
//
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

    //
    private void getPostsFromDB(String id) {
        mDatabase.child("Posts").child("Posts").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PostsModel model = dataSnapshot.getValue(PostsModel.class);
                    if (model != null && model.getId() != null) {
                        itemList.add(model);
                        SharedPrefs.setPosts(itemList);
//                        postCount.setText("" + itemList.size());
                    }
                    if (itemList.size() > 0) {
                        Intent intent = new Intent("postsCount");
                        // You can also include some extra data.
                        intent.putExtra("postsCount", "" + itemList.size());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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


                } else {
                    mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(id).removeValue();
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
