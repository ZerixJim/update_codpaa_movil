package com.codpaa.service;

import android.app.job.JobParameters;
import android.util.Log;

public class JobService extends android.app.job.JobService {


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Log.i("JobService", "start");

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.i("JobService", "stop");
        return true;
    }
}
