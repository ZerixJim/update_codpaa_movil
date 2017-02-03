package com.codpaa.response;


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
 * Created by grim on 2/02/17.
 */

public class MaterialesJsonResponse extends JsonHttpResponseHandler {

    private Context context;
    private ProgressDialog progressDialog;

    public MaterialesJsonResponse(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Enviando Material");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            Log.d("JsonResponse", response.toString());
            try {
                if (response.getBoolean("insert")){

                    JSONArray array = response.getJSONArray("inserted");

                    int lenght = array.length();

                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();

                    for (int i = 0; i < lenght ; i++){
                        db.execSQL("update materiales_solicitud set estatus=2 where id_material=" + array.getJSONObject(i).getInt("idMaterial") +
                                " and id_tienda=" + array.getJSONObject(i).getInt("idTienda") + " and  " +
                                "fecha='" + array.getJSONObject(i).getString("fecha") + "' and cantidad=" + array.getJSONObject(i).getInt("cantidad") + " and " +
                                " id_producto=" + array.getJSONObject(i).getInt("idProducto"));

                    }

                    progressDialog.setMessage("Materiales Recibidos");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();

                        }
                    }, 2000);

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
        Toast.makeText(context, "ocurrio un error al enviar los materiales", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressDialog.show();
    }
}
