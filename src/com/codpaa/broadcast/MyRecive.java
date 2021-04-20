package com.codpaa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.codpaa.service.GeoLocalizar;

/*
 * Created by grim on 8/01/16.
 */
public class MyRecive extends BroadcastReceiver {

    private final String TAG = MyRecive.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            Intent myIntent = new Intent(context, GeoLocalizar.class);


            Log.d(TAG, "onRecive boot completed");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1  ){

                context.startForegroundService(myIntent);
            }else {

                context.startService(myIntent);

            }


        }catch (Exception e){

            e.printStackTrace();

        }



    }
}
