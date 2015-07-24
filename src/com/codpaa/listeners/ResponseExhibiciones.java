package com.codpaa.listeners;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.db.BDopenHelper;


public class ResponseExhibiciones extends JsonHttpResponseHandler {


    Context _context;


    public ResponseExhibiciones(Context context){
        this._context = context;




    }

    @Override
    public void onStart() {
        super.onStart();
        //pdia.show();

        //Toast.makeText(_context.getApplicationContext(),"Iniciando la descarga de Exhibiciones",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        if (response != null) {
            try {
                parseJSONExhi(response.getJSONArray("E"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            Toast.makeText(_context.getApplicationContext(),"Exhibiciones Descargadas",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers,e, errorResponse);



        Toast.makeText(_context.getApplicationContext(),"Error al descargar las Exhibiciones",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onFinish() {
        super.onFinish();

        //pdia.dismiss();
    }

    private void parseJSONExhi(JSONArray exhiArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
        b.vaciarTabla("exhibiciones");

        for(int i= 0; i < exhiArray.length(); i++) {
            b.insertarTipoExhibicion(exhiArray.getJSONObject(i).getInt("IE"),
                    exhiArray.getJSONObject(i).getString("N"));
        }
    }
}
