package com.codpaa.listeners;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import BD.BDopenHelper;


public class ResponseTiendas extends JsonHttpResponseHandler{

    Context _context;
    private ProgressDialog pdia;
    Handler updateProgress;

    public ResponseTiendas(Context context){
        this._context = context;
        pdia = new ProgressDialog(context);
        pdia.setTitle("Tiendas");
        pdia.setMessage("Descargando Tiendas");
        pdia.setIndeterminate(false);
        pdia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdia.setCancelable(false);


        updateProgress = new Handler();
    }

    @Override
    public void onStart() {
        super.onStart();

        updateProgress.post(new Runnable() {
            @Override
            public void run() {
                pdia.show();
            }
        });

        Toast.makeText(_context,"Descargando Tiendas",Toast.LENGTH_SHORT).show();

        Log.d("RTiendas","Start");
    }

    @Override
    public void onProgress(final int bytesWritten, final int totalSize) {
        super.onProgress(bytesWritten, totalSize);

        updateProgress.post(new Runnable() {
            @Override
            public void run() {
                pdia.setMax(totalSize);
                pdia.setProgress(bytesWritten);
            }
        });



    }

    @Override
    public void onSuccess(int statusCode, JSONObject response) {
        super.onSuccess(statusCode, response);

        if (response != null){
            try {

                parseJsonTiendas(response.getJSONArray("C"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            Toast.makeText(_context,"Descarga de Tiendas Satisfactoria",Toast.LENGTH_SHORT).show();
            Log.d("RTiendas","Success");
        }
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);
        Toast.makeText(_context,"Error al descargar",Toast.LENGTH_SHORT).show();
        Log.d("RTiendas","Failure");
    }

    @Override
    public void onFinish() {
        super.onFinish();

        updateProgress.postDelayed(new Runnable() {
            @Override
            public void run() {
                pdia.dismiss();
            }
        },2000);

    }

    private void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context);
        b.vaciarTabla("clientes");
        for(int i=0; i<tiendasArray.length(); i++){
            b.insertarClientes(tiendasArray.getJSONObject(i).getInt("IT"),
                    tiendasArray.getJSONObject(i).getString("G"),
                    tiendasArray.getJSONObject(i).getString("S"),
                    tiendasArray.getJSONObject(i).getString("X"),
                    tiendasArray.getJSONObject(i).getString("Y"));
        }

    }
}
