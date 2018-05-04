package c4r0n0s.xgameclient.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class XGameServiceReceiver extends BroadcastReceiver {
    static final String receiverTag = "XGameServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(receiverTag, "onReceive");
        String measurement = intent.getStringExtra("measurement");
        Log.d(receiverTag, "measurement - 2 : " + measurement);
    }
}
