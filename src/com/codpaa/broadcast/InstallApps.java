package com.codpaa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.codpaa.util.AndroidApps;

/*
 * Created by grim on 14/08/2017.
 */

public class InstallApps extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        //Log.d("install-app", intent.getData().getEncodedSchemeSpecificPart());

        //// TODO: 14/08/2017 get real id

        AndroidApps app = new AndroidApps(context, 1000);

        app.sentSingleApp(intent.getData().getEncodedSchemeSpecificPart());



    }
}
