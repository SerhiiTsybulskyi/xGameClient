package c4r0n0s.xgameclient.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import c4r0n0s.xgameclient.Constants;

public class WebViewService {
    private WebView webView;
    private Context context;

    @SuppressLint("SetJavaScriptEnabled")
    private WebViewService(final Context context) {
        this.context = context;
        final XGameNotificationManager xGameNotificationManager = new XGameNotificationManager(context);
        this.webView = new WebView(context);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, final String url) {
                if (url.equals("https://xgame-online.com/")) {
                    view.evaluateJavascript(loadScripts("login"), null);
                } else if (url.equals("https://xgame-online.com/uni21/overview.php")) {
                    Log.i("overview", "PAGE LOADED");
                    view.evaluateJavascript(loadScripts("check_attack"), new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            value = value.substring(1, value.length() - 1)
                                    .replace("\\\\", "\\")
                                    .replace("\\\"", "\"");
                            if (!value.isEmpty()) {
                                Notification.Builder notification = xGameNotificationManager.getChannelNotification("xGame Attack", value);
                                xGameNotificationManager.getManager().notify(102, notification.build());
                            }


                            Intent taskManagerServiceIntent = new Intent(context, TaskManagerService.class);
                            taskManagerServiceIntent.setAction(Constants.ACTION.UPDATE_MAIN_NOTIFICATION);
                            String currentDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date());
                            taskManagerServiceIntent.putExtra("dateTime", currentDateTime);
                            Objects.requireNonNull(context).startService(taskManagerServiceIntent);
                        }
                    });
                }
            }
        });
    }

    public static WebViewService newInstance(Context context) {
        return new WebViewService(context);
    }

    public WebView getWebView() {
        return webView;
    }

    private String loadScripts(String fileName) {
        Resources res = this.context.getResources();
        InputStream inputStream = res.openRawResource(
                res.getIdentifier(fileName, "raw", this.context.getPackageName()));
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bytes);
    }
}
