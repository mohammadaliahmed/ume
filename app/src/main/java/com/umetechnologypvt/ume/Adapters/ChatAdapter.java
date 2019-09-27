package com.umetechnologypvt.ume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umetechnologypvt.ume.Activities.PlayVideo;
import com.umetechnologypvt.ume.Activities.ViewPictures;
import com.umetechnologypvt.ume.Activities.ViewPost;
import com.umetechnologypvt.ume.Models.ChatModel;
import com.umetechnologypvt.ume.Models.UserModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.Constants;
import com.umetechnologypvt.ume.Utils.CountryUtils;
import com.umetechnologypvt.ume.Utils.DownloadFile;
import com.umetechnologypvt.ume.Utils.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements Handler.Callback {
    Context context;
    ArrayList<ChatModel> chatList;

    public int RIGHT_CHAT = 1;
    public int LEFT_CHAT = 0;
    private MediaPlayer mediaPlayer;
    private ViewHolder mAudioPlayingHolder;
    private int mPlayingPosition = -1;
    private Handler uiUpdateHandler = new Handler(this);

    UserModel hisUserModel;


    private static final int MSG_UPDATE_SEEK_BAR = 1845;

    HashMap<String, String> selectedMap = new HashMap<>();
    ChatScreenCallbacks callbacks;


    public ChatAdapter(Context context, ArrayList<ChatModel> chatList, ChatScreenCallbacks callbacks) {
        this.context = context;
        this.chatList = chatList;
        this.callbacks = callbacks;
    }


    public void setHisUserModel(UserModel hisUserModel) {
        this.hisUserModel = hisUserModel;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == RIGHT_CHAT) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ChatModel model = chatList.get(position);
        if (getItemViewType(position) == RIGHT_CHAT) {
            if (SharedPrefs.getUserModel().getPicUrl() == null) {
                Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.profile_image);

            } else {
                Glide.with(context).load(SharedPrefs.getUserModel().getPicUrl()).into(holder.profile_image);
            }
        } else {
            if (hisUserModel != null) {
                if (SharedPrefs.getUserModel().getPicUrl() == null) {
                    Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.profile_image);

                } else {
                    Glide.with(context).load(hisUserModel.getPicUrl()).into(holder.profile_image);
                }
            }
        }
        if (model.getMessageStatus() != null) {
            if (model.getMessageStatus().equalsIgnoreCase("read")) {
                Glide.with(context).load(R.drawable.ic_read).into(holder.msgStatus);
            } else if (model.getMessageStatus().equalsIgnoreCase("delivered")) {
                Glide.with(context).load(R.drawable.ic_delivered).into(holder.msgStatus);
            } else {
                Glide.with(context).load(R.drawable.ic_sent).into(holder.msgStatus);
            }

        }
        if (model.getMessageType() != null) {

            if (model.getMessageType().equals(Constants.MESSAGE_TYPE_VIDEO)) {
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.document.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.imgProgress.setVisibility(View.GONE);
                holder.balloon.setVisibility(View.VISIBLE);
                holder.messageDeleted.setVisibility(View.GONE);
                holder.video.setVisibility(View.VISIBLE);
                holder.videoPlayBtn.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                Glide.with(context).load(model.getVideoImgUrl()).into(holder.videoImg);
                if (getItemViewType(position) == LEFT_CHAT) {
                    holder.videoProgress.setVisibility(View.GONE);
                    holder.videoPlayBtn.setVisibility(View.VISIBLE);
                } else {
                    if (model.isVideoUploaded()) {
                        holder.videoPlayBtn.setVisibility(View.VISIBLE);
                        holder.videoProgress.setVisibility(View.GONE);
                    } else {
                        holder.videoProgress.setVisibility(View.VISIBLE);
                    }
                }
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);



            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_DELETED)) {
                holder.time.setVisibility(View.GONE);
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.document.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.imgProgress.setVisibility(View.GONE);
                holder.balloon.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.time.setVisibility(View.GONE);
                holder.msgStatus.setVisibility(View.GONE);
                holder.messageDeleted.setVisibility(View.VISIBLE);
                holder.map.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);



            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_STICKER)) {
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.document.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                holder.sticker.setVisibility(View.VISIBLE);
                holder.imgProgress.setVisibility(View.GONE);
                holder.balloon.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                Glide.with(context).load(model.getStickerUrl()).into(holder.sticker);
                holder.messageDeleted.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);



            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_POST)) {
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.balloon.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.image.setVisibility(View.GONE);
                holder.post.setVisibility(View.VISIBLE);
                holder.postMsg.setText(model.getMessageText());
                Glide.with(context).load(model.getImageUrl()).into(holder.postImage);
                holder.messageDeleted.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.VISIBLE);




            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_STORY)) {
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.balloon.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.image.setVisibility(View.GONE);
                holder.post.setVisibility(View.VISIBLE);
                holder.postMsg.setText(model.getMessageText());
                Glide.with(context).load(model.getImageUrl()).into(holder.postImage);
                holder.messageDeleted.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.VISIBLE);



            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE)) {
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.balloon.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.image.setVisibility(View.VISIBLE);
                holder.imgProgress.setVisibility(View.GONE);
                Glide.with(context).load(model.getImageUrl()).into(holder.image);
                if (model.getImageUrl().startsWith("/storage/em")) {
                    holder.imgProgress.setVisibility(View.VISIBLE);
                } else {
                    holder.imgProgress.setVisibility(View.GONE);
                }


                holder.messageDeleted.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);


            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_LOCATION)) {
                holder.image.setVisibility(View.GONE);
                holder.map.setVisibility(View.VISIBLE);
                holder.msgtext.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.audio.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);

                holder.sticker.setVisibility(View.GONE);

                holder.balloon.setVisibility(View.VISIBLE);
                holder.postImage.setVisibility(View.GONE);

                holder.translation.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.msgtext.setText(model.getMessageText());
                holder.imgProgress.setVisibility(View.GONE);
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.messageDeleted.setVisibility(View.GONE);
                holder.contact.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);



            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_CONTACT)) {
                holder.contact.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);
                holder.msgtext.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.audio.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);

                holder.sticker.setVisibility(View.GONE);

                holder.balloon.setVisibility(View.VISIBLE);
                holder.translation.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);


                holder.msgtext.setText(model.getMessageText());
                holder.imgProgress.setVisibility(View.GONE);
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.messageDeleted.setVisibility(View.GONE);
                holder.phoneName.setText(model.getPhoneName());
                holder.phoneNumber.setText(model.getPhoneNumber());
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);


            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_TEXT)) {
                holder.contact.setVisibility(View.GONE);

                holder.image.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.msgtext.setVisibility(View.VISIBLE);
                holder.name.setText(model.getName());
                holder.audio.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);

                holder.sticker.setVisibility(View.GONE);
                holder.balloon.setVisibility(View.VISIBLE);
                holder.translation.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.msgtext.setText(model.getMessageText());
                holder.imgProgress.setVisibility(View.GONE);
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.messageDeleted.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);


            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_DOCUMENT)) {
                holder.contact.setVisibility(View.GONE);

                holder.image.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.msgtext.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.balloon.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);

                holder.audio.setVisibility(View.GONE);
                holder.document.setVisibility(View.VISIBLE);
                holder.imgProgress.setVisibility(View.GONE);
                holder.msgtext.setText(model.getMessageText());
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.messageDeleted.setVisibility(View.GONE);
                if (model.getDocumentFileName() != null) {
                    holder.documentName.setText(model.getDocumentFileName());
                }
                holder.documentType.setText(model.getMediaType().replace(".", ""));
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);


            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_TRANSLATED)) {
                holder.contact.setVisibility(View.GONE);

                holder.image.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.msgtext.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.balloon.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.GONE);

                holder.audio.setVisibility(View.GONE);
                holder.document.setVisibility(View.GONE);
                holder.imgProgress.setVisibility(View.GONE);
