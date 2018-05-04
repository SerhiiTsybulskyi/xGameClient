package c4r0n0s.xgameclient.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import c4r0n0s.xgameclient.R;

import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_ID_MAIN;
import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_ID_NOTIFICATION;
import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_NAME_MAIN;
import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_NAME_NOTIFICATION;

public class XGameNotificationManager {
    private android.app.NotificationManager mManager;
    private Context context;

    public XGameNotificationManager(Context base) {
        this.context = base;
        createMainChannel();
        createNotificationChannel();
    }

    public Notification.Builder getChannelNotification(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this.context, CHANNEL_ID_NOTIFICATION)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);
        }

        return null;
    }

    public android.app.NotificationManager getManager() {
        if (mManager == null) {
            mManager = (android.app.NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    private void createMainChannel() {
        Uri defaultNotificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_MAIN, CHANNEL_NAME_MAIN,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(defaultNotificationUri,
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());

            getManager().createNotificationChannel(channel);
        }
    }

    private void createNotificationChannel() {
        Uri notificationSound = Uri.parse("android.resource://" +
                this.context.getApplicationContext().getPackageName() + "/" + R.raw.best_wake_up_sound);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NOTIFICATION,
                    CHANNEL_NAME_NOTIFICATION, android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(notificationSound,
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());

            getManager().createNotificationChannel(channel);
        }
    }
}
