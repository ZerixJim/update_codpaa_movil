package com.codpaa.listener;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codpaa.db.BDopenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ResponseRuta extends JsonHttpResponseHandler {

    Context _context;
    Locale locale;

    //private ProgressDialog pdia;

    public ResponseRuta(Context context){
        this._context = context;
        locale = new Locale("es_MX");
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

        //Toast.makeText(_context.getApplicationContext(),"Descargando Ruta",Toast.LENGTH_SHORT).show();
        Log.d("RRuta","iniciado");
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {
                parseJSONRuta(response.getJSONArray("V"));
            }catch (JSONException e){
                e.printStackTrace();
            }

            Toast.makeText(_context.getApplicationContext(),"Descarga Satisfactoria de Ruta",Toast.LENGTH_SHORT).show();
            Log.d("RRuta","success");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, e, errorResponse);

        Toast.makeText(_context.getApplicationContext(),"Error al descargar",Toast.LENGTH_SHORT).show();
        Log.d("RRuta","failure");

    }

    @Override
    public void onFinish() {
        super.onFinish();

        Log.d("RRuta","finish");
    }

    private void parseJSONRuta(JSONArray rutasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());
        b.vaciarTabla("visitaTienda");

        Configuracion configuracion = new Configuracion(_context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
        String fecha = dFecha.format(c.getTime());

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

        configuracion.setRuta(fecha);

    }
}
