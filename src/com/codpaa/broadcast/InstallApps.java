package com.codpaa.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.codpaa.db.BDopenHelper;
import com.codpaa.util.AndroidApps;

/*
 * Created by grim on 14/08/2017.
 */

public class InstallApps extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {


        Log.w("receive ", intent.getAction());


        if (getUser(context) > 0){

            Log.w("Action App", intent.getAction());

            //AndroidApps app = new AndroidApps(context, getUser(context));

            String action = intent.getAction();
            String actionString = "";

            if (action.equals(Intent.ACTION_PACKAGE_FULLY_REMOVED) || action.equals(Intent.ACTION_PACKAGE_REMOVED)){

                actionString = "uninstall";

            }else if(action.equals(Intent.ACTION_INSTALL_PACKAGE)){

                actionString = "install";

            }


            //app.sentSingleApp(intent.getData().getEncodedSchemeSpecificPart(), actionString);
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
