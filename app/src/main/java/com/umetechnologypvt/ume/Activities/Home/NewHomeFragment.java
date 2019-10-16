package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umetechnologypvt.ume.Activities.NotificationsList;
import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Adapters.ShareMessageFriendsAdapter;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.BottomDialogs.BottomDialog;
import com.umetechnologypvt.ume.Camera.AddStoryActivity;
import com.umetechnologypvt.ume.Camera.PhotoRedirectActivity;
import com.umetechnologypvt.ume.Interface.PostAdaptersCallbacks;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Stories.HomeStoriesAdapter;
import com.umetechnologypvt.ume.Stories.MyStoryActivity;
import com.umetechnologypvt.ume.Stories.StoryActivity;
import com.umetechnologypvt.ume.Stories.StoryModel;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.DownloadFile;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.PlayerSelector;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;

import static im.ene.toro.media.PlaybackInfo.INDEX_UNSET;
import static im.ene.toro.media.PlaybackInfo.TIME_UNSET;


public class NewHomeFragment extends Fragment {
    Context context;

    DatabaseReference mDatabase;
    Container recycler;
    VideoRecyclerAdapter adapter;
    ArrayList<PostsModel> itemList = new ArrayList<>();
    ArrayList<String> userLikedList = new ArrayList<>();
    ImageView notifications;

    TextView badgeCount;
    String filnamea;
    PlayerSelector selector = PlayerSelector.DEFAULT;  // backup current selector.

    ImageView menu;
    HashMap<String, UserModel> friendsMap = new HashMap<>();

    ArrayList<UserModel> friendsList = new ArrayList<>();
    ImageView camera;
    private List<Uri> mSelected = new ArrayList<>();

    CircleImageView userPic;

    RecyclerView friendsStories;

    HomeStoriesAdapter storiesAdapter;
    HorizontalScrollView hori;
//    RelativeLayout wholeLayout;

    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        LocalBroadcastManager.getInstance(context).registerReceiver(mSeenMessageReceiver,
                new IntentFilter("updateSeenList"));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_new_home_fragment, container, false);
        recycler = rootView.findViewById(R.id.my_fancy_videos);
        badgeCount = rootView.findViewById(R.id.badgeCount);
        hori = rootView.findViewById(R.id.hori);
        menu = rootView.findViewById(R.id.menu);
        notifications = rootView.findViewById(R.id.notifications);
