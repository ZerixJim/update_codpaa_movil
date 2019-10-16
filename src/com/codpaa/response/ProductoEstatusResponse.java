package com.codpaa.response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.QuickstartPreferences;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by Grim on 17/08/2017.
 */

public class ProductoEstatusResponse extends JsonHttpResponseHandler {


    private Context mContext;
    private ProgressDialog progressDialog;

    public ProductoEstatusResponse(Context mContext) {
        this.mContext = mContext;


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onFinish() {
        super.onFinish();



    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


        if (response != null){

            try {
                if(response.getBoolean("inserted")){


                    JSONArray array = response.getJSONArray("productos");


                    int lenght = array.length();

                    SQLiteDatabase db = new BDopenHelper(mContext).getWritableDatabase();


                    for (int i = 0; i < lenght ; i++){
                        db.execSQL("update " + DbEstructure.ProductoCatalogadoTienda.TABLE_NAME +   " " +
                                "set " + DbEstructure.ProductoCatalogadoTienda.ESTATUS_REGISTRO + "=2, " +
                                DbEstructure.ProductoCatalogadoTienda.ESTATUS_PRODUCTO +"="
                                + array.getJSONObject(i).getInt("estatus") + " where " +
                                DbEstructure.ProductoCatalogadoTienda.ID_PRODUCTO +  "=" +
                                array.getJSONObject(i).getInt("idProducto") +
                                " and " + DbEstructure.ProductoCatalogadoTienda.ID_TIENDA +  "=" +
                                array.getJSONObject(i).getInt("idTienda") + " and  " +
                                DbEstructure.ProductoCatalogadoTienda.FECHA_CAPTURA + "='" +
                                array.getJSONObject(i).getString("fecha") + "'");

                    }

                    Log.d("paso ssss", " ssss");




                    Intent intent = new Intent(QuickstartPreferences.SEND);

                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



                }
            } catch (JSONException e) {
                e.printStackTrace();


            }


        }



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
        super.onFailure(statusCode, headers, responseString, throwable);


    }

}
