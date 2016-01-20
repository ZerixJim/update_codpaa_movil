package com.codpaa.util;
/*
 * Created by grim on 18/01/16.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

    public final static int TYPE_WIFI = 1;
    public final static int TYPE_MOBIL = 2;
    public final static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectionType(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nI = cm.getActiveNetworkInfo();

        if (nI != null){
            if (nI.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (nI.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBIL;

        }

        return TYPE_NOT_CONNECTED;

    }

    public static String getConnectionStatus(Context context){
        int con = getConnectionType(context);
        String status = null;
        if (con == NetWorkUtil.TYPE_WIFI){
            status = "wifi";

        }else if (con == NetWorkUtil.TYPE_MOBIL){
            status = "datos moviles";
        }else if (con == NetWorkUtil.TYPE_NOT_CONNECTED){
            status = "sin conexion";
        }

        return status;
    }

}
