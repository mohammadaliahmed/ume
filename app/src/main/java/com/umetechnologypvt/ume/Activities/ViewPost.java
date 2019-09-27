package com.umetechnologypvt.ume.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.umetechnologypvt.ume.Activities.Comments.CommentsActivity;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.Home.MyProfileFragment;
import com.umetechnologypvt.ume.Activities.Home.PostLikesFragment;
import com.umetechnologypvt.ume.Activities.Home.UserProfileFragment;
import com.umetechnologypvt.ume.Activities.Home.VideoRecyclerAdapter;
import com.umetechnologypvt.ume.Activities.Home.VideoViewHolder;
import com.umetechnologypvt.ume.Activities.UserManagement.Login;
import com.umetechnologypvt.ume.Adapters.MainSliderAdapter;
import com.umetechnologypvt.ume.Adapters.ShareMessageFriendsAdapter;
import com.umetechnologypvt.ume.Interface.PostAdaptersCallbacks;
import com.umetechnologypvt.ume.Models.NotificationModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.umetechnologypvt.ume.Utils.DownloadFile;
import com.umetechnologypvt.ume.Utils.NotificationAsync;
import com.umetechnologypvt.ume.Utils.NotificationObserver;
import com.umetechnologypvt.ume.Utils.PrefManager;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.VolumeInfo;


public class ViewPost extends AppCompatActivity implements PostAdaptersCallbacks, NotificationObserver {
    public TextView textView_description;
    public ImageView imageView_sound;
    private static final String TAG = "IOSTUDIO:Video:Holder";

    TextView postByName, likesCount, time, addComment, lastComment, commentsCount, picCount;
    ImageView mainImage, showLike;
    CircleImageView commenterImg, postByPic, flag;
    ImageView muteIcon, comments, likeBtn, menu, forward;
    WormDotsIndicator dots_indicator;
    PostAdaptersCallbacks callbacks;
    PostsModel model;
    boolean finalLiked;
    DatabaseReference mDatabase;
    String postId, intentPostId;
    MainSliderAdapter mViewPagerAdapter;

