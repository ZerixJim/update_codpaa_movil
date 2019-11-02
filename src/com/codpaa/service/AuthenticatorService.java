package com.codpaa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.codpaa.util.MyAuthenticator;

public class AuthenticatorService extends Service {


    private MyAuthenticator myAuthenticator;


    @Override
    public void onCreate() {

        myAuthenticator = new MyAuthenticator(this);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myAuthenticator.getIBinder();
    }
}
