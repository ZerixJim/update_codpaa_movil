package com.codpaa.listeners;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import BD.BDopenHelper;


public class ResponseRuta extends JsonHttpResponseHandler {

    Context _context;

    //private ProgressDialog pdia;

    public ResponseRuta(Context context){
        this._context = context;
        //pdia = new ProgressDialog(context);
       /* pdia.setTitle("Ruta");
        pdia.setMessage("Descargando Ruta");
        pdia.setIndeterminate(false);
        pdia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/


    }

    @Override
    public void onStart() {
        super.onStart();
       // pdia.show();

        Toast.makeText(_context,"Descargando Ruta",Toast.LENGTH_SHORT).show();
        Log.d("RRuta","iniciado");
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
        //pdia.setMax(totalSize);
        //pdia.setProgress(bytesWritten);
    }

    @Override
    public void onSuccess(int statusCode, JSONObject response) {
        super.onSuccess(statusCode, response);

        if (response != null){
            try {
                parseJSONRuta(response.getJSONArray("V"));
            }catch (JSONException e){
                e.printStackTrace();
            }

            Toast.makeText(_context,"Descarga Satisfactoria de Ruta",Toast.LENGTH_SHORT).show();
            Log.d("RRuta","success");
        }
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);

        Toast.makeText(_context,"Error al descargar",Toast.LENGTH_SHORT).show();
        Log.d("RRuta","failure");

    }

    @Override
    public void onFinish() {
        super.onFinish();

        Log.d("RRuta","finish");
    }

    private void parseJSONRuta(JSONArray rutasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context);
        b.vaciarTabla("visitaTienda");

        for(int i= 0; i < rutasArray.length(); i++) {

            b.insertarRutaVisitas(rutasArray.getJSONObject(i).getInt("IT"),
                    rutasArray.getJSONObject(i).getInt("lu"),
                    rutasArray.getJSONObject(i).getInt("ma"),
                    rutasArray.getJSONObject(i).getInt("mi"),
                    rutasArray.getJSONObject(i).getInt("ju"),
                    rutasArray.getJSONObject(i).getInt("vi"),
                    rutasArray.getJSONObject(i).getInt("sa"),
                    rutasArray.getJSONObject(i).getInt("do"),
                    rutasArray.getJSONObject(i).getInt("IP"),
                    rutasArray.getJSONObject(i).getString("R"));

        }

    }
}
