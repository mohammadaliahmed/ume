package com.appsinventiv.ume.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.UserManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.ume.Activities.ImageCrop.PickerBuilder;
import com.appsinventiv.ume.Adapters.ChatAdapter;
import com.appsinventiv.ume.Adapters.ChooseLangaugeFromDialogAdapter;
import com.appsinventiv.ume.ApplicationClass;
import com.appsinventiv.ume.Models.ChatModel;
import com.appsinventiv.ume.Models.LangaugeModel;
import com.appsinventiv.ume.Models.MediaModel;
import com.appsinventiv.ume.Models.UserModel;
import com.appsinventiv.ume.R;
import com.appsinventiv.ume.Utils.CommonUtils;
import com.appsinventiv.ume.Utils.CompressImage;
import com.appsinventiv.ume.Utils.Constants;
import com.appsinventiv.ume.Utils.GifSizeFilter;
import com.appsinventiv.ume.Utils.MyEditText;
import com.appsinventiv.ume.Utils.NotificationAsync;
import com.appsinventiv.ume.Utils.NotificationObserver;
import com.appsinventiv.ume.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.googlecode.mp4parser.authoring.Edit;
import com.iceteck.silicompressorr.SiliCompressor;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChattingScreen extends AppCompatActivity implements NotificationObserver {


    ImageView translate;
    public static final String LOG_TAGa = "singlchat";

    public static final String FILE_PROVIDER_AUTHORITY = "com.appsinventiv.ume.provider";
    private static final int REQUEST_TAKE_CAMERA_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID = 2;
    private static final int REQUEST_TAKE_VIDEO = 200;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    String mCurrentPhotoPath;
    Uri capturedUri = null;
    Uri compressUri = null;
//    ImageView imageView;
//    TextView picDescription;
//    private ImageView videoImageView;
//    LinearLayout compressionMsg;


    private static final int REQUEST_TAKE_GALLERY_VIDEO = 99;
    RecordView recordView;
    RecordButton recordButton;
    DatabaseReference mDatabase;
    MyEditText message;
    ImageView send, back;
    CardView attachArea;
    ImageView attach;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ChatAdapter adapter;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    int soundId;
    SoundPool sp;
    ImageView pickImg, pickVideo, pickDocument;
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_FILE = 25;


    //    ArrayList<UserModel> pList=new ArrayList<>();
    String msgText;

    RelativeLayout recordingArea;
    RelativeLayout messagingArea;
    private static final String LOG_TAG = "AudioRecordTest";
    //    String groupName;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;

    String recordingLocalUrl;
    long recordingTime = 0L;
    //    String groupId;
    boolean isAttachAreaVisible = false;
    String roomId;
    String userId;

    ArrayList<UserModel> participantsList = new ArrayList<>();
    Uri file;
    String messageId;
    CircleImageView toolbarImage;
    Toolbar toolbar;
    TextView otherParticipantName, userStatus;
    UserModel mYUserModel;
    UserModel hisUserModel;
    Menu menu;
    ImageView camera;
    private String languageCode = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chatting_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("");
        translate = findViewById(R.id.translate);
        userStatus = findViewById(R.id.userStatus);
        otherParticipantName = findViewById(R.id.otherParticipantName);
        send = findViewById(R.id.send);
        attach = findViewById(R.id.attach);
        attachArea = findViewById(R.id.attachArea);
        message = findViewById(R.id.message);
        pickImg = findViewById(R.id.pickImg);
        pickDocument = findViewById(R.id.pickDocument);
        pickVideo = findViewById(R.id.pickVideo);
        toolbarImage = findViewById(R.id.toolbarImage);
        camera = findViewById(R.id.camera);

        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordingArea = findViewById(R.id.recordingArea);
        messagingArea = findViewById(R.id.messageArea);
        Intent i = getIntent();
        roomId = getIntent().getStringExtra("roomId");
        userId = getIntent().getStringExtra("userId");

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTranslateDialog();
            }
        });


        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachArea.setVisibility(View.GONE);
                isAttachAreaVisible = false;
