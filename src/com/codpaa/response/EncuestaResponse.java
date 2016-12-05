package com.codpaa.response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by Gus on 21/11/2016.
 */

public class EncuestaResponse extends JsonHttpResponseHandler {

    private Context context;
    private ProgressDialog progressDialog;


    public EncuestaResponse(Context context){
        this.context = context;

        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Enviando Encuesta");
        progressDialog.setCancelable(false);
    }



    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {
                if (response.getBoolean("insert")){

                    //Log.d("json", response.toString());



                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();
                    JSONArray jsonArray = response.getJSONArray("insertados");

                    //Log.d("json array", jsonArray.toString());

                    for ( int i = 0; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Log.d("json value", "" + jsonObject.getInt("idEncuesta"));

                        db.execSQL("update encuesta_respuestas set estatus=2 where idEncuesta="+jsonObject.getInt("idEncuesta")+" " +
                                "and idTienda="+jsonObject.getInt("idTienda")+" and idPregunta="+ jsonObject.getInt("idPregunta"));


                    }

                    progressDialog.setMessage("Encuesta Enviada Gracias!!!");


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();

                            ((Activity)context).finish();


                        }
                    }, 2000);


                    Toast.makeText(context, "Encuesta Recibida", Toast.LENGTH_SHORT).show();
                    db.close();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        Toast.makeText(context, "ocurrio un error al enviar la encuesta", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressDialog.show();

    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
