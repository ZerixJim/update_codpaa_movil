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


public class ResponseTiendas extends JsonHttpResponseHandler{

    Context _context;
    Locale locale;

    public ResponseTiendas(Context context){
        this._context = context;
        locale = new Locale("es_Mx");

    }

    @Override
    public void onStart() {
        super.onStart();


        //Toast.makeText(_context.getApplicationContext(),"Descargando Tiendas",Toast.LENGTH_SHORT).show();

        Log.d("ResponseTiendas","Start");
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
        Log.d("ResponseTiendas", "finish");

    }

    private void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
        b.vaciarTabla("clientes");
        Configuracion configuracion = new Configuracion(_context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
        String fecha = dFecha.format(c.getTime());

        for(int i=0; i<tiendasArray.length(); i++){
            b.insertarClientes(tiendasArray.getJSONObject(i).getInt("IT"),
                    tiendasArray.getJSONObject(i).getString("G"),
                    tiendasArray.getJSONObject(i).getString("S"),
                    tiendasArray.getJSONObject(i).getString("X"),
                    tiendasArray.getJSONObject(i).getString("Y"));
        }

        configuracion.setTiendas(fecha);

    }
}
