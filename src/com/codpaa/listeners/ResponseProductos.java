package com.codpaa.listeners;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BD.BDopenHelper;


public class ResponseProductos extends JsonHttpResponseHandler{

    Context _context;



    public ResponseProductos(Context context){
        this._context = context;

    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(_context,"Descargando Productos", Toast.LENGTH_SHORT).show();

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
                parseJSONProductos(response.getJSONArray("P"));
                Toast.makeText(_context,"Descarga de Productos Satisfactoria",
                        Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);
        Toast.makeText(_context,"Error al Descargar Productos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        super.onFinish();

    }

    private void parseJSONProductos(JSONArray productosArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context);
        b.vaciarTabla("producto");

        for(int i= 0; i < productosArray.length(); i++) {

            b.insertarProducto(productosArray.getJSONObject(i).getInt("IP"),
                    productosArray.getJSONObject(i).getString("N"),
                    productosArray.getJSONObject(i).getString("P"),
                    productosArray.getJSONObject(i).getInt("IM"),
                    productosArray.getJSONObject(i).getString("CB"));
        }
    }
}
