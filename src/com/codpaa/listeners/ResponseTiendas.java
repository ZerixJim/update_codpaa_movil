package com.codpaa.listeners;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import BD.BDopenHelper;


public class ResponseTiendas extends JsonHttpResponseHandler{

    Context _context;


    public ResponseTiendas(Context context){
        this._context = context;

    }

    @Override
    public void onStart() {
        super.onStart();


        //Toast.makeText(_context.getApplicationContext(),"Descargando Tiendas",Toast.LENGTH_SHORT).show();

        Log.d("RTiendas","Start");
    }

    @Override
    public void onProgress(final int bytesWritten, final int totalSize) {
        super.onProgress(bytesWritten, totalSize);


    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {

                parseJsonTiendas(response.getJSONArray("C"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            Toast.makeText(_context.getApplicationContext(),"Descarga de Tiendas Satisfactoria",Toast.LENGTH_SHORT).show();
            Log.d("RTiendas","Success");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, e, errorResponse);
        Toast.makeText(_context.getApplicationContext(),"Error al descargar",Toast.LENGTH_SHORT).show();
        Log.d("RTiendas","Failure");
    }

    @Override
    public void onFinish() {
        super.onFinish();


    }

    private void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
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