//                dispatchTakeVideoIntent();
//                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);


            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });

        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(SingleChattingScreen.this, UserProfileScreen.class);
                i1.putExtra("userId", hisUserModel.getUsername());
                startActivity(i1);
            }
        });
        otherParticipantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(SingleChattingScreen.this, UserProfileScreen.class);
                i1.putExtra("userId", hisUserModel.getUsername());
                startActivity(i1);
            }
        });


        recyclerView = findViewById(R.id.chats);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                attachArea.setVisibility(View.GONE);
                isAttachAreaVisible = false;
                return false;
            }
        });
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this, chatModelArrayList, new ChatAdapter.ChatScreenCallbacks() {
            @Override
            public void deleteMessage(UserModel otherUser, ChatModel chatModel) {
                showDeleteAlert(otherUser.getUsername(), chatModel.getId());
            }

            @Override
            public void deleteMessageForAll(UserModel otherUser, ChatModel chatModel) {
                showDeleteForAllAlert(otherUser.getUsername(), chatModel.getId());
            }

            @Override
            public void setAudioDownloadUrl(ChatModel model, String newAudioUrl) {
                mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                        .child(hisUserModel.getUsername()).child(model.getId()).child("audioUrl").setValue(newAudioUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        CommonUtils.showToast("Updated");
                    }
                });
            }
        });

        message.setKeyBoardInputCallbackListener(new MyEditText.KeyBoardInputCallbackListener() {
            @Override
            public void onCommitContent(InputContentInfoCompat inputContentInfo,
                                        int flags, Bundle opts) {
                //you will get your gif/png/jpg here in opts bundle
                sendStickerMessageToServer(Constants.MESSAGE_TYPE_STICKER, "" + inputContentInfo.getContentUri(), "png");


            }
        });
        recyclerView.setAdapter(adapter);
        getParticipantsFromDB();
        getMessagesFromServer();


        pickDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachArea.setVisibility(View.GONE);
                isAttachAreaVisible = false;
                openFile(REQUEST_CODE_FILE);
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAttachAreaVisible) {
                    attachArea.setVisibility(View.GONE);
                    isAttachAreaVisible = false;
                } else {
                    attachArea.setVisibility(View.VISIBLE);
                    isAttachAreaVisible = true;
                }
            }
        });

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = sp.load(SingleChattingScreen.this, R.raw.tick_sound, 1);


//        intersititialAd();
        recordButton.setRecordView(recordView);


        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    recordingArea.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);


                } else {
                    recordingArea.setVisibility(View.GONE);
                    send.setVisibility(View.VISIBLE);
                }
            }
        });

        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                Log.d("RecordView", "onStart");
                mRecorder = null;
                setMargins(recyclerView, 0, 0, 0, 170);
                startRecording();


            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                Log.d("RecordView", "onCancel");
                mRecorder.release();
                mRecorder = null;
                setMargins(recyclerView, 0, 0, 0, 0);


            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
                String time = CommonUtils.getFormattedDate(recordTime);
                Log.d("RecordView", "onFinish");

                setMargins(recyclerView, 0, 0, 0, 0);

                Log.d("RecordTime", time);
                recordingTime = recordTime;
                messagingArea.setVisibility(View.VISIBLE);
                stopRecording();
                recyclerView.scrollToPosition(chatModelArrayList.size() - 1);


            }

            @Override
            public void onLessThanSecond() {

                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");
                setMargins(recyclerView, 0, 0, 0, 0);

                messagingArea.setVisibility(View.VISIBLE);
                mRecorder = null;
                recyclerView.scrollToPosition(chatModelArrayList.size() - 1);


            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                setMargins(recyclerView, 0, 0, 0, 170);

                Log.d("RecordView", "Basket Animation Finished");
                messagingArea.setVisibility(View.VISIBLE);
                setMargins(recyclerView, 0, 0, 0, 0);


                recyclerView.scrollToPosition(chatModelArrayList.size() - 1);


            }
        });
        recordView.setSoundEnabled(true);

        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachArea.setVisibility(View.GONE);
                isAttachAreaVisible = false;

                mSelected.clear();
                imageUrl.clear();
                initMatisse();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (message.getText().length() == 0) {
                    message.setError("Can't send empty message");
                } else {
                    msgText = message.getText().toString();
                    sp.play(soundId, 1, 1, 0, 0, 1);

                    messageId = mDatabase.push().getKey();
                    sendMyMessageToServer(Constants.MESSAGE_TYPE_TEXT, "", "", hisUserModel);


                }
            }
        });
