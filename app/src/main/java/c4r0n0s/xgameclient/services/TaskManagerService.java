package c4r0n0s.xgameclient.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import c4r0n0s.xgameclient.Constants;
import c4r0n0s.xgameclient.MainActivity;
import c4r0n0s.xgameclient.R;
import c4r0n0s.xgameclient.entities.AccountEntity;
import c4r0n0s.xgameclient.utils.DetectConnection;

import static c4r0n0s.xgameclient.Constants.ACTION.CHANNEL_ID_MAIN;

public class TaskManagerService extends Service {
    private static final String LOG_TAG = TaskManagerService.class.getName();
    private WebViewService webViewService;
    private Timer refreshTimer = new Timer();
    private Long defaultRefreshPeriod = 60000L;
    private Notification.Builder nBuilder;
    private XGameNotificationManager xGameNotificationManager;

    public TaskManagerService() {
    }

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
                        .setStyle(new Notification.BigTextStyle().bigText("xGame client is running..."))
                        .setColor(Color.GREEN)
                        .setTicker("I don't know what is it")
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_sync_24dp)
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
                        .setSubText(intent.getStringExtra("dateTime"))
                        .setSmallIcon(R.drawable.ic_sync_24dp)
                        .setColor(Color.GREEN)
                        .setOnlyAlertOnce(true)
                        .build();
                this.xGameNotificationManager.getManager()
                        .notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, newNotification);
            }
        } else if (Objects.equals(intent.getAction(), Constants.ACTION.NO_INTERNET)) {
            if (!Objects.isNull(nBuilder)) {
                Notification newNotification = nBuilder
                        .setStyle(new Notification.BigTextStyle().bigText("No internet connection!!!"))
                        .setSmallIcon(R.drawable.ic_sync_problem_24dp)
                        .setColor(Color.RED)
                        .setOnlyAlertOnce(false)
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
        final TaskManagerService self = this;
        AccountEntity accountSettings = AccountManagerService.getAccountSettings();
        long refreshPeriod = accountSettings != null ? accountSettings.refreshTime : defaultRefreshPeriod;
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!DetectConnection.isInternetConnectionAvailable(self)) {
                            Intent noConnectionIntent = new Intent(self, TaskManagerService.class);
                            noConnectionIntent.setAction(Constants.ACTION.NO_INTERNET);
                            Objects.requireNonNull(self).startService(noConnectionIntent);
                        } else {
                            webViewService.getWebView().loadUrl("https://xgame-online.com/uni21/overview.php");
                        }
                    }
                });
            }
        }, 0, refreshPeriod);
    }
}

