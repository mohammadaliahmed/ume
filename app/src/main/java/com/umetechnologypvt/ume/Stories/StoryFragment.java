package com.umetechnologypvt.ume.Stories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.Home.UserProfileFragment;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.KeyboardUtils;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class StoryFragment extends Fragment implements StoriesProgressView.StoriesListener {
    Context context;

    DatabaseReference mDatabase;

    int position;

    private int PROGRESS_COUNT = 0;
    private StoriesProgressView storiesProgressView;
    private ProgressBar mProgressBar;
    private LinearLayout mVideoViewLayout;
    private int counter = 0;
    private ArrayList<StoryModel> mStoriesList = new ArrayList<>();

    private ArrayList<View> mediaPlayerArrayList = new ArrayList<>();

    long pressTime = 0L;
    long limit = 500L;
    MediaPlayer mmmmedia;

    CircleImageView storyByPic, userPic;
    TextView storyByName, time;
    EditText message;

    ImageView send;


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    if (mmmmedia != null) {
                        try {
                            mmmmedia.pause();

                        } catch (Exception e) {

                        }
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    if (mmmmedia != null) {
                        try {
                            mmmmedia.start();

                        } catch (Exception e) {

                        }
                    }
                    return limit < now - pressTime;

            }
            return false;
        }
    };
    private VideoView video1;

    @SuppressLint("ValidFragment")
    public StoryFragment() {

    }

    @SuppressLint("ValidFragment")
    public StoryFragment(Context context, int position, int count) {
        this.context = context;
        this.position = position;
        this.PROGRESS_COUNT = count;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_story, null);
        userPic = rootView.findViewById(R.id.userPic);
        storyByPic = rootView.findViewById(R.id.storyByPic);
        storyByName = rootView.findViewById(R.id.storyByName);
        time = rootView.findViewById(R.id.time);
        message = rootView.findViewById(R.id.message);
        send = rootView.findViewById(R.id.send);

        storyByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.USER_ID = MainActivity.arrayLists.get(position).get(0).getStoryByUsername();
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("value", 2);
                context.startActivity(i);
            }
        });

        storyByPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.USER_ID = MainActivity.arrayLists.get(position).get(0).getStoryByUsername();
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("value", 2);
                context.startActivity(i);
            }
        });

        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    storiesProgressView.pause();
                } else {
                    storiesProgressView.resume();
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().length() == 0) {
                    message.setError("Enter message");
                } else {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    sendStoryReply(message.getText().toString(), mStoriesList.get(counter));
                }
            }
        });


        mProgressBar = rootView.findViewById(R.id.progressBar);
        mVideoViewLayout = rootView.findViewById(R.id.videoView);
        storiesProgressView = (StoriesProgressView) rootView.findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);


        if (SharedPrefs.getUserModel().getPicUrl() != null) {
            Glide.with(context).load(SharedPrefs.getUserModel().getThumbnailUrl()).into(userPic);
        }


        prepareStoriesList();
        storiesProgressView.setStoriesListener(this);
        Collections.sort(mStoriesList, new Comparator<StoryModel>() {
            @Override
            public int compare(StoryModel listData, StoryModel t1) {
                Long ob1 = listData.getTime();
                Long ob2 = t1.getTime();
                return ob1.compareTo(ob2);

            }
        });

        for (int i = 0; i < mStoriesList.size(); i++) {
            if (mStoriesList.get(i).getStoryType().contains("video")) {
//                String abc = CommonUtils.getNameFromUrl(mStoriesList.get(i).getVideoUrl());
//                String uploadFilepathtemp = Environment.getExternalStorageDirectory()
//                        + "/UME/" + abc
//                        + ".mp4";
//                File carmeraFile = new File(uploadFilepathtemp);
//                if (carmeraFile.exists()) {
//                    mStoriesList.get(i).setVideoUrl(uploadFilepathtemp);
//                } else {
//                }
                mediaPlayerArrayList.add(getVideoView(i));
            } else if (mStoriesList.get(i).getStoryType().contains("image")) {
                mediaPlayerArrayList.add(getImageView(i));
            }
        }

        setStoryView(counter);

        // bind reverse view
        View reverse = rootView.findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = rootView.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
        return rootView;
    }

    private void sendStoryReply(String s, StoryModel model) {
//        CommonUtils.showToast(s);
        storiesProgressView.resume();
        String key = mDatabase.push().getKey();
        ChatModel storyChatModel = new ChatModel(
                key,
                SharedPrefs.getUserModel().getUsername(),
                model.getStoryByUsername(),
                model.getStoryByName(),
                model.getStoryByPicUrl(),
                s,
                Constants.MESSAGE_TYPE_STORY,
                System.currentTimeMillis(),
                model.getTime(),
                model.getId(),
                model.getImageUrl(),
                "sent",
                model.getCountryCode());

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                .child(model.getStoryByUsername()).child(key)
                .setValue(storyChatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Reply Sent");
                message.setText("");
                storyChatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                storyChatModel.setName(SharedPrefs.getUserModel().getName());
                storyChatModel.setPicUrl(SharedPrefs.getUserModel().getThumbnailUrl());
                mDatabase.child("Chats").child(model.getStoryByUsername()).child(SharedPrefs.getUserModel().getUsername()).child(storyChatModel.getId())
                        .setValue(storyChatModel);
//                sendNotification(forwardChatModel.getMessageType(), forwardChatModel.getId());
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        storiesProgressView.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        storiesProgressView.pause();
    }

    private void setStoryView(final int counter) {
        final View view = (View) mediaPlayerArrayList.get(counter);

        mVideoViewLayout.addView(view);
        time.setText(CommonUtils.getFormattedDate(MainActivity.arrayLists.get(position).get(counter).getTime()));


        HashMap<String, String> smap = SharedPrefs.getStorySeenMap();
        if (smap != null && smap.size() > 0) {
            if (!smap.containsKey(mStoriesList.get(counter).getId())) {
                StoryViewsModel viewsModel = new StoryViewsModel(
                        mStoriesList.get(counter).getId(),
                        SharedPrefs.getUserModel().getName(),
                        SharedPrefs.getUserModel().getThumbnailUrl(),
                        System.currentTimeMillis()

                );
                mDatabase.child("StoryViews")
                        .child(MainActivity.arrayLists.get(position).get(counter).getStoryByUsername()).
                        child(MainActivity.arrayLists.get(position).get(counter).getId()).child(SharedPrefs.getUserModel().getUsername())
                        .setValue(viewsModel);


            }
        }


        if (view instanceof VideoView) {


            final VideoView video = (VideoView) view;
            storiesProgressView.pause();
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mmmmedia = mediaPlayer;
                    mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                            Log.d("mediaStatus", "onInfo: =============>>>>>>>>>>>>>>>>>>>" + i);
                            switch (i) {
                                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                    mProgressBar.setVisibility(View.GONE);
                                    storiesProgressView.startanim(counter);

                                    storiesProgressView.resume();
                                    return true;
                                }
                                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    storiesProgressView.pause();
                                    return true;
                                }
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    storiesProgressView.pause();
                                    return true;

                                }
                                case MediaPlayer.MEDIA_ERROR_TIMED_OUT: {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    storiesProgressView.pause();
                                    return true;
                                }

                                case MediaPlayer.MEDIA_INFO_AUDIO_NOT_PLAYING: {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    storiesProgressView.pause();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    video.start();
                    mProgressBar.setVisibility(View.GONE);
                    storiesProgressView.setStoryDuration(mediaPlayer.getDuration());
                    storiesProgressView.startanim(counter);

                    storiesProgressView.startStories(counter);
                }
            });
        } else if (view instanceof ImageView) {

            final ImageView image = (ImageView) view;
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mProgressBar.setVisibility(View.GONE);
            Glide.with(context).load(mStoriesList.get(counter).getImageUrl()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Toast.makeText(context, "Failed to load media...", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    mProgressBar.setVisibility(View.GONE);
                    storiesProgressView.setStoryDuration(5000);
                    storiesProgressView.startStories(counter);
                    storiesProgressView.startanim(counter);

                    return false;
                }
            }).into(image);
        }
        HashMap<String, String> map = SharedPrefs.getStorySeenMap();
        if (map != null && map.size() > 0) {
            map.put(mStoriesList.get(counter).getId(), mStoriesList.get(counter).getId());
            SharedPrefs.setStorySeenMap(map);
        } else {
            map = new HashMap<>();
            map.put(mStoriesList.get(counter).getId(), mStoriesList.get(counter).getId());
            SharedPrefs.setStorySeenMap(map);
        }


    }

    private void prepareStoriesList() {
        mStoriesList = MainActivity.arrayLists.get(position);
        Glide.with(context).load(MainActivity.arrayLists.get(position).get(MainActivity.arrayLists.get(position).size() - 1).getStoryByPicUrl()).into(storyByPic);
        storyByName.setText(MainActivity.arrayLists.get(position).get(0).getStoryByName());
        long dur = System.currentTimeMillis() - (MainActivity.arrayLists.get(position).get(counter).getTime());


    }

    private VideoView getVideoView(int position) {
        VideoView video = new VideoView(context);
        video.setVideoPath(mStoriesList.get(position).getVideoUrl());
//        video.setVideoPath(mStoriesList.get(position).getProxyUrl());

        video.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return video;
    }

    private ImageView getImageView(int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }

    @Override
    public void onNext() {
        storiesProgressView.destroy();
        mVideoViewLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        setStoryView(++counter);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        storiesProgressView.destroy();
        mVideoViewLayout.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        setStoryView(--counter);
    }

    @Override
    public void onComplete() {


        sendMessage();

//        StoryActivity.activity.finish();
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("storyPosition");
        // You can also include some extra data.
        intent.putExtra("storyPosition", (position + 1));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    @Override
    public void onDestroyView() {
        storiesProgressView.destroy();
        super.onDestroyView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
