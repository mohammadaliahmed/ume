package com.umetechnologypvt.ume.Activities.Home;

import android.content.Context;
import android.net.Uri;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;


import com.google.android.exoplayer2.ui.PlayerView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
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

    TextView postByName, likesCount, time, addComment, lastComment, commentsCount;
    ImageView mainImage,showLike;
    CircleImageView commenterImg, postByPic,flag;
    ImageView muteIcon, comments, likeBtn, menu,forward;
    ViewPager slider;
    DotsIndicator dots_indicator;


    private final Playable.EventListener listener = new Playable.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            Log.d("onPlayerStateChanged", ":" +
                    format(Locale.getDefault(), "STATE: %d・PWR: %s", playbackState, playWhenReady));
        }
    };


    public VideoViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        slider = itemView.findViewById(R.id.slider);
        dots_indicator = itemView.findViewById(R.id.dots_indicator);
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

        //textView_description = itemView.findViewById(R.id.textView_description);
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
        if (helper != null) helper.play();
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

        String url = videoDictionary.getVideoUrl();
        //String url = videoDictionary.getUrl();

/*

        // video cache
        HttpProxyCacheServer proxy = App.getProxy(context);
        proxy.registerCacheListener(this, url);
        String proxyUrl = proxy.getProxyUrl(url);

*/

        Uri uri = Uri.parse(url);
        videoDictionary.setMediaUri(uri);
        this.mediaUri = uri;
    }

    public void onRecycled() {
        // do nothing
    }


}
