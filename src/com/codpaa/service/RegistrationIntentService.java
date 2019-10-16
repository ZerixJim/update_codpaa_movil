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

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;


import com.codpaa.db.BDopenHelper;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.util.Utilities;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    private int idPromotor;

    public RegistrationIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        this.idPromotor = intent.getIntExtra("idPromo", 0);

        try {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String deviceToken = instanceIdResult.getToken();

                    sendRegistrationToServer(deviceToken);

                    Log.i(TAG, "GCM Registration Token: " + deviceToken);

                    try {
                        subscribeTopics();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });



        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false)
                    .apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        if (idPromotor > 0){

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean enviado = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

            Log.d(TAG, QuickstartPreferences.SENT_TOKEN_TO_SERVER + " " + enviado);

            if (!enviado){

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams rp = new RequestParams();
                rp.put("id", idPromotor);
                rp.put("solicitud", "update_token");
                rp.put("token_gcm", token);

                client.get(Utilities.WEB_SERVICE_CODPAA + "serv.php", rp, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(TAG, "token send to server");

                        if (response != null) {
                            try {
                                if (response.getInt("success") == 1) {
                                    sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                                    Log.d(TAG, "success");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();

                        Log.d(TAG, "onFailure");


                    }


                });
            }




        }





    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @throws IOException if unable to reach the GCM PubSub service
     */

    private void subscribeTopics() throws IOException {

        //SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        //Cursor c = db.rawQuery("select nombre from marca", null);
        int idPromotor = new BDopenHelper(this).getIdPromotor();

        FirebaseMessaging messaging = FirebaseMessaging.getInstance();

        /*for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            String marca = c.getString(c.getColumnIndex("nombre"));


            messaging.subscribeToTopic(marca.replace(" ", ""));
        }*/

        //messaging.subscribeToTopic("van-promotor");

        if (idPromotor == 1000){

            messaging.subscribeToTopic("test-1");
        }

        if (idPromotor != 0) {
            messaging.subscribeToTopic("promotor-" + idPromotor);
            //messaging.subscribeToTopic("ruta-" + idPromotor);
            //messaging.subscribeToTopic("user-" + idPromotor);
            //messaging.subscribeToTopic("producto-" + idPromotor);
        }
    }




}
