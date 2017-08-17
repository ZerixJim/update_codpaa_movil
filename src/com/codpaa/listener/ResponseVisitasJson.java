package com.codpaa.listener;

/*
 * Created by grim on 01/08/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ResponseVisitasJson extends JsonHttpResponseHandler{

    private Context context;


    public ResponseVisitasJson(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        if (response != null){

            try {
                if (response.getBoolean("inserted")){

                    SQLiteDatabase base = new BDopenHelper(context).getWritableDatabase();


                    JSONArray array = response.getJSONArray("visitas");

                    int lenght = array.length();

                    for (int i = 0; i < lenght ; i++){

                        base.execSQL("UPDATE coordenadas SET status=2 WHERE " +
                                "idTienda="+ array.getJSONObject(i).getInt("idTienda") +" and fecha='"+
                                array.getJSONObject(i).getString("fechaCaptura")+"' " +
                                "and tipo='"+array.getJSONObject(i).getString("tipo")+"';");

                    }



                    Toast.makeText(context,"Visita Recibida",Toast.LENGTH_SHORT).show();

                    /*if (lenght == 1){

                        Toast.makeText(context,"Visita Recibida",Toast.LENGTH_SHORT).show();

                    }else if (lenght > 1){
                        Toast.makeText(context,"Visitas Recibidas",Toast.LENGTH_SHORT).show();
                    }*/


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        //Log.d("error", responseString);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        //Log.d("success", responseString);
    }
}
