package com.codpaa.listener;
/*
 * Created by grim on 2/02/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ResponseEncuesta extends JsonHttpResponseHandler {


    private Context _context;


    public ResponseEncuesta(Context context){
        this._context = context;

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {
                parseJSONPreguntas(response.getJSONArray("questions"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void parseJSONPreguntas(JSONArray preguntasArray) throws JSONException {

        BDopenHelper b = new BDopenHelper(_context.getApplicationContext());

        b.vaciarTabla("preguntas");
        Configuracion configuracion = new Configuracion(_context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());


        for (int i=0; i<preguntasArray.length(); i++ ) {


            try {
                ContentValues v = new ContentValues();
                v.put("id_pregunta", preguntasArray.getJSONObject(i).getInt("idPregunta"));
                v.put("descripcion", preguntasArray.getJSONObject(i).getString("pregunta"));
                v.put("id_encuesta", preguntasArray.getJSONObject(i).getInt("idEncuesta"));
                v.put("id_tipo", preguntasArray.getJSONObject(i).getInt("idTipo"));
                v.put("id_marca", preguntasArray.getJSONObject(i).getInt("idMarca"));
                v.put("nombre_encuesta", preguntasArray.getJSONObject(i).getString("nombreEncuesta"));
                v.put("tipo_encuesta", preguntasArray.getJSONObject(i).getInt("tipoEncuesta"));
                b.insertar("preguntas", v);

                if(preguntasArray.getJSONObject(i).getInt("idTipo") == 3){

                    JSONArray jsonArray = preguntasArray.getJSONObject(i).getJSONArray("opciones");
                    for (int j=0; j < jsonArray.length() ; j++){


                        try {
                            ContentValues cV = new ContentValues();
                            cV.put("idOpcion", jsonArray.getJSONObject(j).getInt("idOpcion"));
                            cV.put("opcion", jsonArray.getJSONObject(j).getString("opcion"));
                            cV.put("idPregunta", jsonArray.getJSONObject(j).getInt("idPregunta"));

                            b.insertar(DbEstructure.Opciones.TABLE_NAME, cV);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }


                }

            }catch (JSONException e){
                e.printStackTrace();

            }



        }

        configuracion.setEncuestaDisponibleByDay(fecha);

    }

    @Override
    public void onFinish() {
        super.onFinish();

        Log.d("Encuesta", "end of listener");
    }
}
