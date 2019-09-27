package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Adapters.ShareMessageFriendsAdapter;
import com.umetechnologypvt.ume.Interface.PostAdaptersCallbacks;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.DownloadFile;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.PlayerSelector;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;

import static im.ene.toro.media.PlaybackInfo.INDEX_UNSET;
import static im.ene.toro.media.PlaybackInfo.TIME_UNSET;


public class UserPostsFragment extends Fragment {

    int position;
    String filnamea;
    Context context;
    DatabaseReference mDatabase;
    Container recycler;
    VideoRecyclerAdapter adapter;
    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> userLikedList = new ArrayList<>();
    ImageView notifications;

    //    TextView badgeCount;
    PlayerSelector selector = PlayerSelector.DEFAULT;  // backup current selector.


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_user_posts_fragment, container, false);
        recycler = rootView.findViewById(R.id.my_fancy_videos);
//        recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        position = Constants.PICTURE_POSITION;
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Constants.SAVED_POST) {
            itemList = SharedPrefs.getSavedPosts();
        }else{
            itemList = SharedPrefs.getPosts();

        }
        Collections.sort(itemList, new Comparator<PostsModel>() {
            @Override
            public int compare(PostsModel listData, PostsModel t1) {
                Long ob1 = listData.getTime();
                Long ob2 = t1.getTime();
                return ob2.compareTo(ob1);

            }
        });
        boolean isMuteByDefault =
                IOPref.getInstance().getBoolean(context, IOPref.PreferenceKey.isMute, true);

        adapter = new VideoRecyclerAdapter(context,
                itemList,
                isMuteByDefault,
                System.currentTimeMillis(), new PostAdaptersCallbacks() {
            @Override
            public void takeUserToMyUserProfile(String userId) {
                Fragment fragment = new MyProfileFragment();
                loadFragment(fragment);
            }

            @Override
            public void takeUserToOtherUserProfile(String userId) {

            }

            @Override
            public void takeUserToLikesScreen(String postId) {
                Constants.POST_ID = postId;
                Fragment fragment = new PostLikesFragment();
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
                        .child("likesCount").setValue(model.getLikesCount());
                mDatabase.child("Posts").child("Likes").child(model.getId())
                        .child(SharedPrefs.getUserModel().getUsername()).setValue(SharedPrefs.getUserModel().getUsername());
                if (!model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                    getUserDetailsFromDB(model);
                }
            }

            @Override
            public void onUnlikedPost(PostsModel model) {
                ArrayList list = SharedPrefs.getLikesList();
                try {
                    list.remove(list.indexOf(model.getId()));
                    SharedPrefs.setLikesList(list);
                    adapter.setLikeList(SharedPrefs.getLikesList());
                    mDatabase.child("Posts").child("Posts").child(model.getId())
                            .child("likesCount").setValue(model.getLikesCount());
                    mDatabase.child("Posts").child("Likes").child(model.getId())
                            .child(SharedPrefs.getUserModel().getUsername()).removeValue();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFileDownload(String filename) {
                filnamea = filename;
            }

            @Override
            public void onDelete(PostsModel model, int position) {
                showDeleteAlert(model, position);
            }

            @Override
            public void onMutePost(PostsModel model) {
                showMuteAlert(model);
            }

            @Override
            public void onUnMutePost(PostsModel model) {

                showUnMuteAlert(model);
            }

            @Override
            public void onSharePostWithFriends(PostsModel model) {
                showBottomDialog(model);
            }

            @Override
            public void onRePost(PostsModel model) {
                showRepostAlert(model);
            }

            @Override
            public void onShowDownloadMenu(PostsModel model) {
                showDownloadMenuAlert(model);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.scrollToPosition(position);
        recycler.setPlayerSelector(selector);
        adapter.setLikeList(SharedPrefs.getLikesList());

        if (isMuteByDefault) {
            recycler.setPlayerInitializer(new Container.Initializer() {
                @NonNull
                @Override
                public PlaybackInfo initPlaybackInfo(int order) {
                    VolumeInfo volumeInfo = new VolumeInfo(true, 0.75f);
                    return new PlaybackInfo(INDEX_UNSET, TIME_UNSET, volumeInfo);
                }
            });
        }

    }


    private void showRepostAlert(PostsModel model) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Repost?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        String key = mDatabase.push().getKey();
                        model.setId(key);
                        model.setPostByName(SharedPrefs.getUserModel().getName());
//                        model.setPostBy(SharedPrefs.getUserModel().getUsername());
                        model.setUserPicUrl(SharedPrefs.getUserModel().getThumbnailUrl());
                        model.setCountryCode(SharedPrefs.getUserModel().getCountryNameCode());
                        model.setLikesCount(0);
                        model.setTime(System.currentTimeMillis());
                        model.setCommentsCount(0);
                        model.setComment("");
                        model.setUserAge(SharedPrefs.getUserModel().getAge());
                        model.setGender(SharedPrefs.getUserModel().getGender());
                        model.setCommentBy("");
                        model.setCommentByName("");
                        model.setCommentsCount(0);
                        mDatabase.child("Posts").child("Posts").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("repost", true);
                                map.put("originalPostBy", model.getPostBy());
                                map.put("postBy", SharedPrefs.getUserModel().getUsername());
                                mDatabase.child("Posts").child("Posts").child(model.getId()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CommonUtils.showToast("Reposted");
//                                        loadFragment(new NewHomeFragment());
                                    }
                                });
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
    private void showDownloadMenuAlert(PostsModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option");
        AlertDialog alert = builder.create();


        builder.setItems(new CharSequence[]
                        {"Save post", "Download", "Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                mDatabase.child("SavedPosts").child(SharedPrefs.getUserModel().getUsername()).child(model.getId()).setValue(model.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CommonUtils.showToast("Post Saved");
                                    }
                                });
                                break;
                            case 1:
                                String filen = (model.getType().equalsIgnoreCase("image") ? model.getPictureUrl().substring(model.getPictureUrl().length() - 10, model.getPictureUrl().length()) + ".jpg" :
                                        model.getVideoUrl().substring(model.getVideoUrl().length() - 10, model.getVideoUrl().length()) + ".mp4");
                                File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS) + "/" + filen
                                );
                                if (applictionFile != null && applictionFile.exists()) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
                                    context.startActivity(intent);

                                } else {
                                    DownloadFile.fromUrll((model.getType().equalsIgnoreCase("image")
                                            ? model.getPictureUrl() : model.getVideoUrl()), filen);
                                    filnamea = filen;
                                }
                                break;
                            case 2:
                                dialog.dismiss();

                                break;

                        }
                    }
                });
        alert = builder.create();
        alert.show();


    }
    @SuppressLint("WrongConstant")
    private void showBottomDialog(PostsModel postsModel) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.share_frnds_bottom_option, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        EditText messageText = customView.findViewById(R.id.messageText);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        CircleImageView image = customView.findViewById(R.id.image);
        Glide.with(context).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(image);

        ShareMessageFriendsAdapter adapter = new ShareMessageFriendsAdapter(context, SharedPrefs.getFriendsList(), new ShareMessageFriendsAdapter.ShareMessageFriendsAdapterCallbacks() {
            @Override
            public void onSend(UserModel model) {
                Intent i = new Intent(context, SingleChattingScreen.class);
                i.putExtra("userId", model.getUsername());
                Constants.FORWARD_POST = 1;
                String url = "";
                if (postsModel.getType().equalsIgnoreCase("image")) {
                    url = postsModel.getPictureUrl();
                } else if (postsModel.getType().equalsIgnoreCase("video")) {
                    url = postsModel.getVideoThumbnailUrl();
                } else if (postsModel.getType().equalsIgnoreCase("multi")) {
                    url = postsModel.getPictureUrl();
                }

                Constants.FORWARD_PIC_URL = url;
                Constants.POST_MESSAGE = messageText.getText().toString();
                Constants.POST_ID = postsModel.getId();
                startActivity(i);

                dialog.dismiss();
            }
        });


        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        dialog.show();


    }

    private void showUnMuteAlert(PostsModel model) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Un hide posts from this user?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList list = SharedPrefs.getMutedList();
                        if (list == null) {
                            list = new ArrayList();
                        }
                        list.remove(list.indexOf(model.getPostBy()));
                        SharedPrefs.setMutedList(list);
                        CommonUtils.showToast("Unmute");

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

    private void showMuteAlert(PostsModel model) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Hide posts from this user?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList list = SharedPrefs.getMutedList();
                        list.add(model.getPostBy());
                        SharedPrefs.setMutedList(list);
