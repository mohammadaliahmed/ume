package com.umetechnologypvt.ume.Activities.Home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static java.lang.String.format;


/**
 * Created by anantshah on 15/07/17.
 */

@SuppressWarnings({"WeakerAccess", "unused"}) //
public class VideoViewHolder extends RecyclerView.ViewHolder implements ToroPlayer {
    public TextView textView_description;
    public ImageView imageView_sound;
    public PlayerView video_view;
    private static final String TAG = "IOSTUDIO:Video:Holder";

    private Context context;
    public ExoPlayerViewHelper helper;
    Uri mediaUri;

    TextView postByName, likesCount, time, addComment, lastComment, commentsCount, picCount, duration;
    ImageView mainImage, showLike;
    CircleImageView commenterImg, postByPic, flag;
    ImageView muteIcon, comments, likeBtn, menu, forward, download, repost;
    ViewPager slider;
    WormDotsIndicator dots_indicator;
    TextView age;
    RelativeLayout genderBg;
    ImageView gender;
    ImageView progress_image;
    ProgressBar videoProgress;


    private final Playable.EventListener listener = new Playable.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            if (playbackState == Player.STATE_BUFFERING) {
                Glide.with(context).load(imageUrl).into(progress_image);
                progress_image.setVisibility(View.VISIBLE);
                videoProgress.setVisibility(View.VISIBLE);
            } else {
                progress_image.setVisibility(View.INVISIBLE);
                videoProgress.setVisibility(View.GONE);
            }

            Log.d("onPlayerStateChanged", ":" +
                    format(Locale.getDefault(), "STATE: %d・PWR: %s", playbackState, playWhenReady));
        }
    };
    private String imageUrl;
//    private final Runnable updateUI= new Runnable()
//    {
//        public void run()
//        {
//            try
//            {
//                //update ur ui here
////                start.setText((mPlayer.getCurrentPosition()/mPlayer.getDuration())*100);
//                ‌ duration.setText(helper.getDuration()-mPlayer.getCurrentPosition());
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    };
//    private final Handler mHandler = new Handler();


    public VideoViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        duration = itemView.findViewById(R.id.duration);
        videoProgress = itemView.findViewById(R.id.videoProgress);
        slider = itemView.findViewById(R.id.slider);
        dots_indicator = itemView.findViewById(R.id.dots_indicator);
        picCount = itemView.findViewById(R.id.picCount);
        progress_image = itemView.findViewById(R.id.progress_image);
        video_view = itemView.findViewById(R.id.video_view);
        imageView_sound = itemView.findViewById(R.id.imageView_sound);
        postByName = itemView.findViewById(R.id.postByName);
        likesCount = itemView.findViewById(R.id.likesCount);
        time = itemView.findViewById(R.id.time);
        addComment = itemView.findViewById(R.id.addComment);
        lastComment = itemView.findViewById(R.id.lastComment);
        commentsCount = itemView.findViewById(R.id.commentsCount);
        mainImage = itemView.findViewById(R.id.mainImage);
        commenterImg = itemView.findViewById(R.id.commenterImg);
        postByPic = itemView.findViewById(R.id.postByPic);
        muteIcon = itemView.findViewById(R.id.muteIcon);
        likeBtn = itemView.findViewById(R.id.likeBtn);
        comments = itemView.findViewById(R.id.comments);
        menu = itemView.findViewById(R.id.menu);
        forward = itemView.findViewById(R.id.forward);
        showLike = itemView.findViewById(R.id.showLike);
        flag = itemView.findViewById(R.id.flag);
        download = itemView.findViewById(R.id.download);
        repost = itemView.findViewById(R.id.repost);
        genderBg = itemView.findViewById(R.id.genderBg);
        gender = itemView.findViewById(R.id.gender);
        age = itemView.findViewById(R.id.age);
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("pause"));
    }

    @NonNull
    @Override
    public PlayerView getPlayerView() {
        return video_view;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (mediaUri != null) {
//        if (mediaUri == null) throw new IllegalStateException("mediaUri is null.");
            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, mediaUri);
                helper.addEventListener(listener);
            }
            helper.initialize(container, playbackInfo);
        } else {
//            CommonUtils.showToast("Its null");
        }
    }

    @Override
    public void play() {
        if (helper != null) {
            helper.play();
            if (SharedPrefs.getMuted().equalsIgnoreCase("yes")) {
                helper.setVolume(0);
            } else {
                helper.setVolume(1);
            }
        }
    }

    @Override
    public void pause() {
        if (helper != null) helper.pause();
    }

    @Override
    public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override
    public void release() {
        if (helper != null) {
            helper.removeEventListener(listener);
            helper.release();
            helper = null;
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (helper != null) {
                helper.removeEventListener(listener);
                helper.release();
                helper = null;
            }

        }
    };


    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }

    @Override
    public String toString() {
        return "ExoPlayer{" + hashCode() + " " + getAdapterPosition() + "}";
    }

    public void bind(PostsModel videoDictionary, int position) {

//        String url = videoDictionary.getProxyUrl();
        String url = videoDictionary.getVideoUrl();
        imageUrl = videoDictionary.getVideoThumbnailUrl();
        //String url = videoDictionary.getUrl();


        // video cache
//        HttpProxyCacheServer proxy = ApplicationClass.getProxy(context);
//        proxy.registerCacheListener(this, url);
//        String proxyUrl = proxy.getProxyUrl(url);



        Uri uri = Uri.parse(url);
        videoDictionary.setMediaUri(uri);
        this.mediaUri = uri;
    }

    public void onRecycled() {
        // do nothing
    }



}
