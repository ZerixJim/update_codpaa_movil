package com.codpaa.response;/*
 * Created by grim on 09/08/2016.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import com.codpaa.provider.DbEstructure.Mensaje;

import cz.msebera.android.httpclient.Header;

public class MensajeHttpResponse extends JsonHttpResponseHandler{


    private Context context;
    private int idMensaje;
    SQLiteDatabase db;

    public MensajeHttpResponse(Context context, int idMensaje){

        this.context = context;
        this.idMensaje = idMensaje;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        Log.d("MensajeHTTP" , "success");

        if (response != null){
            try {

                if (response.getBoolean("insert")){
                    Log.d("MensajeHTTP" , "success true");
                    Toast.makeText(context, "Acuse Recibido", Toast.LENGTH_SHORT).show();
                    db = new BDopenHelper(context).getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Mensaje.ENVIADO, 1);

                    db.update(Mensaje.TABLE_NAME,contentValues, Mensaje.ID_MENSAJE+"="+idMensaje, null);

                    db.close();

                    try {
                        ((Activity)context).finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    Log.d("MensajeHTTP" , "success false");


                    try {
                        ((Activity)context).finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }


    @Override
    public void onFinish() {
        super.onFinish();




    }
}
