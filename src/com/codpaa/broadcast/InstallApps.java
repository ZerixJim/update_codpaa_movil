package com.codpaa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.codpaa.db.BDopenHelper;
import com.codpaa.util.AndroidApps;

/*
 * Created by grim on 14/08/2017.
 */

public class InstallApps extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {


        if (getUser(context) > 0){

            AndroidApps app = new AndroidApps(context, getUser(context));

            app.sentSingleApp(intent.getData().getEncodedSchemeSpecificPart());
        }



    }

    private int getUser(Context context){

        SQLiteDatabase db = new BDopenHelper(context).getReadableDatabase();

        int id = 0;
        Cursor c = db.rawQuery("select idCelular from usuarios ", null);

        if (c.getCount() > 0){

            c.moveToFirst();

            id = c.getInt(c.getColumnIndex("idCelular"));

            c.close();


            return id;
        }

        c.close();

        return id;

    }




}
