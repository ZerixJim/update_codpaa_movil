package com.codpaa.listener;
/*
 * Created by grim on 2/02/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ResponseEncuesta extends JsonHttpResponseHandler {


    Context _context;
    Locale locale;



    public ResponseEncuesta(Context context){
        this._context = context;
        locale = new Locale("es_MX");

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
        //Configuracion configuracion = new Configuracion(_context);


        for (int i=0; i<preguntasArray.length(); i++ ) {
            ContentValues v = new ContentValues();
            v.put("id_pregunta", preguntasArray.getJSONObject(i).getInt("id_pregunta"));
            v.put("descripcion", preguntasArray.getJSONObject(i).getString("contenido"));
            v.put("id_encuesta", preguntasArray.getJSONObject(i).getInt("id_encuesta"));
            v.put("id_tipo", preguntasArray.getJSONObject(i).getInt("id_tipo"));
            v.put("id_marca", preguntasArray.getJSONObject(i).getInt("id_marca"));
            v.put("nombre_encuesta", preguntasArray.getJSONObject(i).getString("nombre_encuesta"));
            b.insertar("preguntas", v);

        }



    }

    @Override
    public void onFinish() {
        super.onFinish();

        Log.d("Encuesta", "end of listener");
    }
}
