package com.codpaa.broadcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.codpaa.service.GeoLocalizar;
import com.codpaa.service.JobService;

/*
 * Created by grim on 8/01/16.
 */
public class MyRecive extends BroadcastReceiver {

    private final String TAG = MyRecive.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

/*
        ComponentName serviceComponent = new ComponentName(context, JobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();


        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());*/


       /* try{
            Intent myIntent = new Intent(context, GeoLocalizar.class);


            context.startService(myIntent);
            *//*Log.d(TAG, "onRecive boot completed");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1  ){

                context.startForegroundService(myIntent);
            }else {

                context.startService(myIntent);

            }*//*


        }catch (Exception e){

            e.printStackTrace();

        }*/



    }
}