//        markAsRead();

    }

    private void showLanguagePickerDialog(ImageView translateTo, TextView languageName) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View languageDialogView = factory.inflate(R.layout.dialog_language_pick, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(languageDialogView);
        ArrayList<LangaugeModel> itemList = new ArrayList<>();
        itemList.add(new LangaugeModel("English", "en", R.drawable.flag_united_states_of_america));
        itemList.add(new LangaugeModel("Spanish", "es", R.drawable.flag_spain));
        itemList.add(new LangaugeModel("German", "de", R.drawable.flag_germany));
        itemList.add(new LangaugeModel("French", "fr", R.drawable.flag_france));
        RecyclerView recycler = languageDialogView.findViewById(R.id.recycler);
        Button cancel = languageDialogView.findViewById(R.id.cancel);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        ChooseLangaugeFromDialogAdapter ada = new ChooseLangaugeFromDialogAdapter(this, itemList, new ChooseLangaugeFromDialogAdapter.Callbacks() {
            @Override
            public void callback(LangaugeModel model) {
                dialog.dismiss();
                Glide.with(SingleChattingScreen.this).load(model.getPicDrawable()).into(translateTo);
                languageName.setText(model.getLanguageName());
                languageCode = model.getLangCode();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        recycler.setAdapter(ada);

        dialog.show();
    }

    private void showTranslateDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View translteDialogView = factory.inflate(R.layout.dialog_layout, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();

        deleteDialog.setView(translteDialogView);
        EditText text;
        ImageView translateTo;
        TextView languageName;
        Button clear = translteDialogView.findViewById(R.id.clear);
        Button translate = translteDialogView.findViewById(R.id.translate);
        text = translteDialogView.findViewById(R.id.text);
        translateTo = translteDialogView.findViewById(R.id.translateTo);
        languageName = translteDialogView.findViewById(R.id.languageName);

        LinearLayout translatedLay;
        EditText translatedText;
        FloatingActionButton sendMsg;

        translatedLay = translteDialogView.findViewById(R.id.translatedLay);
        translatedText = translteDialogView.findViewById(R.id.translatedText);
        sendMsg = translteDialogView.findViewById(R.id.sendMsg);


        translateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguagePickerDialog(translateTo, languageName);
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                sendTranslatedMessageToServer(Constants.MESSAGE_TYPE_TRANSLATED,
                        translatedText.getText().toString(), text.getText().toString(), languageCode);

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                text.setText("");
            }
        });
        translteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
            }
        });
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.showToast("Translating: " + text.getText().toString());
                translatedLay.setVisibility(View.VISIBLE);
                ;
                translateNow(text.getText().toString(), translatedText);
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    translate.setEnabled(true);

                    translate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    translate.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    translate.setEnabled(false);

                }
            }
        });


        deleteDialog.show();
    }

    private void translateNow(String originalText, EditText translatedText) {
        final String[] finalText = {""};
        final Handler textViewHandler = new Handler();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(Constants.API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(originalText,
                                Translate.TranslateOption.targetLanguage(languageCode));
                String textt = translation.getTranslatedText();

                finalText[0] = textt;
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (translatedText != null) {
                            translatedText.setText(translation.getTranslatedText());
                        }
                    }
                });
                return null;
            }
        }.execute();

    }

    private void markAsRead() {
        if (chatModelArrayList.size() > 0 && hisUserModel != null) {
            for (ChatModel msg : chatModelArrayList) {
                if (msg.getId() != null) {
                    mDatabase
                            .child("Chats")
                            .child(hisUserModel.getUsername())
                            .child(SharedPrefs.getUserModel().getUsername())
                            .child(msg.getId())
                            .child("messageStatus")
                            .setValue("read");
                }
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            try {

                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.1);
                capturedUri = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        createMediaFile(TYPE_VIDEO));

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d(LOG_TAG, "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = (type == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                type == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
        File file = File.createTempFile(
                fileName,  /* prefix */
                type == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Get the path of the file created
        mCurrentPhotoPath = file.getAbsolutePath();
        Log.d(LOG_TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return file;
    }

    private void startCamera() {

        imageUrl.clear();
        new PickerBuilder(SingleChattingScreen.this, PickerBuilder.SELECT_FROM_CAMERA)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public void onImageReceived(Uri imageUri) {
                        mSelected = new ArrayList<>();
                        mSelected.add(imageUri);
                        CompressImage compressImage = new CompressImage(SingleChattingScreen.this);
                        imageUrl.add(compressImage.compressImage("" + mSelected.get(0)));
                        sendPictureMessageToServer(Constants.MESSAGE_TYPE_IMAGE, imageUrl.get(0), "png");


                    }
                })
                .start();
    }

    private void getParticipantsFromDB() {
        mDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    hisUserModel = dataSnapshot.getValue(UserModel.class);
                    if (hisUserModel.getPicUrl() != null) {
                        markAsRead();
                        try {
                            Glide.with(SingleChattingScreen.this).load(hisUserModel.getPicUrl()).into(toolbarImage);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        adapter.setHisUserModel(hisUserModel);

                    }
                    otherParticipantName.setText(hisUserModel.getName());
                    userStatus.setText(
                            hisUserModel.getStatus()
                                    .equalsIgnoreCase("Online") ? "Online" : "Last seen " + CommonUtils.getFormattedDate(Long.parseLong(hisUserModel.getStatus()))
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void startRecording() {
        messagingArea.setVisibility(View.GONE);

        recordingLocalUrl = Long.toHexString(Double.doubleToLongBits(Math.random()));
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setOutputFile(mFileName + recordingLocalUrl + ".mp3");
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    messagingArea.setVisibility(View.VISIBLE);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    messagingArea.setVisibility(View.VISIBLE);

                }

            }
        }, 100);


    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        messageId = mDatabase.push().getKey();
        sp.play(soundId, 1, 1, 0, 0, 1);

        sendAudioMessageToServer(Constants.MESSAGE_TYPE_AUDIO, mFileName + recordingLocalUrl + ".mp3", "mp3");


    }

    public SingleChattingScreen() {

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/r";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && data != null) {
            mSelected = Matisse.obtainResult(data);
            for (Uri img : mSelected) {
                CompressImage compressImage = new CompressImage(SingleChattingScreen.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            for (String img : imageUrl) {
                sendPictureMessageToServer(Constants.MESSAGE_TYPE_IMAGE, img, "png");


            }

        }
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                //create destination directory
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                if (f.mkdirs() || f.isDirectory())
                    //compress and output new video specs
                    sendVideoMessageToServer(Constants.MESSAGE_TYPE_VIDEO, mCurrentPhotoPath, ".mp4", f);
//                    new VideoCompressAsyncTask(this).execute(mCurrentPhotoPath, f.getPath());

            }
        }

        if (requestCode == REQUEST_CODE_FILE && data != null) {
            Uri Fpath = data.getData();
//            putDocument(Fpath);
            sendDocumentMessageToServer(Constants.MESSAGE_TYPE_DOCUMENT, "" + Fpath, getMimeType(SingleChattingScreen.this, Fpath));
        }
        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                sendVideoMessageToServer(Constants.MESSAGE_TYPE_VIDEO, "" + selectedImageUri, getMimeType(SingleChattingScreen.this, selectedImageUri));

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;
        ChatModel model;

        public VideoCompressAsyncTask(Context context, ChatModel model) {
            mContext = context;
            this.model = model;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_photo_camera_white_48px));
