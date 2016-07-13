/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codpaa.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.codpaa.R;
import com.codpaa.activity.MessaginActivity;
import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.util.Utilities;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class MyGcmListenerService extends FirebaseMessagingService{

    private static final String TAG = "MyGcmListenerService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try{
            Map<String, String> data = remoteMessage.getData();

            //JSONObject json =

            String asunto = data.get("asunto");
            String message = data.get("message");
            String content = data.get("content");
            int idServer = Integer.valueOf(data.get("id_mensaje"));

            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "Content: " + content);
            Log.d(TAG, "ID SERVER: " + idServer );

            Calendar c = Calendar.getInstance();
            SimpleDateFormat dFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String fecha = dFecha.format(c.getTime());


            SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("mensaje", message);
            values.put("asunto", asunto);
            values.put("content", content);
            values.put("fecha", fecha);
            values.put(DbEstructure.Mensaje.ID_SERVIDOR, idServer);

            int idMensaje = (int) db.insert(Utilities.TABLE_MENSAJE, null, values);


            db.close();



            sendNotification(data, idMensaje, idServer);


            Intent newMessage = new Intent(QuickstartPreferences.NEW_MESSAGE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(newMessage);
        }catch (Exception e){
            e.printStackTrace();
        }




        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param data GCM message received.
     */
    private void sendNotification(Map<String, String> data, int idMensaje, int idServer) {

        String asunto = data.get("asunto");
        String message = data.get("message");
        String content = data.get("content");

        Intent intent = new Intent(this, MessaginActivity.class);

        intent.putExtra("content", content);
        intent.putExtra("idMensaje", idMensaje);
        intent.putExtra("idServidor", idServer);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(asunto)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }



}
