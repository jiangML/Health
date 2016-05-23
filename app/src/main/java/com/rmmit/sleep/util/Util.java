package com.rmmit.sleep.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/4/27.
 */
public class Util {

    /**
     * 判断有无网络
     * @param act
     * @return
     */
    public static  boolean hasNetWork(Activity act)
    {
        ConnectivityManager manager = (ConnectivityManager) act .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

}
