package com.codpaa.response;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.QuickstartPreferences;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by Grim on 22/05/2017.
 */

public class ResponseUpdateFirmaProducto extends JsonHttpResponseHandler {

    private Context context;

    public ResponseUpdateFirmaProducto(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){

            try {
                if (response.getBoolean("update")){

                    JSONArray array = response.getJSONArray("productos");

                    int lenght = array.length();

                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();


                    for (int i = 0; i < lenght ; i++){

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DbEstructure.ProductoCatalogadoTienda.ESTATUS_REGISTRO, 3);

                        db.update(DbEstructure.ProductoCatalogadoTienda.TABLE_NAME, contentValues,"" +
                                DbEstructure.ProductoCatalogadoTienda.ID_PRODUCTO + "=" + array.getJSONObject(i).getInt("idProducto") + " and " +
                                DbEstructure.ProductoCatalogadoTienda.ID_TIENDA + "=" + array.getJSONObject(i).getInt("idTienda") + " and " +
                                DbEstructure.ProductoCatalogadoTienda.FECHA_CAPTURA +"='" + array.getJSONObject(i).getString("fecha") + "'",null);




                    }

                    Intent intent = new Intent(QuickstartPreferences.SEND);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);




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
        super.onFailure(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
    }
}
