package com.codpaa.listeners;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.db.BDopenHelper;


public class ResponseProductos extends JsonHttpResponseHandler{

    Context _context;



    public ResponseProductos(Context context){
        this._context = context;

    }

    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(_context.getApplicationContext(),"Descargando Productos", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers,response);
        if (response != null){
            try {
                parseJSONProductos(response.getJSONArray("P"));
                Toast.makeText(_context.getApplicationContext(),"Descarga de Productos Satisfactoria",
                        Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, e, errorResponse);
        Toast.makeText(_context.getApplicationContext(),"Error al Descargar Productos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        super.onFinish();

    }

    private void parseJSONProductos(JSONArray productosArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
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
