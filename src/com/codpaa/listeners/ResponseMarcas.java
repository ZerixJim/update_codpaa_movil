package com.codpaa.listeners;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BD.BDopenHelper;


public class ResponseMarcas extends JsonHttpResponseHandler{



    Context _context;



    public ResponseMarcas(Context context){
        this._context = context;

    }

    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(_context.getApplicationContext(),"Descargando Marcas",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);

    }

    @Override
    public void onSuccess(int statusCode, JSONObject response) {
        super.onSuccess(statusCode, response);

        if (response != null){
            try {
                parseJSONMarca(response.getJSONArray("M"));
                Toast.makeText(_context.getApplicationContext(),"Descarga de Marcas Satisfactoria",
                        Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);
        Toast.makeText(_context.getApplicationContext(),"Error al descargar Marcas",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        super.onFinish();

    }

    private void parseJSONMarca(JSONArray marcaArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
        b.vaciarTabla("marca");

        for(int i= 0; i < marcaArray.length(); i++) {

            b.insertarMarca(marcaArray.getJSONObject(i).getInt("IM"),
                    marcaArray.getJSONObject(i).getString("N"));
        }
    }
}
