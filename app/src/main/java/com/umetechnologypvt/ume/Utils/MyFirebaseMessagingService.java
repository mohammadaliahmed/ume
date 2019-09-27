package com.umetechnologypvt.ume.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.umetechnologypvt.ume.Activities.Home.MainActivity;
import com.umetechnologypvt.ume.Activities.SingleChattingScreen;
import com.umetechnologypvt.ume.Activities.ViewPost;
import com.umetechnologypvt.ume.ApplicationClass;
import com.umetechnologypvt.ume.FloatingChatButton.FloatingButton;
import com.umetechnologypvt.ume.Interface.NotificationInterface;
import com.umetechnologypvt.ume.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by AliAh on 01/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String msg;
    String title, message, type;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String username;
    private String Id, PictureUrl;
    private int ChannelId;
    SoundPool sp;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("message payload", "Message data payload: " + remoteMessage.getData());
            msg = "" + remoteMessage.getData();

            Map<String, String> map = remoteMessage.getData();

            message = map.get("Message");
            title = map.get("Title");
            ChannelId = Integer.parseInt(map.get("ChannelId") == null ? "" + System.currentTimeMillis() : map.get("ChannelId"));
            type = map.get("Type");
            PictureUrl = map.get("PictureUrl");
//            username = map.get("Username");
            Id = map.get("Id");

            if (type.equalsIgnoreCase("chat")) {
                if (SingleChattingScreen.activityAcitve) {

                } else {
                    int count = 0;
                    try {
                        count = Integer.parseInt((SharedPrefs.getHeadNotificationCount(Id).equalsIgnoreCase("") ? "0" : SharedPrefs.getHeadNotificationCount(Id)));
                        if (count == 0) {
                            count = 1;
                        } else {
                            count = count + 1;
                        }
                    } catch (NumberFormatException e) {
                        SharedPrefs.setHeadNotificationCount(Id, "1");
                    }

                    SharedPrefs.setHeadNotificationCount(Id, "" + count);
                    Intent it = new Intent(ApplicationClass.getInstance().getApplicationContext(), FloatingButton.class);
                    it.putExtra(Constants.EXTRA_MSG, message.length() > 30 ? message.substring(30) : message);
                    it.putExtra(Constants.IMAGE_URL, PictureUrl);
                    it.putExtra(Constants.USER_ID, Id);
                    it.putExtra(Constants.NOTIFICATION_COUNT, count);
                    startService(it);

                    RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).play();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                }

            } else {
                handleNow(title, message, type);

            }
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow(msg);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("body", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleNow(String notificationTitle, String messageBody, String type) {

        int num = (int) System.currentTimeMillis();
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = null;
        if (type.equalsIgnoreCase("chat")) {
            resultIntent = new Intent(this, SingleChattingScreen.class);
            resultIntent.putExtra("userId", Id);

        } else if (type.equalsIgnoreCase("friendRequest")) {
            if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {

                int co = Integer.parseInt(SharedPrefs.getNotificationCount());
                SharedPrefs.setNotificationCount("" + (co + 1));
            }
            sendMessage();
            resultIntent = new Intent(this, MainActivity.class);
//            resultIntent.putExtra("userId", Id);
            Constants.USER_ID =Id;
            resultIntent.putExtra("value", 2);
        } else if (type.equalsIgnoreCase("likePost")) {
            if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {

                int co = Integer.parseInt(SharedPrefs.getNotificationCount());
                SharedPrefs.setNotificationCount("" + (co + 1));
            } else {
                SharedPrefs.setNotificationCount("" + 1);
            }
            sendMessage();
            resultIntent = new Intent(this, ViewPost.class);
            resultIntent.putExtra("postId", Id);


        } else if (type.equalsIgnoreCase("commentPost")) {
            if (!SharedPrefs.getNotificationCount().equalsIgnoreCase("")) {

                int co = Integer.parseInt(SharedPrefs.getNotificationCount());
                SharedPrefs.setNotificationCount("" + (co + 1));
            } else {
                SharedPrefs.setNotificationCount("" + 1);
            }

            sendMessage();

            resultIntent = new Intent(this, ViewPost.class);
            resultIntent.putExtra("postId", Id);

        }


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(notificationTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(num /* Request Code */, mBuilder.build());
    }
}