    VideoView video_view;
    View videoView;
    private String filnamea;
    private List<String> imgsList = new ArrayList<>();
    ViewPager banner;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        onNewIntent(getIntent());
        intentPostId = getIntent().getStringExtra("postId");
        if (intentPostId != null) {
            getuserDataFromDB(intentPostId);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        callbacks = (PostAdaptersCallbacks) this;
        dots_indicator = findViewById(R.id.dots_indicator);
        picCount = findViewById(R.id.picCount);
        banner = findViewById(R.id.slider);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                picCount.setText((position + 1) + "/" + model.getMultiImages().size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerAdapter = new MainSliderAdapter(this, new ArrayList<>(), new MainSliderAdapter.ClicksCallback() {
            @Override
            public void onDoubleClick() {
                showLike.setVisibility(View.VISIBLE);
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(ViewPost.this, R.anim.fadein);
                showLike.startAnimation(myFadeInAnimation);
                likePost(finalLiked, model);
//                    showLike.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        showLike.setVisibility(View.GONE);
                    }
                }, 2500);
            }

            @Override
            public void onPicChanged(int position) {
            }
        });
        banner.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
        dots_indicator.setViewPager(banner);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        videoView = findViewById(R.id.videoView);
        video_view = findViewById(R.id.video_view);
        imageView_sound = findViewById(R.id.imageView_sound);
        postByName = findViewById(R.id.postByName);
        likesCount = findViewById(R.id.likesCount);
        time = findViewById(R.id.time);
        addComment = findViewById(R.id.addComment);
        lastComment = findViewById(R.id.lastComment);
        commentsCount = findViewById(R.id.commentsCount);
        mainImage = findViewById(R.id.mainImage);
        commenterImg = findViewById(R.id.commenterImg);
        postByPic = findViewById(R.id.postByPic);
        muteIcon = findViewById(R.id.muteIcon);
        likeBtn = findViewById(R.id.likeBtn);
        comments = findViewById(R.id.comments);
        menu = findViewById(R.id.menu);
        forward = findViewById(R.id.forward);
        showLike = findViewById(R.id.showLike);
        flag = findViewById(R.id.flag);


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog(model);

            }
        });
        likesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.takeUserToLikesScreen(model.getId());
            }
        });
        postByPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToProfile(model.getPostBy());
            }
        });
        postByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToProfile(model.getPostBy());

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ViewPost.this);
                dialog.setContentView(R.layout.pop_up_menu);


                TextView download = dialog.findViewById(R.id.download);
                TextView shareLink = dialog.findViewById(R.id.shareLink);
                TextView copyLink = dialog.findViewById(R.id.copyLink);
                TextView delete = dialog.findViewById(R.id.delete);


                TextView mute = dialog.findViewById(R.id.mute);

                if (model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                    mute.setVisibility(View.GONE);
                }
                if (SharedPrefs.getMutedList() != null) {
                    if (SharedPrefs.getMutedList().contains(model.getPostBy())) {
                        mute.setText("Un Mute");
                    }
                }

                mute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (SharedPrefs.getMutedList().contains(model.getPostBy())) {
                            callbacks.onUnMutePost(model);
                        } else {
                            callbacks.onMutePost(model);
                        }
                    }
                });
                if (model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callbacks.onDelete(model, 1);
                    }
                });
                copyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String url = "http://umetechnology.com/" + model.getId();
                        CommonUtils.showToast("copied to clipboard");
                    }
                });
                shareLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CommonUtils.shareUrl(ViewPost.this, "post", model.getId());
                    }
                });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (!model.getType().equalsIgnoreCase("multi")) {

                            String filen = (model.getType().equalsIgnoreCase("image") ? model.getPictureUrl().substring(model.getPictureUrl().length() - 10, model.getPictureUrl().length()) + ".jpg" :
                                    model.getVideoUrl().substring(model.getVideoUrl().length() - 10, model.getVideoUrl().length()) + ".mp4");
                            File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS) + "/" + filen
                            );
                            if (applictionFile != null && applictionFile.exists()) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
                                ViewPost.this.startActivity(intent);

                            } else {
                                DownloadFile.fromUrll((model.getType().equalsIgnoreCase("image")
                                        ? model.getPictureUrl() : model.getVideoUrl()), filen);
                                callbacks.onFileDownload(filen);
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPost.this, CommentsActivity.class);
                i.putExtra("postId", model.getId());
                i.putExtra("postBy", model.getPostBy());
                ViewPost.this.startActivity(i);
            }
        });
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPost.this, CommentsActivity.class);
                i.putExtra("postId", model.getId());
                i.putExtra("postBy", model.getPostBy());
                ViewPost.this.startActivity(i);
            }
        });


        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(finalLiked, model);
            }
        });

        mainImage.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(ViewPost.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    showLike.setVisibility(View.VISIBLE);
                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(ViewPost.this, R.anim.fadein);
                    showLike.startAnimation(myFadeInAnimation);
                    likePost(finalLiked, model);
//                    showLike.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            showLike.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(ViewPost.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    showLike.setVisibility(View.VISIBLE);
                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(ViewPost.this, R.anim.fadein);
                    showLike.startAnimation(myFadeInAnimation);
                    likePost(finalLiked, model);
//                    showLike.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            showLike.setVisibility(View.GONE);
                        }
                    }, 2500);
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        video_view.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(ViewPost.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    likePost(finalLiked, model);
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


    }

    public void likePost(boolean value, PostsModel model) {
        if (value) {
            likeBtn.setImageDrawable(ViewPost.this.getResources().getDrawable(R.drawable.ic_like_empty));
            model.setLikesCount(model.getLikesCount() - 1);
            callbacks.onUnlikedPost(model);
        } else {
            likeBtn.setImageDrawable(ViewPost.this.getResources().getDrawable(R.drawable.ic_like_fill));
            model.setLikesCount(model.getLikesCount() + 1);
            callbacks.onLikedPost(model);

        }

    }

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

    public void takeUserToProfile(String userId) {
        if (userId.equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
            callbacks.takeUserToMyUserProfile(userId);
        } else {
            callbacks.takeUserToOtherUserProfile(userId);
        }
    }


    @Override
    public void takeUserToMyUserProfile(String userId) {
        Intent i = new Intent(ViewPost.this, MainActivity.class);
        i.putExtra("value", 1);
        startActivity(i);
    }

    @Override
    public void takeUserToOtherUserProfile(String userId) {
        Constants.USER_ID = userId;
        Intent i = new Intent(ViewPost.this, MainActivity.class);
        i.putExtra("value", 2);
        startActivity(i);
    }

    @Override
    public void takeUserToLikesScreen(String postId) {
        Constants.POST_ID = postId;
        Intent i = new Intent(ViewPost.this, MainActivity.class);
        i.putExtra("value", 3);
        startActivity(i);
    }

    @Override
    public void onLikedPost(PostsModel model) {
        ArrayList list = SharedPrefs.getLikesList();
        if (list == null || list.size() < 1) {
            list = new ArrayList();
        }
        list.add(model.getId());
        SharedPrefs.setLikesList(list);
        mDatabase.child("Posts").child("Posts").child(model.getId())
                .child("likesCount").setValue(model.getLikesCount());
        mDatabase.child("Posts").child("Likes").child(model.getId())
                .child(SharedPrefs.getUserModel().getUsername()).setValue(SharedPrefs.getUserModel().getUsername());
        if (!model.getPostBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
            getUserDetailsFromDB(model);
        }
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
            NotificationAsync notificationAsync = new NotificationAsync(ViewPost.this);
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

    @Override
    public void onUnlikedPost(PostsModel model) {
        ArrayList list = SharedPrefs.getLikesList();
        try {
            list.remove(list.indexOf(model.getId()));
            SharedPrefs.setLikesList(list);
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
        showBottomDialog(model);
    }

    @Override
    public void onRePost(PostsModel model) {

    }

    @Override
    public void onShowDownloadMenu(PostsModel model) {
        showDownloadMenuAlert(model);
    }

    private void showDownloadMenuAlert(PostsModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPost.this);
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
                                    startActivity(intent);

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


        LayoutInflater inflater = (LayoutInflater) ViewPost.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.share_frnds_bottom_option, null);
        BottomSheetDialog dialog = new BottomSheetDialog(ViewPost.this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        EditText search = customView.findViewById(R.id.search);
        EditText messageText = customView.findViewById(R.id.messageText);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);
        CircleImageView image = customView.findViewById(R.id.image);
        Glide.with(ViewPost.this).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(image);

        ShareMessageFriendsAdapter adapter = new ShareMessageFriendsAdapter(ViewPost.this, SharedPrefs.getFriendsList(), new ShareMessageFriendsAdapter.ShareMessageFriendsAdapterCallbacks() {
            @Override
            public void onSend(UserModel model) {
                Intent i = new Intent(ViewPost.this, SingleChattingScreen.class);
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


        recyclerview.setLayoutManager(new LinearLayoutManager(ViewPost.this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        dialog.show();


    }


    protected void onNewIntent(Intent intent) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onNewIntent(intent);
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            postId = data.substring(data.lastIndexOf("/") + 1);
            getuserDataFromDB(postId);
        }
    }

    private void getuserDataFromDB(String postIdFromLink) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Constants.POST_ID = postIdFromLink;
        if (postIdFromLink != null) {
            mDatabase.child("Posts").child("Posts").child(postIdFromLink).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        model = dataSnapshot.getValue(PostsModel.class);
                        if (model != null) {
                            initUi(model);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void showMuteAlert(PostsModel model) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewPost.this);
        builder1.setMessage("Hide posts from this user?");
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewPost.this);
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

    private void showDeleteAlert(PostsModel model) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewPost.this);
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


    private void initUi(PostsModel model) {
        if (SharedPrefs.getLikesList() != null) {
            if (SharedPrefs.getLikesList().contains(model.getId())) {
                likeBtn.setImageDrawable(ViewPost.this.getResources().getDrawable(R.drawable.ic_like_fill));
            } else {
                likeBtn.setImageDrawable(ViewPost.this.getResources().getDrawable(R.drawable.ic_like_empty));

            }
        }

        this.setTitle(model.getPostByName());
        Glide.with(ViewPost.this).load(model.getUserPicUrl()).into(postByPic);
        Glide.with(ViewPost.this).load(model.getUserPicUrl()).into(commenterImg);
        if (model.getType().equalsIgnoreCase("Image")) {
            video_view.setVisibility(View.GONE);
            mainImage.setVisibility(View.VISIBLE);
            muteIcon.setVisibility(View.GONE);
            dots_indicator.setVisibility(View.GONE);

            Glide.with(ViewPost.this).load(model.getPictureUrl()).into(mainImage);

        } else if (model.getType().equalsIgnoreCase("video")) {
            videoView.setVisibility(View.VISIBLE);
            video_view.setVisibility(View.VISIBLE);
            mainImage.setVisibility(View.GONE);
            muteIcon.setVisibility(View.VISIBLE);
            dots_indicator.setVisibility(View.GONE);
            banner.setVisibility(View.GONE);
            video_view.setVideoPath(model.getVideoUrl());

            video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {

                    video_view.start();
                }
            });
            video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    video_view.start();
                }
            });


        } else if (model.getType().equalsIgnoreCase("multi")) {
            banner.setOffscreenPageLimit(0);

            video_view.setVisibility(View.GONE);
            mainImage.setVisibility(View.GONE);
            muteIcon.setVisibility(View.GONE);
            dots_indicator.setVisibility(View.VISIBLE);
            picCount.setVisibility(View.VISIBLE);
            banner.setVisibility(View.VISIBLE);

            imgsList = model.getMultiImages();
            mViewPagerAdapter.setPicturesList(model.getMultiImages());
            picCount.setText(1 + "/" + model.getMultiImages().size());


        } else {
            video_view.setVisibility(View.GONE);
            mainImage.setVisibility(View.GONE);
            banner.setVisibility(View.GONE);

            muteIcon.setVisibility(View.GONE);
            dots_indicator.setVisibility(View.GONE);
        }
        if (model.getComment().equalsIgnoreCase("")) {
            lastComment.setVisibility(View.GONE);
        } else {
            String sourceString = "<b>" + model.getCommentByName() + "</b> " + model.getComment();
            lastComment.setText(Html.fromHtml(sourceString));
            lastComment.setVisibility(View.VISIBLE);

        }
        postByName.setText(model.getPostByName());
        likesCount.setText(model.getLikesCount() + " likes");
        commentsCount.setText(model.getCommentsCount() + " comments");
        time.setText(CommonUtils.getFormattedDate(model.getTime()));

        if (model.getCountryCode() != null) {
            flag.setVisibility(View.VISIBLE);
            Glide.with(ViewPost.this).load(CountryUtils.getFlagDrawableResId(model.getCountryCode())).into(flag);
        } else {
            flag.setVisibility(View.GONE);
        }


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

    @Override
    public void onStop() {

        try {
            unregisterReceiver(onDownloadComplete);

        } catch (Exception e) {

        }

        super.onStop();
    }


}