//                holder.msgtext.setText(model.getMessageText());
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                holder.messageDeleted.setVisibility(View.GONE);
                holder.translation.setVisibility(View.VISIBLE);
                holder.translatedText.setText(model.getTranslatedText());
                holder.translationName.setText(model.getLanguage());
                Glide.with(context).load(CountryUtils.getFlagDrawableResId(model.getLanguage())).into(holder.translationFlag);
                if (model.getLanguageName() != null) {
                    holder.translationName.setText(model.getLanguageName());
                } else {
                    holder.translationName.setText("");
                }
//                holder.translatedText.setCompoundDrawablesWithIntrinsicBounds(0, CountryUtils.getFlagDrawableResId(model.getLanguage()), 0, 0);
                holder.originalText.setText(model.getOriginalText());
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);


            } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_AUDIO)) {
                holder.contact.setVisibility(View.GONE);

                if (getItemViewType(position) == RIGHT_CHAT) {
                    if (model.isAudioUploaded()) {

                        holder.playPause.setVisibility(View.VISIBLE);
                        holder.audioProgress.setVisibility(View.GONE);


                    } else {
                        holder.audioProgress.setVisibility(View.VISIBLE);
                        holder.playPause.setVisibility(View.GONE);
                    }
                } else {
                    holder.audioProgress.setVisibility(View.GONE);
                    holder.playPause.setVisibility(View.VISIBLE);
                }
//                holder.playPause.setVisibility(View.VISIBLE);
                holder.imgProgress.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);
                holder.map.setVisibility(View.GONE);

                holder.document.setVisibility(View.GONE);
                holder.sticker.setVisibility(View.GONE);
                holder.translation.setVisibility(View.GONE);

                holder.name.setText(model.getName());
                holder.balloon.setVisibility(View.VISIBLE);

                holder.msgtext.setVisibility(View.GONE);
                holder.audio.setVisibility(View.VISIBLE);

                holder.audioTime.setText(CommonUtils.getDuration(model.getMediaTime()));
                holder.time.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                if (position == mPlayingPosition) {
                    mAudioPlayingHolder = holder;
                    updatePlayingView();
                } else {
                    updateInitialPlayerView(holder);
                }
                holder.messageDeleted.setVisibility(View.GONE);
                holder.postImage.setVisibility(View.GONE);
                holder.post.setVisibility(View.GONE);

            }

        }


        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, model.getPhoneName())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, model.getPhoneNumber());

                context.startActivity(contactIntent);


            }
        });

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.showToast("Location");


                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + model.getLat() + "," + model.getLon());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });


        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getMessageType().equalsIgnoreCase(Constants.MESSAGE_TYPE_STORY)) {

                    if (System.currentTimeMillis() - model.getStoryTime() < 86400000) {


                    } else {
                        CommonUtils.showToast("Story is expired");
                    }
                } else {
                    Intent i = new Intent(context, ViewPost.class);
                    i.putExtra("postId", model.getPostId());
                    context.startActivity(i);
                }
            }
        });
        holder.document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                DownloadFile.fromUrl1(model.getDocumentUrl());
