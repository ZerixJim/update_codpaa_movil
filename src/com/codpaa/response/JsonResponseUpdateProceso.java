package com.codpaa.response;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.codpaa.activity.impulsor.ProcesoAceptacion;
import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 23/05/2017.
 */

public class JsonResponseUpdateProceso extends JsonHttpResponseHandler {

    private Context context;

    public JsonResponseUpdateProceso(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){

            try {
                if (response.getBoolean("update")){

                    JSONArray jsonArray = response.getJSONArray("productos");

                    int length = jsonArray.length();

                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();


                    for (int i=0; i< length; i++){
                        ContentValues cv = new ContentValues();
                        cv.put("estatus_registro", 4);

                        db.update(DbEstructure.ProductoCatalogadoTienda.TABLE_NAME, cv, "" +
                                DbEstructure.ProductoCatalogadoTienda.ID_PRODUCTO + "=" + jsonArray.getJSONObject(i).getInt("idProducto") + " and " +
                                DbEstructure.ProductoCatalogadoTienda.ID_TIENDA + "=" + jsonArray.getJSONObject(i).getInt("idTienda") + " and " +
                                DbEstructure.ProductoCatalogadoTienda.FECHA_CAPTURA +"='" + jsonArray.getJSONObject(i).getString("fecha") + "'", null);


                    }



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