//                        get();
                        CommonUtils.showToast("Muted");
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

    private void showDeleteAlert(PostsModel model, int position) {
        itemList.remove(position);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Delete post?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabase.child("Posts").child("Posts").child(model.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPrefs.setPosts(itemList);
                                CommonUtils.showToast("Deleted!");
                                adapter.notifyDataSetChanged();
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

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            CommonUtils.showToast("Downloaded");
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/" + filnamea);
            Intent intenta = new Intent();
            intenta.setAction(android.content.Intent.ACTION_VIEW);

            intenta.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
            context.startActivity(intenta);
        }
    };

    private String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onDestroyView() {
        context.unregisterReceiver(onDownloadComplete);

        super.onDestroyView();

    }

    private void getUserDetailsFromDB(PostsModel model) {
        mDatabase.child("Users").child(model.getPostBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        sendLikeNotification(model, userModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendLikeNotification(PostsModel model, UserModel userModel) {
        if (userModel.getFcmKey() != null && !userModel.getFcmKey().equalsIgnoreCase("")) {
            NotificationAsync notificationAsync = new NotificationAsync(getActivity());
//                        String NotificationTitle = "New message in " + groupName;
            String NotificationTitle = SharedPrefs.getUserModel().getName() + " liked your post";
            String NotificationMessage = "Click to view ";

            notificationAsync.execute("ali", userModel.getFcmKey(), NotificationTitle, NotificationMessage, "likePost", "likePost",
                    model.getId(),
                    "" + SharedPrefs.getUserModel().getUsername().length(), SharedPrefs.getUserModel().getPicUrl()
            );
            String key = mDatabase.push().getKey();

            NotificationModel notificationModel = new NotificationModel(
                    key, userModel.getUsername(),
                    model.getId(),
                    SharedPrefs.getUserModel().getThumbnailUrl(),
                    SharedPrefs.getUserModel().getName() + " liked your post",
                    "likePost",
                    System.currentTimeMillis()
            );


            mDatabase.child("Notifications").child(userModel.getUsername()).child(key).setValue(notificationModel);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