//                String filename = "" + model.getDocumentUrl().substring(model.getDocumentUrl().length() - 7, model.getDocumentUrl().length());
                String filename = model.getDocumentFileName();
                File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS) + "/" + filename);

                if (applictionFile != null && applictionFile.exists()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
                    context.startActivity(intent);

                } else {
                    DownloadFile.fromUrll(model.getDocumentUrl(), filename);
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);

                    intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
                    context.startActivity(intent);
                }

            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(context, ViewPictures.class);
                i.putExtra("url", model.getImageUrl());
                context.startActivity(i);
                String filename = "" + model.getImageUrl().substring(model.getImageUrl().length() - 7, model.getImageUrl().length());


                File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS) + "/" + filename + ".png");

                if (applictionFile != null && applictionFile.exists()) {
                } else {
                    if (getItemId(position) == LEFT_CHAT) {

                        DownloadFile.fromUrl(model.getImageUrl(), filename + ".png");

                    }

                }


            }

        });


        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, PlayVideo.class);
                i.putExtra("url", model.getVideoUrl());
                context.startActivity(i);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.itemView.setOnLongClickListener(v -> {


            if (getItemViewType(position) == LEFT_CHAT && model.getMessageType().equalsIgnoreCase(Constants.MESSAGE_TYPE_TEXT)) {
//                CommonUtils.showToast("Transalte this");
                callbacks.setTranslateMsg(model);
            } else {
                deleteMsg(hisUserModel, model, position);

            }
            return false;
        });
        holder.document.setOnLongClickListener(v -> {


            deleteMsg(hisUserModel, model, position);
            return false;
        });
        holder.audio.setOnLongClickListener(v -> {


            deleteMsg(hisUserModel, model, position);
            return false;
        });
        holder.image.setOnLongClickListener(v -> {


            deleteMsg(hisUserModel, model, position);
            return false;
        });


        holder.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getItemViewType(position) == LEFT_CHAT) {
                    if (model.getAudioUrl().contains("https://firebasestorage"))
                        downloadAudio(model);
                }

                performPlayButtonClick(model, holder);

            }
        });

