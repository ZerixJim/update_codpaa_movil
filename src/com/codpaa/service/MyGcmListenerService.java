/*
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


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.codpaa.R;
import com.codpaa.activity.MessaginActivity;
import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.Configuracion;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.util.Utilities;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MyGcmListenerService extends FirebaseMessagingService{

    private static final String TAG = MyGcmListenerService.class.getSimpleName();


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("NEW TOKEN", s);


        sendRegistrationToServer(s);

    }


    private void sendRegistrationToServer(final String token) {

        BDopenHelper db = new BDopenHelper(getApplicationContext());
        int idPromotor = db.getIdPromotor();

        Log.w(TAG, "idPromo" + idPromotor);

        // Add custom implementation, as needed.
        if (idPromotor > 0){

            final Configuracion conf = new Configuracion(this);

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams rp = new RequestParams();
            rp.put("id", idPromotor);
            rp.put("solicitud", "update_token");
            rp.put("token_gcm", token);

            client.get(Utilities.WEB_SERVICE_CODPAA + "serv.php", rp, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d(TAG, "token send to server "+ token);

                    if (response != null) {
                        try {
                            if (response.getInt("success") == 1) {

                                conf.setToken(token);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();

                    Log.d(TAG, "onFailure");


                }


            });






        }



    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        try{
            Map<String, String> data = remoteMessage.getData();

            //JSONObject json =

            int idPromotor = new BDopenHelper(getApplicationContext()).getIdPromotor();

            String tipo = data.get("tipo");

            if (tipo.equals("mensaje")){
                mensaje(data, remoteMessage.getFrom(), idPromotor);
            } else if(tipo.equals("baja")){

                 setBajaPromotor(data);

            } /*else if (tipo.equals("cambio_estatus_producto")){


            }*/


            Log.d("MEssage", data.get("tipo"));




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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "my_chanel_id_01")
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


    private void mensaje(Map<String, String> data, String from, int idPromotor){



        String asunto = data.get("asunto");
        String message = data.get("message");
        String content = data.get("content");
        String fecha = data.get("fecha");
        int idServer = Integer.parseInt(data.get("id_mensaje"));


        SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("mensaje", message);
        values.put("asunto", asunto);
        values.put("content", content);
        values.put("fecha", fecha);
        values.put("from", from);
        values.put(DbEstructure.Mensaje.ID_PROMOTOR, idPromotor);
        values.put(DbEstructure.Mensaje.ID_SERVIDOR, idServer);

        int idMensaje = (int) db.insert(Utilities.TABLE_MENSAJE, null, values);


        db.close();



        sendNotification(data, idMensaje, idServer);


        Intent newMessage = new Intent(QuickstartPreferences.NEW_MESSAGE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(newMessage);

    }

    private void setBajaPromotor(Map<String, String> data) {


        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);




        Log.d("Promotor" , "Baja");

        int idPromotor = Integer.parseInt(data.get("idPromotor"));
        SQLiteDatabase db = new BDopenHelper(getApplicationContext()).getWritableDatabase();

        db.execSQL("update " + DbEstructure.Usuario.TABLE_NAME + " set " + DbEstructure.Usuario.STATUS + "='b' " +
                "where " + DbEstructure.Usuario.ID_USER + "=" + idPromotor);


        AccountManager accountManager  = (AccountManager) getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE);


        Account account = new Account( getResources().getString(R.string.app_name) , "com.codpaa");


        if (accountManager.getPassword(account) != null ){
            accountManager.removeAccountExplicitly(account);
        }



        localBroadcastManager.sendBroadcast(new Intent("com.codpaa.action.close"));


        db.close();


    }




}
