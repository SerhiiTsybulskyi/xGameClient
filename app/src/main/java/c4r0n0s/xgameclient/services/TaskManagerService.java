package c4r0n0s.xgameclient.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import c4r0n0s.xgameclient.Constants;
import c4r0n0s.xgameclient.MainActivity;
import c4r0n0s.xgameclient.R;

import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_ID_MAIN;

public class TaskManagerService extends Service {
    private static final String LOG_TAG = TaskManagerService.class.getName();
    private WebViewService webViewService;
    private Timer refreshTimer = new Timer();
    private Notification.Builder nBuilder;
    private XGameNotificationManager xGameNotificationManager;

    public TaskManagerService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        this.webViewService = WebViewService.newInstance(this);
        this.reload(this.webViewService);
        this.xGameNotificationManager = new XGameNotificationManager(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Objects.equals(intent.getAction(), Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Intent previousIntent = new Intent(this, TaskManagerService.class);
            previousIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            PendingIntent pStopIntent = PendingIntent.getService(this, 0,
                    previousIntent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Action stopAction = new Notification.Action.Builder(
                        Icon.createWithResource(this, R.drawable.ic_stop_black_24dp),
                        "Stop",
                        pStopIntent).build();

                nBuilder = new Notification.Builder(this, CHANNEL_ID_MAIN)
                        .setContentTitle("xGame client")
                        .setTicker("Truiton Music Player")
                        .setContentText("xGame client is running")
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_sync_black_24dp)
                        .addAction(stopAction);

                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, nBuilder.build());
            }
        } else if (Objects.equals(intent.getAction(), Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        } else if (Objects.equals(intent.getAction(), Constants.ACTION.UPDATE_MAIN_NOTIFICATION)) {
            if (!Objects.isNull(nBuilder)) {
                Notification newNotification = nBuilder
                        .setContentText("xGame client is running... Last update: "
                                + intent.getStringExtra("dateTime"))
                        .build();
                this.xGameNotificationManager.getManager()
                        .notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, newNotification);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshTimer.cancel();
        webViewService.getWebView().destroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    public void reload(final WebViewService webViewService) {
        final Handler handler = new Handler();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        webViewService.getWebView().loadUrl("https://xgame-online.com/uni21/overview.php");
                    }
                });
            }
        }, 0, 300000);
    }
}

