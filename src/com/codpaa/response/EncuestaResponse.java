package com.codpaa.response;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.codpaa.db.BDopenHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by Gus on 21/11/2016.
 */

public class EncuestaResponse extends JsonHttpResponseHandler {

    private Context context;


    public EncuestaResponse(Context context){
        this.context = context;
    }



    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {
                if (response.getBoolean("insert")){

                    //Log.d("json", response.toString());



                    SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();
                    JSONArray jsonArray = response.getJSONArray("insertados");

                    //Log.d("json array", jsonArray.toString());

                    for ( int i = 0; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Log.d("json value", "" + jsonObject.getInt("idEncuesta"));

                        db.execSQL("update encuesta_respuestas set estatus=2 where idEncuesta="+jsonObject.getInt("idEncuesta")+" " +
                                "and idTienda="+jsonObject.getInt("idTienda")+" and idPregunta="+ jsonObject.getInt("idPregunta"));


                    }


                    db.close();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