//        wholeLayout = rootView.findViewById(R.id.wholeLayout);
        camera = rootView.findViewById(R.id.camera);
        userPic = rootView.findViewById(R.id.userPic);
        friendsStories = rootView.findViewById(R.id.friendsStories);


        hori.setVerticalScrollBarEnabled(false);
        hori.setHorizontalScrollBarEnabled(false);


        storiesAdapter = new HomeStoriesAdapter(context, SharedPrefs.getHomeStories(), new HomeStoriesAdapter.HomeStoriesAdapterCallbacks() {
            @Override
            public void onStoryClicked(StoryModel model, int position) {
                Constants.STORY_POSITION = position;
                Intent i = new Intent(context, StoryActivity.class);
                context.startActivity(i);

            }
        });
        friendsStories.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        friendsStories.setAdapter(storiesAdapter);
        storiesAdapter.notifyDataSetChanged();

        performSeenOperations();


        if (SharedPrefs.getUserModel().getPicUrl() != null) {
            Glide.with(context).load(SharedPrefs.getUserModel().getPicUrl()).into(userPic);
        }


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(context, StoryActivity.class);
//                context.startActivity(i);
                if (MainActivity.myArrayLists != null) {
                    if (MainActivity.myArrayLists.size() > 0) {
                        Intent i = new Intent(context, MyStoryActivity.class);
                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, AddStoryActivity.class);
                        context.startActivity(i);
                    }
                } else {
                    Intent i = new Intent(context, AddStoryActivity.class);
                    context.startActivity(i);
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment = new StoriesFragment();
//                loadFragment(fragment);
//                Intent i = new Intent(context, StoryActivity.class);
//                context.startActivity(i);
                Intent i = new Intent(context, AddStoryActivity.class);
                context.startActivity(i);
//                mSelected.clear();
//                Matisse.from(getActivity())
//                        .choose(MimeType.ofImage())
//                        .countable(true)
//                        .maxSelectable(1)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                        .thumbnailScale(0.85f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(23);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopup(v);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NotificationsList.class);
                startActivity(i);
            }
        });
        boolean isMuteByDefault =
                IOPref.getInstance().getBoolean(context, IOPref.PreferenceKey.isMute, true);


        adapter = new VideoRecyclerAdapter(context,
                SharedPrefs.getHomePosts() == null ? itemList : SharedPrefs.getHomePosts(),
                isMuteByDefault,
                System.currentTimeMillis(), new PostAdaptersCallbacks() {
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
//                adapter.setLikeList(SharedPrefs.getLikesList());
                mDatabase.child("Posts").child("Posts").child(model.getId())
                        .child("likesCount").setValue(model.getLikesCount());
                mDatabase.child("Posts").child("Likes").child(model.getId())
                        .child(SharedPrefs.getUserModel().getUsername()).setValue(SharedPrefs.getUserModel().getUsername());
                if (!model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                    getUserDetailsFromDB(model);
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onUnlikedPost(PostsModel model) {
                ArrayList list = SharedPrefs.getLikesList();
                try {
                    list.remove(list.indexOf(model.getId()));
                    SharedPrefs.setLikesList(list);
//                    adapter.setLikeList(SharedPrefs.getLikesList());
                    mDatabase.child("Posts").child("Posts").child(model.getId())
                            .child("likesCount").setValue(model.getLikesCount());
                    mDatabase.child("Posts").child("Likes").child(model.getId())
                            .child(SharedPrefs.getUserModel().getUsername()).removeValue();
                } catch (Exception e) {

                }
//                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFileDownload(String filename) {
                filnamea = filename;
            }

            @Override
            public void onDelete(PostsModel model, int position) {
                showDeleteAlert(model);
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
//                showBottomDialog(model);
                if (friendsList != null && friendsList.size() > 0) {
                    BottomDialog.showFriendsAdapter(context, model, mDatabase);
                } else {
                    CommonUtils.showToast("No Friends");
                }
            }

            @Override
            public void onRePost(PostsModel model) {
                showRepostAlert(model);
            }

            @Override
            public void onShowDownloadMenu(PostsModel model) {
                showDownloadMenuAlert(model);
            }
        }
        );
        if (SharedPrefs.getLikesList() != null && SharedPrefs.getLikesList().size() > 0) {
            adapter.setLikeList(SharedPrefs.getLikesList());
        }

        recycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setPlayerSelector(selector);
        adapter.setLikeList(SharedPrefs.getLikesList());
//        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();


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

        getFriendsFromDB();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == Activity.RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            new ImageEditor.Builder(getActivity(), CommonUtils.getRealPathFromURI(mSelected.get(0)))
                    .setStickerAssets("stickers")
                    .open();

        } else if (requestCode == ImageEditor.RC_IMAGE_EDITOR) {
            String img = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH);
            Intent mIntent = new Intent(context, PhotoRedirectActivity.class);
            mIntent.putExtra("PATH", img);
            mIntent.putExtra("THUMB", img);
            mIntent.putExtra("WHO", "Image");
            startActivity(mIntent);
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
                        model.setMediaUri(null);
                        try {
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
                                            mDatabase.child("PostsBy").child(SharedPrefs.getUserModel().getUsername()).child(key).setValue(id);
//                                        loadFragment(new NewHomeFragment());
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            CommonUtils.showToast(e.getMessage());
                        }

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

    private void getFriendsFromDB() {
        if (SharedPrefs.getUserModel().getConfirmFriends() != null) {
            friendsList.clear();
            friendsMap.clear();
            for (String abc : SharedPrefs.getUserModel().getConfirmFriends()) {
                getFriendsFromDB(abc);
            }
        }
    }

    private void getFriendsFromDB(String userId) {
        try {
            mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            friendsList.clear();
                            friendsMap.put(userModel.getUsername(), userModel);
                            for (Map.Entry<String, UserModel> entry : friendsMap.entrySet()) {
                                friendsList.add(entry.getValue());
                                SharedPrefs.setFriendsList(friendsList);
                            }

                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_home, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shareprofile:
                        return true;
                    case R.id.copyprofile:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void showMuteAlert(PostsModel model) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Mute " + model.getPostByName() + "?");
        builder1.setMessage("You can unmute them from their profile");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList list = SharedPrefs.getMutedList();
                        if (list == null) {
                            list = new ArrayList();
                        }
                        list.add(model.getPostBy());
                        SharedPrefs.setMutedList(list);
                        CommonUtils.showToast("Muted");

                        getDateFromDB();
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

                        getDateFromDB();
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

    private void showDeleteAlert(PostsModel model) {
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
                                CommonUtils.showToast("Deleted!");
                                getDateFromDB();
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
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        context.unregisterReceiver(onDownloadComplete);
        super.onDestroyView();

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
            intenta.setAction(Intent.ACTION_VIEW);

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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("0")) {
                badgeCount.setVisibility(View.VISIBLE);

                badgeCount.setText(SharedPrefs.getNotificationCount());
            } else {
                badgeCount.setVisibility(View.GONE);
            }

        }
    };

    private BroadcastReceiver mSeenMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            performSeenOperations();

        }
    };

    private void performSeenOperations() {
        HashMap<String, Boolean> newMap = new HashMap<>();
        for (ArrayList<StoryModel> list : MainActivity.arrayLists) {
            boolean seen = false;
            for (StoryModel item : list) {
                HashMap<String, String> map = SharedPrefs.getStorySeenMap();
                if (map != null) {
                    if (map.containsKey(item.getId())) {
                        seen = true;
                    } else {
                        seen = false;
                        break;
                    }
                }
            }
            if (!seen) {
                newMap.put(list.get(0).getStoryByUsername(), seen);
            } else {
                newMap.put(list.get(0).getStoryByUsername(), seen);
            }

        }
        storiesAdapter.setHashMap(newMap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDateFromDB();

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

    private void getDateFromDB() {
        mDatabase.child("Posts").child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PostsModel model = snapshot.getValue(PostsModel.class);
                        if (model != null) {
                            if (model.getId() != null) {
//                            if(model.getType().equalsIgnoreCase("video")) {
//                                HttpProxyCacheServer proxy = ApplicationClass.getProxy(context);
//                                proxy.registerCacheListener(new CacheListener() {
//                                    @Override
//                                    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
//
//                                    }
//                                }, model.getVideoUrl());
//                                String proxyUrl = proxy.getProxyUrl(model.getVideoUrl());
//                                model.setProxyUrl(proxyUrl);
//                            }
                                if (SharedPrefs.getUserModel().getConfirmFriends().contains(model.getPostBy())) {


                                    if (SharedPrefs.getMutedList() != null) {
                                        if (!SharedPrefs.getMutedList().contains(model.getPostBy())) {
                                            itemList.add(model);
                                        }
                                    } else {
                                        itemList.add(model);
                                    }

                                } else if (model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                                    itemList.add(model);
                                }

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

                    SharedPrefs.setHomePosts(itemList);
//                    wholeLayout.setVisibility(View.GONE);
                    adapter.setLikeList(SharedPrefs.getLikesList());

//                    adapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("0") && !SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {
            badgeCount.setVisibility(View.VISIBLE);

            badgeCount.setText(SharedPrefs.getNotificationCount());
        } else {
            badgeCount.setVisibility(View.GONE);
        }
        performSeenOperations();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(onDownloadComplete);

        } catch (Exception e) {

        }

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