//        if (model.getMessageType().equalsIgnoreCase(Constants.MESSAGE_TYPE_VIDEO)) {
//            MediaController mc = new MediaController(context);
//            mc.setAnchorView(holder.video);
//            mc.setMediaPlayer(holder.video);
//            Uri video = Uri.parse(model.getVideoUrl());
//            holder.video.setMediaController(mc);
//            holder.video.setVideoURI(video);
//            holder.video.start();
//        }
    }


    private void deleteMsg(UserModel hisUserModel, ChatModel model, int position) {
        if (!model.getMessageType().equalsIgnoreCase(Constants.MESSAGE_TYPE_DELETED)) {

            if ((System.currentTimeMillis() - model.getTime()) < Constants.DELETED_MSG_TIME) {
                if (getItemViewType(position) == RIGHT_CHAT) {
                    callbacks.deleteMessageForAll(hisUserModel, model);
                } else {
                    callbacks.deleteMessage(hisUserModel, model);

                }

            } else {
                callbacks.deleteMessage(hisUserModel, model);
            }
        }
    }

    private void downloadAudio(ChatModel model) {
        String audioLocalUrl = Long.toHexString(Double.doubleToLongBits(Math.random()));

        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/r";

        String filename = "" + model.getAudioUrl().substring(model.getAudioUrl().length() - 7, model.getAudioUrl().length());


        File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + filename + ".mp3");

        if (applictionFile != null && applictionFile.exists()) {
        } else {


            DownloadFile.fromUrl(model.getAudioUrl(), filename + ".mp3");
            String fname = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/" + filename + ".mp3";
            callbacks.setAudioDownloadUrl(model, fname);


        }
    }

    public void setNewList(ArrayList<ChatModel> chatModelArrayList) {
        this.chatList = chatModelArrayList;
        notifyDataSetChanged();
    }


    public interface ChatScreenCallbacks {

        public void deleteMessage(UserModel otherUser, ChatModel chatModel);

        public void deleteMessageForAll(UserModel otherUser, ChatModel chatModel);

        public void setAudioDownloadUrl(ChatModel model, String newAudioUrl);

        public void setTranslateMsg(ChatModel model);

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

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    private void updateInitialPlayerView(ViewHolder holder) {
        if (holder == mAudioPlayingHolder) {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
        }
        holder.seekBar.setProgress(0);
        holder.playPause.setImageResource(R.drawable.play_btn);
    }

    private void updatePlayingView() {
        if (mediaPlayer == null || mAudioPlayingHolder == null) return;
        mAudioPlayingHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());

        if (mediaPlayer.isPlaying()) {
            uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
            mAudioPlayingHolder.playPause.setImageResource(R.drawable.stop);

        } else {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            mAudioPlayingHolder.playPause.setImageResource(R.drawable.play_btn);
        }
        mAudioPlayingHolder.audioTime.setText(CommonUtils.getDuration(mediaPlayer.getCurrentPosition()));

    }

    private void startMediaPlayer(ChatModel model) {


        try {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(model.getAudioUrl()));
            } catch (Exception e) {
                e.printStackTrace();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(model.getAudioUrl());
            }
            if (mediaPlayer == null) return;

            mediaPlayer.setOnCompletionListener(mp -> releaseMediaPlayer());
            if (mAudioPlayingHolder != null)
                mediaPlayer.seekTo(mAudioPlayingHolder.seekBar.getProgress());
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
        ChatModel model = chatList.get(position);
        if (model.getMessageBy() != null) {
            if (model.getMessageBy().equalsIgnoreCase(SharedPrefs.getUserModel().getUsername())) {
                return RIGHT_CHAT;
            } else {
                return LEFT_CHAT;
            }
        }
        return -1;

    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_UPDATE_SEEK_BAR: {

                int percentage = mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration();
                mAudioPlayingHolder.seekBar.setProgress(percentage);
                mAudioPlayingHolder.audioTime.setText(CommonUtils.getDuration(mediaPlayer.getCurrentPosition()));
                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                return true;
            }
        }
        return false;
    }

    public void activityBackPressed() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            releaseMediaPlayer();

        }
    }


    private void performPlayButtonClick(ChatModel recordingItem, ViewHolder myViewHolder) {

        int currentPosition = chatList.indexOf(recordingItem);
        if (currentPosition == mPlayingPosition) {
            // toggle between play/pause of audio
            if (mediaPlayer == null) return;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            // start another audio playback
            ChatModel previousPlayObject = mPlayingPosition == -1 ? null : chatList.get(mPlayingPosition);
            mPlayingPosition = currentPosition;
            if (mediaPlayer != null) {
                if (null != mAudioPlayingHolder) {
                    if (previousPlayObject != null)
                        mAudioPlayingHolder.audioTime.setText(CommonUtils.getDuration(previousPlayObject.getMediaTime()));
                    updateNonPlayingView(mAudioPlayingHolder);
                }
                mediaPlayer.release();
            }
            mAudioPlayingHolder = myViewHolder;
            startMediaPlayer(recordingItem);
        }
        updatePlayingView();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener {
        TextView msgtext, time, name, audioTime;
        CircleImageView profile_image;
        ImageView image, playPause, map;
        RelativeLayout document;
        RelativeLayout audio;
        SeekBar seekBar;
        ProgressBar audioProgress, attachmentProgress;
        RelativeLayout selectedItem;
        ProgressBar imgProgress;
        RelativeLayout balloon;
        ImageView sticker;
        TextView messageDeleted;
        RelativeLayout video;
        ImageView videoImg, videoPlayBtn, msgStatus;
        ProgressBar videoProgress;
        TextView documentName, documentType;
        TextView translatedText, originalText;
        LinearLayout translation;
        TextView phoneNumber, phoneName;
        LinearLayout contact;
        CircleImageView translationFlag;
        TextView translationName;
        RelativeLayout post;
        ImageView postImage;
        TextView postMsg;


        public ViewHolder(View itemView) {
            super(itemView);
            msgtext = itemView.findViewById(R.id.msgtext);
            time = itemView.findViewById(R.id.time);
            profile_image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            audio = itemView.findViewById(R.id.audio);
            playPause = itemView.findViewById(R.id.playPause);
            audioTime = itemView.findViewById(R.id.audioTime);
            seekBar = itemView.findViewById(R.id.seek);
            document = itemView.findViewById(R.id.document);
            audioProgress = itemView.findViewById(R.id.audioProgress);
            audioProgress = itemView.findViewById(R.id.audioProgress);
            selectedItem = itemView.findViewById(R.id.selectedItem);
            imgProgress = itemView.findViewById(R.id.imgProgress);
            balloon = itemView.findViewById(R.id.balloon);
            sticker = itemView.findViewById(R.id.sticker);
            messageDeleted = itemView.findViewById(R.id.messageDeleted);
            video = itemView.findViewById(R.id.video);
            videoImg = itemView.findViewById(R.id.videoImg);
            videoProgress = itemView.findViewById(R.id.videoProgress);
            videoPlayBtn = itemView.findViewById(R.id.videoPlayBtn);
            msgStatus = itemView.findViewById(R.id.msgStatus);
            documentName = itemView.findViewById(R.id.documentName);
            documentType = itemView.findViewById(R.id.documentType);
            translation = itemView.findViewById(R.id.translation);
            originalText = itemView.findViewById(R.id.originalText);
            translatedText = itemView.findViewById(R.id.translatedText);
            contact = itemView.findViewById(R.id.contact);
            phoneName = itemView.findViewById(R.id.phoneName);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            map = itemView.findViewById(R.id.map);
            translationFlag = itemView.findViewById(R.id.translationFlag);
            translationName = itemView.findViewById(R.id.translationName);
            post = itemView.findViewById(R.id.post);
            postImage = itemView.findViewById(R.id.postImage);
            postMsg = itemView.findViewById(R.id.postMsg);

//            seekBar.setOnSeekBarChangeListener(this);


        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            if (b && mediaPlayer != null && getAdapterPosition() == mPlayingPosition)
                mediaPlayer.seekTo(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }


    }

    private void updateNonPlayingView(ViewHolder holder) {
        if (holder == mAudioPlayingHolder) {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
        }
        holder.seekBar.setProgress(0);
        holder.playPause.setImageResource(R.drawable.play_btn);
    }

    private void releaseMediaPlayer() {

        if (null != mAudioPlayingHolder) {
            updateNonPlayingView(mAudioPlayingHolder);
        }
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mPlayingPosition = -1;
    }

    public void stopPlayer() {
        if (null != mediaPlayer) {
            releaseMediaPlayer();
        }
    }


}
