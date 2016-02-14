package hu.zhu.example.mortarflowsetup.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import hu.zhu.example.mortarflowsetup.application.CustomApplication;

/**
 * Created by Zhuinden on 2015.06.28..
 */
public class NetworkChecker {
    public static boolean isWifiOrMobileActive() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    public static boolean isWifiActive() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    public static boolean isMobileActive() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }
}