//            compressionMsg.setVisibility(View.VISIBLE);
//            picDescription.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", getString(R.string.video_compression_complete), imageFile.getName(), value);
//            compressionMsg.setVisibility(View.GONE);
//            picDescription.setVisibility(View.VISIBLE);
//            picDescription.setText(text);
//            CommonUtils.showToast(text);
//            putRecordedVideo(compressedFilePath, model);
//            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void putSticker(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.parse(path);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("stickerUrl").setValue("" + downloadUrl);
                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setStickerUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .setValue(chatModel);
                        sendNotification(chatModel.getMessageType(), chatModel.getId());


                        String k = mDatabase.push().getKey();
                        mDatabase.child("Stickers").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_IMAGE, "" + downloadUrl, System.currentTimeMillis()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    public void putDocument(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.parse(path);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Documents").child(imgName);
        String fileName = CommonUtils.uri2filename(file);
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("documentUrl").setValue("" + downloadUrl);


                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setDocumentUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .setValue(chatModel);

                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername())
                                .child(chatModel.getId()).child("documentFileName").setValue("" + fileName);

                        sendNotification(chatModel.getMessageType(), chatModel.getId());

                        String k = mDatabase.push().getKey();
                        mDatabase.child("Documents").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_DOCUMENT, "" + downloadUrl, System.currentTimeMillis()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    public void putVideo(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.parse(path);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Videos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
//                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("videoUrl").setValue("" + downloadUrl);
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("videoUploaded").setValue(true);


                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setVideoUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .setValue(chatModel);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername())
                                .child(chatModel.getId()).child("videoUploaded").setValue(true);

                        sendNotification(chatModel.getMessageType(), chatModel.getId());

                        String k = mDatabase.push().getKey();
                        mDatabase.child("Videos").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_VIDEO, "" + downloadUrl, System.currentTimeMillis()));


                        putVideoPicture(CommonUtils.getVideoPic("" + downloadUrl), chatModel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }


    public void putRecordedVideo(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Videos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("videoUrl").setValue("" + downloadUrl);
                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setVideoUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .setValue(chatModel);
                        sendNotification(chatModel.getMessageType(), chatModel.getId());

                        String k = mDatabase.push().getKey();
                        mDatabase.child("Videos").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_VIDEO, "" + downloadUrl, System.currentTimeMillis()));
