package com.codpaa.listener;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.db.BDopenHelper;


public class ResponseMarcas extends JsonHttpResponseHandler{



    Context _context;



    public ResponseMarcas(Context context){
        this._context = context;

    }

    @Override
    public void onStart() {
        super.onStart();

        //Toast.makeText(_context.getApplicationContext(),"Descargando Marcas",Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
        super.onSuccess(statusCode, headers,response);

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
    public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers,e, errorResponse);
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

            b.insertarMarca(marcaArray.getJSONObject(i).getInt("IM")
                    ,marcaArray.getJSONObject(i).getString("N")
                    ,marcaArray.getJSONObject(i).getString("logo"));
        }
    }
}
