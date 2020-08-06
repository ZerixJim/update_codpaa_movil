package com.codpaa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codpaa.service.GeoLocalizar;

/*
 * Created by grim on 8/01/16.
 */
public class MyRecive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            Intent myIntent = new Intent(context, GeoLocalizar.class);
            context.startService(myIntent);

        }catch (Exception e){

            e.printStackTrace();

        }



    }
}
