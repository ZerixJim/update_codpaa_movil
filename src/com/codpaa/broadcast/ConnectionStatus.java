package com.codpaa.broadcast;/*
 * Created by grim on 18/01/16.
 */

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.util.NetWorkUtil;

public class ConnectionStatus extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetWorkUtil.getConnectionStatus(context);

        RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.menu);

        remote.setTextViewText(R.id.txtconexion, status);
        ComponentName cn = new ComponentName(context, TextView.class);
        AppWidgetManager.getInstance(context).updateAppWidget(cn, remote);

        Log.d("ConnectionStatus", status);
    }
}
