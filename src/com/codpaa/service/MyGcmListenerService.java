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
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codpaa.R;
import com.codpaa.activity.MessaginActivity;
import com.codpaa.db.BDopenHelper;
import com.codpaa.util.Utilities;
import com.google.android.gms.gcm.GcmListenerService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String asunto = data.getString("asunto");
        String message = data.getString("message");
        String content = data.getString("content");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Content: " + content);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String fecha = dFecha.format(c.getTime());


        SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("mensaje", message);
        values.put("asunto", asunto);
        values.put("content", content);
        values.put("fecha", fecha);

        int idMensaje = (int) db.insert(Utilities.TABLE_MENSAJE, null, values);


        db.close();


        /*

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }*/

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(data, idMensaje);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param data GCM message received.
     */
    private void sendNotification(Bundle data, int idMensaje) {

        String asunto = data.getString("asunto");
        String message = data.getString("message");
        String content = data.getString("content");

        Intent intent = new Intent(this, MessaginActivity.class);

        intent.putExtra("content", content);
        intent.putExtra("idMensaje", idMensaje);

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
