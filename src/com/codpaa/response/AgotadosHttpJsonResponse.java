package com.codpaa.response;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AgotadosHttpJsonResponse extends JsonHttpResponseHandler {

    private final Context context;
    private final int idTienda;
    private final int idProducto;
    private final String fecha;

    public AgotadosHttpJsonResponse(Context context, int idTienda, int idProducto, String fecha){

        this.context = context;
        this.idTienda = idTienda;
        this.idProducto = idProducto;
        this.fecha = fecha;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        Log.d("Agotados", "success");

        if (response != null){


            try {
                if (response.getBoolean("created")){

                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(DbEstructure.SolicitudAgodatos.STATUS_REGISTRO, 2);

                    db.update(DbEstructure.SolicitudAgodatos.TABLE_NAME,cv,
                            DbEstructure.SolicitudAgodatos.ID_TIENDA + "=" + idTienda + " and "+
                                    DbEstructure.SolicitudAgodatos.ID_PRODUCTO + "=" + idProducto + " and "+
                                    DbEstructure.SolicitudAgodatos.FECHA + "='" + fecha + "'"

                            ,null);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        if(errorResponse != null) {
            //Log.e("ERROR HTTP RESPONSE", errorResponse.toString());
        } else {
            // Log.e("ERROR HTTP RESPONSE", "ErrorResponse is null");
        }
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
