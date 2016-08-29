package com.salvin.mayatest.helpers;

/**
 * Created by Aliva Salvin on 6/23/2015.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectionDetector {

    private Context _context;

    public InternetConnectionDetector(Context context) {
        this._context = context;
    }


    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        } else if (mWifi.isConnected()) return true;

        return false;
    }
}