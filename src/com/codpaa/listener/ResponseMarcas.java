package com.codpaa.listener;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.db.BDopenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class ResponseMarcas extends JsonHttpResponseHandler{



    Context _context;
    Locale locale;



    public ResponseMarcas(Context context){
        this._context = context;
        locale = new Locale("es_MX");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ResponseMarcas","start");
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
        Log.d("ResponseMarca","finish");
    }

    private void parseJSONMarca(JSONArray marcaArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
        b.vaciarTabla("marca");

        Configuracion configuracion = new Configuracion(_context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
        String fecha = dFecha.format(c.getTime());

        for(int i= 0; i < marcaArray.length(); i++) {

            b.insertarMarca(marcaArray.getJSONObject(i).getInt("IM")
                    ,marcaArray.getJSONObject(i).getString("N")
                    ,marcaArray.getJSONObject(i).getString("logo"));
        }

        configuracion.setMarca(fecha);
    }
}
