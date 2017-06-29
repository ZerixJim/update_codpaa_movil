package com.codpaa.response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.QuickstartPreferences;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by Grim on 21/05/2017.
 */

public class ProductoCatalogoResponse extends JsonHttpResponseHandler {


    private Context mContext;
    private ProgressDialog progressDialog;

    public ProductoCatalogoResponse(Context mContext) {
        this.mContext = mContext;

        this.progressDialog = new ProgressDialog(mContext);
        this.progressDialog.setMessage("Enviando...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMax(100);

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

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);


        Handler handler = new Handler();

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

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 2500);

                    Toast.makeText(mContext, "informacion enviada ", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(QuickstartPreferences.SEND);

                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



                }
            } catch (JSONException e) {
                e.printStackTrace();

                progressDialog.dismiss();

            }


        }



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Toast.makeText(mContext, "error al enviar ", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Toast.makeText(mContext, "error al enviar ", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);

        Toast.makeText(mContext, "error al enviar ", Toast.LENGTH_SHORT).show();

        progressDialog.dismiss();

    }
}
