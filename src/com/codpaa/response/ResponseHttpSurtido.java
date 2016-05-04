package com.codpaa.response;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 03/05/2016.
 */
public class ResponseHttpSurtido extends JsonHttpResponseHandler {

    private int idTienda, idProducto;
    private String fecha;
    private Context context;
    private SQLiteDatabase db;

    public ResponseHttpSurtido(Context context, int idTienda, String fecha, int idProducto){
        this.context = context;
        this.fecha = fecha;
        this.idProducto = idProducto;
        this.idTienda = idTienda;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        if (response != null){
            try {
                if (response.getBoolean("insert")){
                    db = new BDopenHelper(context).getWritableDatabase();
                    db.execSQL("delete from surtido where idTienda="+idTienda+" and fecha='"+fecha+"' and idProducto="+idProducto+";");
                    Log.d("ResponseSurtido", "eliminado");
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


}
