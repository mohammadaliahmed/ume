package com.umetechnologypvt.ume.Stories;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.umetechnologypvt.ume.Camera.RunTimePermission;
import com.umetechnologypvt.ume.Camera.TextStatusActivity;
import com.umetechnologypvt.ume.Camera.VideoRedirectActivity;
import com.umetechnologypvt.ume.Camera.WhatsappCameraActivity;
import com.umetechnologypvt.ume.Models.PostsModel;
import com.umetechnologypvt.ume.R;
import com.umetechnologypvt.ume.Utils.CommonUtils;
import com.umetechnologypvt.ume.Utils.CompressImage;
import com.umetechnologypvt.ume.Utils.CompressImageToThumbnail;
import com.umetechnologypvt.ume.Utils.GifSizeFilter;
import com.umetechnologypvt.ume.Utils.SharedPrefs;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StoriesCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 23;
    public static Activity activity;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Handler customHandler = new Handler();
    int flag = 0;
    private File tempFile = null;
    private Camera.PictureCallback jpegCallback;
    int MAX_VIDEO_SIZE_UPLOAD = 25; //MB
    float mDist = 0;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 99;

    ImageView pickGallery;
    FrameLayout pCameraLayout;
    private List<Uri> mSelected = new ArrayList<>();
    ImageView status;
    //    ZoomControls zoomControls;
    static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    static final String VIDEO_TOTAL_DURATION = "VIDEO_TOTAL_DURATION";
    private boolean videoCompressed = false;
    private String finalVideoPath;


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (myOrientationEventListener != null)
                myOrientationEventListener.enable();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private File folder = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (runTimePermission != null) {
            runTimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stories_activity_camera);
        activity = this;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        pCameraLayout = findViewById(R.id.pCameraLayout);
        pickGallery = findViewById(R.id.pickGallery);
        status = findViewById(R.id.status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoriesCameraActivity.this, TextStatusStory.class));
            }
        });

        runTimePermission = new RunTimePermission(this);
        runTimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new RunTimePermission.RunTimePermissionListener() {

            @Override
            public void permissionGranted() {
                // First we need to check availability of play services
                initControls();

                identifyOrientationEvents();

                //create a folder to get image
                folder = new File(Environment.getExternalStorageDirectory() + "/whatsappCamera");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                //capture image on callback
                captureImageCallback();
                //
                if (camera != null) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        imgFlashOnOff.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void permissionDenied() {
            }
        });

        pickGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.showToast("Gallery");
                initMatisse();
            }
        });


//        enableZoom();

    }

    private void initMatisse() {
        mSelected.clear();
        Matisse.from(StoriesCameraActivity.this)
//                .choose(MimeType.ofVideo())
                .choose(MimeType.of(MimeType.MP4, MimeType.JPEG, MimeType.PNG), false)
                .countable(true)
                .maxSelectable(10)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 52) {

            String imagePath = data.getStringExtra(ImageEditor.EXTRA_EDITED_PATH);
            Intent intent = new Intent();
            intent.putExtra(ImageEditor.EXTRA_EDITED_PATH, imagePath);
            setResult(Activity.RESULT_OK, intent);


        } else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            ArrayList<StoriesPickedModel> list = new ArrayList<>();

            for (Uri uri : mSelected) {
                if (uri.toString().contains("video")) {
                    StoriesPickedModel model = new StoriesPickedModel(
                            "" + uri, "" + uri, "", "video", System.currentTimeMillis()
                    );
                    list.add(model);
                } else {
                    StoriesPickedModel model = new StoriesPickedModel(
                            "" + uri, "" + uri, "", "image", System.currentTimeMillis()
                    );
                    list.add(model);
                }

            }
            if (list.size() > 0) {
                SharedPrefs.setPickedList(list);
                startActivity(new Intent(this, StoryRedirectActivity.class));

            }


