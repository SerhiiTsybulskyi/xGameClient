package c4r0n0s.xgameclient.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Objects;

public class ServiceTools {
    public static boolean isServiceRunning(Context context, Class<?> serviceClass){
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = Objects.requireNonNull(activityManager).getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }
}