//                        putVideoPicture(CommonUtils.getVideoPic("" + downloadUrl), chatModel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    public void putVideoPicture(Uri path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = path;

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
//                                .child(hisUserModel.getUsername()).child(chatModel.getId())
//                                .child("videoImgUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
                        mDatabase.child("Chats").child(hisUserModel.getUsername())
                                .child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .child("videoImgUrl").setValue("" + downloadUrl);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    public void putPictures(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("imageUrl").setValue("" + downloadUrl);
                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setImageUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(chatModel.getId())
                                .setValue(chatModel);
                        sendNotification(chatModel.getMessageType(), chatModel.getId());

                        String k = mDatabase.push().getKey();
                        mDatabase.child("Images").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_IMAGE, "" + downloadUrl, System.currentTimeMillis()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    public void putAudio(String path, ChatModel chatModel) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        file = Uri.fromFile(new File(path));


        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Audio").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

//                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
//                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("audioUrl").setValue("" + path);
//

                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("audioUploaded").setValue(true);

                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        chatModel.setAudioUrl("" + downloadUrl);
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(messageId)
                                .setValue(chatModel);
                        sendNotification(chatModel.getMessageType(), chatModel.getId());

                        String k = mDatabase.push().getKey();

                        mDatabase.child("Audio").child(k).setValue(new MediaModel(k, Constants.MESSAGE_TYPE_AUDIO, "" + downloadUrl, System.currentTimeMillis()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    private void initMatisse() {
        Matisse.from(SingleChattingScreen.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(10)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    private void openFile(Integer CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, CODE);
    }

    private void sendTranslatedMessageToServer(final String type, String translatedText, String originalText, String language) {
        messageId = mDatabase.push().getKey();
        message.setText(null);
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                System.currentTimeMillis(),
                "sent", translatedText, originalText, language
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername())
                .child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(messageId)
                                .setValue(chatModel);
                        sendNotification(type, chatModel.getId());

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void sendMyMessageToServer(final String type, final String url, String extension, UserModel model) {

        message.setText(null);
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(model.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
                        chatModel.setUsername(SharedPrefs.getUserModel().getUsername());
                        chatModel.setName(SharedPrefs.getUserModel().getName());
                        chatModel.setPicUrl(SharedPrefs.getUserModel().getPicUrl());
                        mDatabase.child("Chats").child(hisUserModel.getUsername()).child(SharedPrefs.getUserModel().getUsername()).child(messageId)
                                .setValue(chatModel);
                        sendNotification(type, chatModel.getId());

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void sendNotification(String type, String msgId) {
        NotificationAsync notificationAsync = new NotificationAsync(SingleChattingScreen.this);
//                        String NotificationTitle = "New message in " + groupName;
        String NotificationTitle = "New message ";
        String NotificationMessage = "";
        if (type.equals(Constants.MESSAGE_TYPE_TEXT)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": " + msgText;
        } else if (type.equals(Constants.MESSAGE_TYPE_IMAGE)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83D\uDCF7 Image";
        } else if (type.equals(Constants.MESSAGE_TYPE_AUDIO)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83C\uDFB5 Audio";
        } else if (type.equals(Constants.MESSAGE_TYPE_DOCUMENT)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83D\uDCC4 Document";
        } else if (type.equals(Constants.MESSAGE_TYPE_STICKER)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83D\uDD37 Sticker";
        } else if (type.equals(Constants.MESSAGE_TYPE_VIDEO)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83D\uDCFD Video";
        }else if (type.equals(Constants.MESSAGE_TYPE_TRANSLATED)) {
            NotificationMessage = SharedPrefs.getUserModel().getName() + ": \uD83C\uDE02 Translation";
        }
        notificationAsync.setMsgId(messageId);
        notificationAsync.execute("ali", hisUserModel.getFcmKey(), NotificationTitle, NotificationMessage, Constants.MESSAGE_TYPE_TEXT, "chat",
                SharedPrefs.getUserModel().getUsername(),
                "" + SharedPrefs.getUserModel().getUsername().length()
        );

    }


    private void sendAudioMessageToServer(final String type, final String url, String extension) {

        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        putAudio(mFileName + recordingLocalUrl + ".mp3", chatModel);

//                        sendNotification(type);

                    }
                });


    }

    private void sendStickerMessageToServer(final String type, final String url, String extension) {
        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

                        putSticker(url, chatModel);

//                        sendNotification(type);

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void sendVideoMessageToServer(final String type, final String url, String extension, File f) {
        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
                        new VideoCompressAsyncTask(SingleChattingScreen.this, chatModel).execute(mCurrentPhotoPath, f.getPath());

//                        putVideo(url, chatModel);

//                        sendNotification(type);

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void sendVideoMessageToServer(final String type, final String url, String extension) {
        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
//                        new VideoCompressAsyncTask(SingleChattingScreen.this,chatModel).execute(mCurrentPhotoPath, f.getPath());

                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId())
                                .child("videoImgUrl").setValue("" + CommonUtils.getVideoPiac(Uri.parse(url))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        putVideo(url, chatModel);

//                        sendNotification(type);

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    private void sendPictureMessageToServer(final String type, final String url, String extension) {
        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

                        putPictures(url, chatModel);

//                        sendNotification(type);

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    private void sendDocumentMessageToServer(final String type, final String url, String extension) {
        message.setText(null);
        messageId = mDatabase.push().getKey();
        ChatModel chatModel = new ChatModel(
                messageId,
                msgText,
                SharedPrefs.getUserModel().getUsername(),
                type.equals(Constants.MESSAGE_TYPE_IMAGE) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_AUDIO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_VIDEO) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_DOCUMENT) ? url : "",
                type.equals(Constants.MESSAGE_TYPE_STICKER) ? url : "",
                hisUserModel.getUsername(),
                hisUserModel.getName(),
                hisUserModel.getPicUrl(),
                type,
                "." + extension,
                roomId,
                System.currentTimeMillis(),
                recordingTime,
                "sent"
        );

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(messageId)
                .setValue(chatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

//                        putPictures(url, chatModel);
                        final Uri file = Uri.parse(url);
                        String fileName = CommonUtils.uri2filename(file);
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                .child(hisUserModel.getUsername()).child(chatModel.getId()).child("documentFileName").setValue("" + fileName);

                        putDocument(url, chatModel);


//                        sendNotification(type);

                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private void getMessagesFromServer() {

        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(userId)
                .limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    chatModelArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel chat = snapshot.getValue(ChatModel.class);
                        if (chat != null) {
                            chatModelArrayList.add(chat);
                        }
                    }
                    recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

                    adapter.notifyDataSetChanged();
                    markAsRead();
                } else {
                    chatModelArrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(SingleChattingScreen.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        adapter.activityBackPressed();
        finish();
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
//        updateMenuItems(0, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuItems(int i, Menu menu) {
        if (menu != null && i == 2) {

        } else if (menu != null && i == 1) {
            toolbarImage.setVisibility(View.GONE);
            otherParticipantName.setVisibility(View.GONE);
            userStatus.setVisibility(View.GONE);
//            menu.findItem(R.id.action_audio).setVisible(false);
//            menu.findItem(R.id.action_video).setVisible(false);
//            menu.findItem(R.id.action_delete).setVisible(true);

        } else if (menu != null && i == 0) {
            toolbarImage.setVisibility(View.VISIBLE);
            otherParticipantName.setVisibility(View.VISIBLE);
            userStatus.setVisibility(View.VISIBLE);
//            menu.findItem(R.id.action_audio).setVisible(true);
//            menu.findItem(R.id.action_video).setVisible(true);
//            menu.findItem(R.id.action_delete).setVisible(false);


        }
//        invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_chat_menu, menu);
        updateMenuItems(0, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {


            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlert(String username, String msgId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SingleChattingScreen.this);
        builder1.setMessage("Delete message?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(msgId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Message deleted");
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

    private void showDeleteForAllAlert(String username, String msgId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SingleChattingScreen.this);
        builder.setTitle("Delete Message?");
        AlertDialog alert = builder.create();


        builder.setItems(new CharSequence[]
                        {"Delete for me", "Cancel", "Delete for everyone"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername()).child(msgId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CommonUtils.showToast("Message deleted");
                                    }
                                });
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                            case 2:
                                mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername())
                                        .child(hisUserModel.getUsername()).child(msgId).child("messageType").setValue(Constants.MESSAGE_TYPE_DELETED);
                                mDatabase.child("Chats").child(hisUserModel.getUsername())
                                        .child(SharedPrefs.getUserModel().getUsername()).child(msgId).child("messageType").setValue(Constants.MESSAGE_TYPE_DELETED);

                                break;

                        }
                    }
                });
        alert = builder.create();
        alert.show();


    }


    private void sendCallLogsToDB(String type, UserModel otherParticipant) {
        String key = mDatabase.push().getKey();
        for (UserModel user : participantsList) {
//            mDatabase.child("Calls").child(user.getUsername()).child(key).setValue(new CallModel(
//                    key,
//                    System.currentTimeMillis(),
//                    0,
//                    roomId,
//                    type,
//                    otherParticipant.getName(),
//                    otherParticipant.getPicUrl(),
//                    "Dialing",
//                    System.currentTimeMillis()
//
//
//            ));
        }
    }

    @Override
    public void onSuccess(String chatId) {
        mDatabase.child("Chats").child(SharedPrefs.getUserModel().getUsername()).child(hisUserModel.getUsername())
                .child(chatId).child("messageStatus").setValue("delivered");
    }

    @Override
    public void onFailure() {

    }

}