//            for (Uri uri : mSelected) {
//                if (uri.toString().contains("video")) {
//                    initVideo(uri);
//                } else {
//                    CompressImage compressImage = new CompressImage(StoriesCameraActivity.this);
//                    imgs.add(compressImage.compressImage("" + uri));
//                }
//            }
//            if (imgs.size() > 0) {
//                SharedPrefs.setMultiPickedImg(imgs);
//                startActivity(new Intent(this, StoryRedirectActivity.class));
//
//            }


        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (data != null) {

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadVideoStory(String finalVideoPath) {

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.push().getKey();
        StoryModel model = new StoryModel(
                key,
                SharedPrefs.getUserModel().getName(),
                SharedPrefs.getUserModel().getUsername(),
                SharedPrefs.getUserModel().getThumbnailUrl(),
                "",
                "",
                "video/mp4",
                System.currentTimeMillis(), SharedPrefs.getUserModel().getCountryNameCode()
        );
        mDatabase.child("Stories").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                putVideo(model, finalVideoPath, mDatabase);

            }
        });
    }

    public void putVideo(StoryModel storyModel, String path, DatabaseReference mDatabase) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();


        StorageReference riversRef = mStorageRef.child("Videos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Stories").child(storyModel.getId()).child("videoUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Video Story Added");
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(StoriesCameraActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void initVideo(Uri uri) {
        try {

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
            if (f.mkdirs() || f.isDirectory())

                new StoriesCameraActivity.VideoCompressAsyncTask(this).execute("" + CommonUtils.getRealPathFromURI(uri), f.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(paths[0]);
                String width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1],
                        Integer.parseInt(width), Integer.parseInt(height), 450000);

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

            finalVideoPath = compressedFilePath;
            videoCompressed = true;
            uploadVideoStory(finalVideoPath);
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }
    }


    private int getMediaDuration(Uri uriOfFile) {
        MediaPlayer mp = MediaPlayer.create(this, uriOfFile);
        int duration = mp.getDuration();
        return duration;
    }


    private void cancelSavePicTaskIfNeed() {
        if (savePicTask != null && savePicTask.getStatus() == AsyncTask.Status.RUNNING) {
            savePicTask.cancel(true);
        }
    }

    private void cancelSaveVideoTaskIfNeed() {
        if (saveVideoTask != null && saveVideoTask.getStatus() == AsyncTask.Status.RUNNING) {
            saveVideoTask.cancel(true);
        }
    }

    private SavePicTask savePicTask;

    private class SavePicTask extends AsyncTask<Void, Void, String> {
        private byte[] data;
        private int rotation = 0;

        public SavePicTask(byte[] data, int rotation) {
            this.data = data;
            this.rotation = rotation;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                return saveToSDCard(data, rotation);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            activeCameraCapture();

            tempFile = new File(result);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    Intent mIntent = new Intent(StoriesCameraActivity.this, PhotoRedirectActivity.class);
//                    mIntent.putExtra("PATH", tempFile.toString());
//                    mIntent.putExtra("THUMB", tempFile.toString());
//                    mIntent.putExtra("WHO", "Image");
//                    startActivity(mIntent);

                    new ImageEditor.Builder(StoriesCameraActivity.this, tempFile.toString())
                            .setStickerAssets("stickers")
                            .open();
                }
            }, 100);


        }
    }

    public String saveToSDCard(byte[] data, int rotation) throws IOException {
        String imagePath = "";
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int reqHeight = metrics.heightPixels;
            int reqWidth = metrics.widthPixels;

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if (rotation != 0) {
                Matrix mat = new Matrix();
                mat.postRotate(rotation);
                Camera.CameraInfo info = new Camera.CameraInfo();

                if (flag == 0) {
                    Camera.getCameraInfo(0, info);
                } else {
                    Camera.getCameraInfo(1, info);
                }
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

                    mat.preScale(1.0f, -1.0f);
                }
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                if (bitmap != bitmap1) {
                    bitmap.recycle();
                }
                imagePath = getSavePhotoLocal(bitmap1);
                if (bitmap1 != null) {
                    bitmap1.recycle();
                }
            } else {
                imagePath = getSavePhotoLocal(bitmap);
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private String getSavePhotoLocal(Bitmap bitmap) {
        String path = "";
        try {
            OutputStream output;
            File file = new File(folder.getAbsolutePath(), "wc" + System.currentTimeMillis() + ".jpg");
            try {
                output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                output.flush();
                output.close();
                path = file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private void captureImageCallback() {

        surfaceHolder = imgSurface.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                refreshCamera();

                cancelSavePicTaskIfNeed();
                savePicTask = new SavePicTask(data, getPhotoRotation());
                savePicTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

            }
        };
    }

    private class SaveVideoTask extends AsyncTask<Void, Void, Void> {

        File thumbFilename;

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(StoriesCameraActivity.this);
            progressDialog.setMessage("Processing a video...");
            progressDialog.show();
            super.onPreExecute();
            imgCapture.setOnTouchListener(null);
            textCounter.setVisibility(View.GONE);
            imgSwipeCamera.setVisibility(View.VISIBLE);
            imgFlashOnOff.setVisibility(View.VISIBLE);
            imgFlashOnOff.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                try {
                    myOrientationEventListener.enable();

                    customHandler.removeCallbacksAndMessages(null);

                    mediaRecorder.stop();
                    releaseMediaRecorder();

                    tempFile = new File(folder.getAbsolutePath() + "/" + mediaFileName + ".mp4");
                    thumbFilename = new File(folder.getAbsolutePath(), "t_" + mediaFileName + ".jpeg");
                    generateVideoThmb(tempFile.getPath(), thumbFilename);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            if (tempFile != null && thumbFilename != null)
                onVideoSendDialog(tempFile.getAbsolutePath(), thumbFilename.getAbsolutePath());
        }

    }

    private int mPhotoAngle = 90;

    private void identifyOrientationEvents() {

        myOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int iAngle) {

                final int iLookup[] = {0, 0, 0, 90, 90, 90, 90, 90, 90, 180, 180, 180, 180, 180, 180, 270, 270, 270, 270, 270, 270, 0, 0, 0}; // 15-degree increments
                if (iAngle != ORIENTATION_UNKNOWN) {

                    int iNewOrientation = iLookup[iAngle / 15];
                    if (iOrientation != iNewOrientation) {
                        iOrientation = iNewOrientation;
                        if (iOrientation == 0) {
                            mOrientation = 90;
                        } else if (iOrientation == 270) {
                            mOrientation = 0;
                        } else if (iOrientation == 90) {
                            mOrientation = 180;
                        }

                    }
                    mPhotoAngle = normalize(iAngle);
                }
            }
        };

        if (myOrientationEventListener.canDetectOrientation()) {
            myOrientationEventListener.enable();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the pointer ID
        Camera.Parameters params = camera.getParameters();
        int action = event.getAction();


        if (event.getPointerCount() > 1) {
            // handle multi-touch events
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                mDist = getFingerSpacing(event);
            } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                camera.cancelAutoFocus();
                handleZoom(event, params);
            }
        } else {
            // handle single touch events
            if (action == MotionEvent.ACTION_UP) {
                handleFocus(event, params);
            }
        }
        return true;
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        camera.setParameters(params);
    }


    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }

    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private MediaRecorder mediaRecorder;
    private SurfaceView imgSurface;
    private ImageView imgCapture;
    private ImageView imgFlashOnOff;
    private ImageView imgSwipeCamera;
    private RunTimePermission runTimePermission;
    private TextView textCounter;
    private TextView hintTextView;

    private void initControls() {

        mediaRecorder = new MediaRecorder();

        imgSurface = (SurfaceView) findViewById(R.id.imgSurface);
        textCounter = (TextView) findViewById(R.id.textCounter);
        imgCapture = (ImageView) findViewById(R.id.imgCapture);
        imgFlashOnOff = (ImageView) findViewById(R.id.imgFlashOnOff);
        imgSwipeCamera = (ImageView) findViewById(R.id.imgChangeCamera);
        textCounter.setVisibility(View.GONE);

        hintTextView = (TextView) findViewById(R.id.hintTextView);

        imgSwipeCamera.setOnClickListener(this);
        activeCameraCapture();

        imgFlashOnOff.setOnClickListener(this);
        imgSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.showToast("Clicked");

//set camera to continually auto-focus
                Camera.Parameters params = camera.getParameters();
//*EDIT*//params.setFocusMode("continuous-picture");
//It is better to use defined constraints as opposed to String, thanks to AbdelHady
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//                params.setZoom(10);
                try {
                    camera.setParameters(params);

                } catch (Exception e) {

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelSavePicTaskIfNeed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlashOnOff:
                flashToggle();
                break;
            case R.id.imgChangeCamera:
                camera.stopPreview();
                camera.release();
                if (flag == 0) {
                    imgFlashOnOff.setVisibility(View.GONE);
                    flag = 1;
                } else {
                    imgFlashOnOff.setVisibility(View.VISIBLE);
                    flag = 0;
                }
                surfaceCreated(surfaceHolder);
                break;
            default:
                break;
        }
    }

    private void flashToggle() {

        if (flashType == 1) {

            flashType = 2;
        } else if (flashType == 2) {

            flashType = 3;
        } else if (flashType == 3) {

            flashType = 1;
        }
        refreshCamera();
    }

    private void captureImage() {
        camera.takePicture(null, null, jpegCallback);
        inActiveCameraCapture();
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = new MediaRecorder();
        }
    }


    public void refreshCamera() {

        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
            Camera.Parameters param = camera.getParameters();

            if (flag == 0) {
                if (flashType == 1) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    imgFlashOnOff.setImageResource(R.drawable.ic_flash_auto);
                } else if (flashType == 2) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    Camera.Parameters params = null;
                    if (camera != null) {
                        params = camera.getParameters();

                        if (params != null) {
                            List<String> supportedFlashModes = params.getSupportedFlashModes();

                            if (supportedFlashModes != null) {
                                if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                                    param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                                }
                            }
                        }
                    }
                    imgFlashOnOff.setImageResource(R.drawable.ic_flash_on);
                } else if (flashType == 3) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    imgFlashOnOff.setImageResource(R.drawable.ic_flash_off);
                }
            }


            refrechCameraPriview(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refrechCameraPriview(Camera.Parameters param) {
        try {
            camera.setParameters(param);
            setCameraDisplayOrientation(0);

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCameraDisplayOrientation(int cameraId) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();

        if (Build.MODEL.equalsIgnoreCase("Nexus 6") && flag == 1) {
            rotation = Surface.ROTATION_180;
        }
        int degrees = 0;
        switch (rotation) {

            case Surface.ROTATION_0:

                degrees = 0;
                break;

            case Surface.ROTATION_90:

                degrees = 90;
                break;

            case Surface.ROTATION_180:

                degrees = 180;
                break;

            case Surface.ROTATION_270:

                degrees = 270;
                break;

        }

        int result;

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror

        } else {
            result = (info.orientation - degrees + 360) % 360;

        }

        camera.setDisplayOrientation(result);

    }

    //------------------SURFACE CREATED FIRST TIME--------------------//

    int flashType = 1;

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            if (flag == 0) {
                camera = Camera.open(0);
            } else {
                camera = Camera.open(1);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        try {
            Camera.Parameters param;
            param = camera.getParameters();
            List<Camera.Size> sizes = param.getSupportedPreviewSizes();
            //get diff to get perfact preview sizes
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            long diff = (height * 1000 / width);
            long cdistance = Integer.MAX_VALUE;
            int idx = 0;
            for (int i = 0; i < sizes.size(); i++) {
                long value = (long) (sizes.get(i).width * 1000) / sizes.get(i).height;
                if (value > diff && value < cdistance) {
                    idx = i;
                    cdistance = value;
                }
                Log.e("WHHATSAPP", "width=" + sizes.get(i).width + " height=" + sizes.get(i).height);
            }
            Log.e("WHHATSAPP", "INDEX:  " + idx);
            Camera.Size cs = sizes.get(idx);
            param.setPreviewSize(cs.width, cs.height);
            param.setPictureSize(cs.width, cs.height);
            camera.setParameters(param);
            setCameraDisplayOrientation(0);

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

            if (flashType == 1) {
                param.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_auto);

            } else if (flashType == 2) {
                param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                Camera.Parameters params = null;
                if (camera != null) {
                    params = camera.getParameters();

                    if (params != null) {
                        List<String> supportedFlashModes = params.getSupportedFlashModes();

                        if (supportedFlashModes != null) {
                            if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                                param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                            }
                        }
                    }
                }
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_on);

            } else if (flashType == 3) {
                param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_off);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        try {
            camera.stopPreview();
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }

    //------------------SURFACE OVERRIDE METHIDS END--------------------//

    private long timeInMilliseconds = 0L, startTime = SystemClock.uptimeMillis(), updatedTime = 0L, timeSwapBuff = 0L;
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;

            secs = secs % 60;
            textCounter.setText(String.format("%02d", mins) + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);

        }

    };


    @Override
    protected void onPause() {
        super.onPause();

        try {

            if (customHandler != null)
                customHandler.removeCallbacksAndMessages(null);

            releaseMediaRecorder();       // if you are using MediaRecorder, release it first

            if (myOrientationEventListener != null)
                myOrientationEventListener.enable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SaveVideoTask saveVideoTask = null;

    private void activeCameraCapture() {
        if (imgCapture != null) {
            imgCapture.setAlpha(1.0f);
            imgCapture.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onLongClick(View v) {
                    hintTextView.setVisibility(View.INVISIBLE);
                    try {
                        if (prepareMediaRecorder()) {
                            myOrientationEventListener.disable();
                            mediaRecorder.start();
                            startTime = SystemClock.uptimeMillis();
                            customHandler.postDelayed(updateTimerThread, 0);
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textCounter.setVisibility(View.VISIBLE);
                    imgSwipeCamera.setVisibility(View.GONE);
                    pickGallery.setVisibility(View.GONE);
                    imgFlashOnOff.setVisibility(View.GONE);
                    imgCapture.setImageDrawable(getResources().getDrawable(R.drawable.ic_capture_record));
//                    scaleUpAnimation();
                    imgCapture.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                                return true;
                            }
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                imgCapture.setImageDrawable(getResources().getDrawable(R.drawable.ic_capture));

//                                scaleDownAnimation();
                                hintTextView.setVisibility(View.VISIBLE);
                                pickGallery.setVisibility(View.VISIBLE);
                                cancelSaveVideoTaskIfNeed();
                                saveVideoTask = new SaveVideoTask();
                                saveVideoTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                                return true;
                            }
                            return true;

                        }
                    });
                    return true;
                }

            });
            imgCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isSpaceAvailable()) {
                        captureImage();
                    } else {
                        Toast.makeText(StoriesCameraActivity.this, "Memory is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void onVideoSendDialog(final String videopath, final String thumbPath) {

        runOnUiThread(new Runnable() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void run() {
                if (videopath != null) {
                    File fileVideo = new File(videopath);
                    long fileSizeInBytes = fileVideo.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB > MAX_VIDEO_SIZE_UPLOAD) {
                        new AlertDialog.Builder(StoriesCameraActivity.this)
                                .setMessage(getString(R.string.file_limit_size_upload_format, MAX_VIDEO_SIZE_UPLOAD))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {

                        Intent mIntent = new Intent(StoriesCameraActivity.this, VideoRedirectActivity.class);
                        mIntent.putExtra("PATH", videopath.toString());
                        mIntent.putExtra("THUMB", thumbPath.toString());
                        mIntent.putExtra("WHO", "Video");
                        startActivity(mIntent);

                        //SendVideoDialog sendVideoDialog = SendVideoDialog.newInstance(videopath, thumbPath, name, phoneNuber);
                        // sendVideoDialog.show(getSupportFragmentManager(), "SendVideoDialog");
                    }
                }
            }
        });
    }

    private void inActiveCameraCapture() {
        if (imgCapture != null) {
            imgCapture.setAlpha(0.5f);
            imgCapture.setOnClickListener(null);
        }
    }

    //--------------------------CHECK FOR MEMORY -----------------------------//

    public int getFreeSpacePercantage() {
        int percantage = (int) (freeMemory() * 100 / totalMemory());
        int modValue = percantage % 5;
        return percantage - modValue;
    }

    public double totalMemory() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdAvailSize = (double) stat.getBlockCount() * (double) stat.getBlockSize();
        return sdAvailSize / 1073741824;
    }

    public double freeMemory() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdAvailSize = (double) stat.getAvailableBlocks() * (double) stat.getBlockSize();
        return sdAvailSize / 1073741824;
    }

    public boolean isSpaceAvailable() {
        if (getFreeSpacePercantage() >= 1) {
            return true;
        } else {
            return false;
        }
    }
    //-------------------END METHODS OF CHECK MEMORY--------------------------//


    private String mediaFileName = null;

    @SuppressLint("SimpleDateFormat")
    protected boolean prepareMediaRecorder() throws IOException {

        mediaRecorder = new MediaRecorder(); // Works well
        camera.stopPreview();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (flag == 1) {
            mediaRecorder.setProfile(CamcorderProfile.get(1, CamcorderProfile.QUALITY_480P));
        } else {
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        }
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        mediaRecorder.setOrientationHint(mOrientation);

        if (Build.MODEL.equalsIgnoreCase("Nexus 6") && flag == 1) {

            if (mOrientation == 90) {
                mediaRecorder.setOrientationHint(mOrientation);
            } else if (mOrientation == 180) {
                mediaRecorder.setOrientationHint(0);
            } else {
                mediaRecorder.setOrientationHint(180);
            }

        } else if (mOrientation == 90 && flag == 1) {
            mediaRecorder.setOrientationHint(270);
        } else if (flag == 1) {
            mediaRecorder.setOrientationHint(mOrientation);
        }
        mediaFileName = "wc_vid_" + System.currentTimeMillis();
        mediaRecorder.setOutputFile(folder.getAbsolutePath() + "/" + mediaFileName + ".mp4"); // Environment.getExternalStorageDirectory()

        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {

            public void onInfo(MediaRecorder mr, int what, int extra) {
                // TODO Auto-generated method stub

                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {

                    long downTime = 0;
                    long eventTime = 0;
                    float x = 0.0f;
                    float y = 0.0f;
                    int metaState = 0;
                    MotionEvent motionEvent = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_UP,
                            0,
                            0,
                            metaState
                    );

                    imgCapture.dispatchTouchEvent(motionEvent);

                    Toast.makeText(StoriesCameraActivity.this, "You reached to Maximum(25MB) video size.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mediaRecorder.setMaxFileSize(1000 * 25 * 1000);

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            releaseMediaRecorder();
            e.printStackTrace();
            return false;
        }
        return true;

    }

    OrientationEventListener myOrientationEventListener;
    int iOrientation = 0;
    int mOrientation = 90;

    public void generateVideoThmb(String srcFilePath, File destFile) {
        try {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(srcFilePath, 120);
            FileOutputStream out = new FileOutputStream(destFile);
            ThumbnailUtils.extractThumbnail(bitmap, 200, 200).compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int normalize(int degrees) {
        if (degrees > 315 || degrees <= 45) {
            return 0;
        }

        if (degrees > 45 && degrees <= 135) {
            return 90;
        }

        if (degrees > 135 && degrees <= 225) {
            return 180;
        }

        if (degrees > 225 && degrees <= 315) {
            return 270;
        }

        throw new RuntimeException("Error....");
    }

    private int getPhotoRotation() {
        int rotation;
        int orientation = mPhotoAngle;

        Camera.CameraInfo info = new Camera.CameraInfo();
        if (flag == 0) {
            Camera.getCameraInfo(0, info);
        } else {
            Camera.getCameraInfo(1, info);
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else {
            rotation = (info.orientation + orientation) % 360;
        }
        return rotation;
    }
}
