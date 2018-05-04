package c4r0n0s.xgameclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class DetectConnection {
    public static boolean isInternetConnectionAvailable(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }
}
