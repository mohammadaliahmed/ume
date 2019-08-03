package com.umetechnologypvt.ume.Activities.Home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Activities.Comments.CommentsActivity;
import com.umetechnologypvt.ume.Activities.ForwardContactSelectionScreen;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.DownloadFile;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.umetechnologypvt.ume.Camera.WhatsappCameraActivity.activity;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.ViewHolder> {
    Context context;
    ArrayList<PostsModel> itemList;
    HomePostsAdapterCallBacks callBacks;
    AudioManager mAudioManager;
    ArrayList<String> likeList;

    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
        notifyDataSetChanged();
    }

    public HomePostsAdapter(Context context, ArrayList<PostsModel> itemList, ArrayList<String> likeList, HomePostsAdapterCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;
        this.likeList = likeList;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_post_item_layout, parent, false);
        HomePostsAdapter.ViewHolder viewHolder = new HomePostsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostsModel model = itemList.get(position);

        boolean liked = false;
        if (likeList.size() > 0) {
            if (likeList.contains(model.getId())) {
                liked = true;
            }
        }
        if (liked) {
            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_fill));
        } else {
            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_empty));

        }

        Glide.with(context).load(model.getUserPicUrl()).into(holder.postByPic);
        Glide.with(context).load(model.getUserPicUrl()).into(holder.commenterImg);
        if (model.getType().equalsIgnoreCase("Image")) {
//            holder.mainVideo.setVisibility(View.GONE);
            holder.mainImage.setVisibility(View.VISIBLE);
            holder.muteIcon.setVisibility(View.GONE);
            Glide.with(context).load(model.getPictureUrl()).into(holder.mainImage);

        } else if (model.getType().equalsIgnoreCase("video")) {
//            holder.mainVideo.setVisibility(View.VISIBLE);
            holder.mainImage.setVisibility(View.GONE);
//            holder.mainVideo.setVideoURI(Uri.parse(model.getVideoUrl()));
            holder.muteIcon.setVisibility(View.VISIBLE);


        } else {
//            holder.mainVideo.setVisibility(View.GONE);
            holder.mainImage.setVisibility(View.GONE);
        }
        if (model.getComment().equalsIgnoreCase("")) {
            holder.lastComment.setVisibility(View.GONE);
        } else {
            String sourceString = "<b>" + model.getCommentByName() + "</b> " + model.getComment();
            holder.lastComment.setText(Html.fromHtml(sourceString));
            holder.lastComment.setVisibility(View.VISIBLE);

        }
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        holder.postByName.setText(model.getPostByName());
        holder.likesCount.setText(model.getLikesCount() + " likes");
        holder.commentsCount.setText(model.getCommentsCount() + " comments");
        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));

        holder.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://umetechnology.com/" + model.getId());
                context.startActivity(Intent.createChooser(shareIntent, "Share post via.."));

            }
        });
        holder.likesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.takeUserToLikesScreen(model.getId());
            }
        });
        holder.postByPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToProfile(model.getPostBy());
            }
        });
        holder.postByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToProfile(model.getPostBy());

            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pop_up_menu);


                TextView download = dialog.findViewById(R.id.download);
                TextView shareLink = dialog.findViewById(R.id.shareLink);
                TextView copyLink = dialog.findViewById(R.id.copyLink);


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
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);

                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://umetechnology.com/" + model.getId());
                        context.startActivity(Intent.createChooser(shareIntent, "Share post via.."));
                    }
                });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
                            callBacks.onFileDownload(filen);

//                            Intent intent = new Intent();
//                            intent.setAction(android.content.Intent.ACTION_VIEW);
//
//                            intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
//                            context.startActivity(intent);
                        }
                    }
                });


                dialog.show();
            }
        });

        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CommentsActivity.class);
                i.putExtra("postId", model.getId());
                i.putExtra("postBy", model.getPostBy());
                context.startActivity(i);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CommentsActivity.class);
                i.putExtra("postId", model.getId());
                i.putExtra("postBy", model.getPostBy());
                context.startActivity(i);
            }
        });

        boolean finalLiked = liked;
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(finalLiked, holder, model);
            }
        });

        holder.mainImage.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
//                    Log.d("TEST", "onDoubleTap");
//                    if (!finalLiked) {
                    likePost(finalLiked, holder, model);
//                    }
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


    public void likePost(boolean value, ViewHolder holder, PostsModel model) {
        if (value) {
            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_empty));
            model.setLikesCount(model.getLikesCount() - 1);
            callBacks.onUnlikedPost(model);
        } else {
            holder.likeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_fill));
            model.setLikesCount(model.getLikesCount() + 1);
            callBacks.onLikedPost(model);

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
            callBacks.takeUserToMyUserProfile(userId);
        } else {
            callBacks.takeUserToOtherUserProfile(userId);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView postByName, likesCount, time, addComment, lastComment, commentsCount;
        ImageView mainImage;
        CircleImageView commenterImg, postByPic;
        //        VideoView mainVideo;
        ImageView muteIcon, comments, likeBtn, menu, forward;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postByName = itemView.findViewById(R.id.postByName);
            likesCount = itemView.findViewById(R.id.likesCount);
            time = itemView.findViewById(R.id.time);
            addComment = itemView.findViewById(R.id.addComment);
            lastComment = itemView.findViewById(R.id.lastComment);
            commentsCount = itemView.findViewById(R.id.commentsCount);
            mainImage = itemView.findViewById(R.id.mainImage);
//            mainVideo = itemView.findViewById(R.id.mainVideo);
            commenterImg = itemView.findViewById(R.id.commenterImg);
            postByPic = itemView.findViewById(R.id.postByPic);
            muteIcon = itemView.findViewById(R.id.muteIcon);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            comments = itemView.findViewById(R.id.comments);
            menu = itemView.findViewById(R.id.menu);
            forward = itemView.findViewById(R.id.forward);
        }
    }

    public interface HomePostsAdapterCallBacks {
        public void takeUserToMyUserProfile(String userId);

        public void takeUserToOtherUserProfile(String userId);

        public void takeUserToLikesScreen(String postId);

        public void onLikedPost(PostsModel model);

        public void onUnlikedPost(PostsModel model);

        public void onFileDownload(String filename);
        public void onMutePost(PostsModel model);
    }
}
